//
//  ViewController.m
//  Memorandum
//
//  Created by Luca on 15/07/22.
//

/**
 ViewController per mostrare solo i promemoria attivi
 */

#import "ViewControllerPromAttivi.h"
#import <MapKit/MapKit.h>

@import UserNotifications;

@interface ViewControllerPromAttivi ()

@end

@implementation ViewControllerPromAttivi


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self controlloPermessiPosizione];
    
    //Creo il gestore dei promemoria
    _gp = [[GestionePromemoria alloc] init];
    
    //Preparo le geofence
    _geoFence = [NSMutableArray new];
    [self setGeoFence];
    
    //Preparo la tabella
    _tableViewPromemoriaAttivi.dataSource  = self;
    _tableViewPromemoriaAttivi.delegate  = self;
    
    //Preparo il gestore della posizione
    self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    self.locationManager.distanceFilter = 100;
    self.locationManager.delegate = self;
    [self.locationManager startUpdatingLocation];
    
    
    //Controllo l autorizzazione per le notifiche
    _isGrantedNotificationAccess = false;
    UNUserNotificationCenter* center =[UNUserNotificationCenter currentNotificationCenter];
    
    UNAuthorizationOptions options = UNAuthorizationOptionAlert + UNAuthorizationOptionSound;
    
    //Richiesta abilitazione notifiche
    [center requestAuthorizationWithOptions:options completionHandler:^(BOOL granted, NSError* _Nullable error){
        self->_isGrantedNotificationAccess = true;
        NSLog(@"Notifiche permesse");
    }];
    
    //Imposto l observer per aggiornare le geofence in caso di modifiche/aggiunta di promemoria
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(setGeoFence)
                                                 name:@"Update"
                                               object:nil];
}

-(void) controlloPermessiPosizione{
    //Controllo se sono attivi i servizi della posizione
    if ([CLLocationManager locationServicesEnabled]){
        
        NSLog(@"Servizio posizione attivo");
        
        //Controllo se e possibile accedere alla posizione
        if ([self.locationManager authorizationStatus] == kCLAuthorizationStatusDenied ||
            [self.locationManager authorizationStatus] == kCLAuthorizationStatusRestricted ||
            [self.locationManager authorizationStatus] == kCLAuthorizationStatusNotDetermined ||
            [self.locationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedWhenInUse) {
            
            //Se non posso accedere alla posizione avviso l utente
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Errore"
                                                                           message:@"Nessun accesso alla posizione, impostare la possibilita di accesso su 'sempre' per poter usufruire a pieno dell applicazione"
                                                                    preferredStyle:UIAlertControllerStyleAlert];
            
            UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK"
                                                         style:UIAlertActionStyleDefault
                                                       handler:^(UIAlertAction * _Nonnull action) {exit(0);}];
            
            [alert addAction:ok];
            [self presentViewController:alert animated:YES completion:nil];

        } else {
            NSLog(@"Accesso alla posizione ok");
        }
    } else {
        NSLog(@"Servizio posizione non attivo");
    }
    

}

/**
 Imposto tutte le geofence di tutti i promemoria
 */
-(void) setGeoFence{
    NSLog(@"Imposto geofence");
    
    //Rimuovo le precedenti geofence
    for(CLRegion* r in _geoFence){
        [self.locationManager stopMonitoringForRegion:r];
    }
    [_geoFence removeAllObjects];
    
    //Imposto le geofence
    for(Promemoria *p in _gp.getListaAttivi){
        
        CLLocationCoordinate2D tmp = {.latitude = p.luogo.latitudine, .longitude = p.luogo.longitudine};
        CLRegion *region_tmp = [[CLCircularRegion alloc]initWithCenter:tmp
                                                                radius:100.0
                                                            identifier:p.titolo];
        [self.locationManager startMonitoringForRegion:region_tmp];
        [_geoFence addObject:region_tmp];
        
        //NSLog(@"Setto Geofence per: %@",p.titolo);
    }
}


/**
 Ritorno il nuomero di righe della tabella
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [[_gp getListaAttivi] count];
}

/**
 Ritorno la lista con cui popolare la tabella
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    cell.textLabel.text = [[[_gp getListaAttivi] objectAtIndex:indexPath.row] titolo];
    return cell;
}

/**
 Inizializzatore del gestore delle posizioni
 */
- (CLLocationManager *)locationManager{
    if(!_locationManager) _locationManager = [[CLLocationManager alloc] init];
    return _locationManager;
}

/**
 Metodo chiamato ogni volta che viene aggiornata la posizione
 */
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations{
    //CLLocation *currentLocation = [locations lastObject];
    NSLog(@"POSIZIONE AGGIORNATA");
}

/**
 Metodo chiamato ogni volta che si entra in una regione associata ad una geofence
 */
- (void)locationManager:(CLLocationManager *)manager didEnterRegion:(CLRegion *)region{
    NSLog(@"Vicino a: %@",region.identifier);
    [self notifica:region.identifier];
}

/**
 Genera una notifica locale con la stringa passata
 */
-(void)notifica: (NSString*)str{
    
    if(_isGrantedNotificationAccess){
        UNUserNotificationCenter* center =[UNUserNotificationCenter currentNotificationCenter];
        UNMutableNotificationContent* content = [[UNMutableNotificationContent alloc] init];
        content.title = @"Promemoria";
        content.body = [NSString stringWithFormat:@"Hai un promemoria nelle vicinanze: %@", str];
        content.sound=[UNNotificationSound defaultSound];
        
        UNTimeIntervalNotificationTrigger* trigger = [UNTimeIntervalNotificationTrigger triggerWithTimeInterval:3 repeats:NO];
        
        UNNotificationRequest* request = [UNNotificationRequest requestWithIdentifier:@"Notifica" content:content trigger:trigger];
        
        [center addNotificationRequest:request withCompletionHandler:^(NSError * _Nullable error) {
            if (error != nil) {
                NSLog(@"%@", error.localizedDescription);
            }
        }];
    }
}

/**
 Metodo per spostamento tra view
 */
- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    
    //Spostameto verso la view per info singolo promemoria
    if([segue.identifier isEqualToString:@"mostraInfoProm"]){
        if([segue.destinationViewController isKindOfClass:[ViewControllerInfoProm class]]){
            ViewControllerInfoProm *infoProm = (ViewControllerInfoProm*) segue.destinationViewController;
            
            //Prendo il promemoria selezionato e lo passo alla nuova finestra
            NSIndexPath *indexPath = [_tableViewPromemoriaAttivi indexPathForCell:sender];
            Promemoria *prom = [[_gp getListaAttivi] objectAtIndex:indexPath.row];
            infoProm.prom = prom;
            infoProm.gp = _gp;
        }
    }
}

/**
 Metodo chiamato la view torna visibile
 */
-(void) viewDidAppear:(BOOL)animated{
    //Aggiorno i promemoria attivi
    [_tableViewPromemoriaAttivi reloadData];
    
    //Se non ci sono promemoria attivi mostro una messagebox
    if([[_gp getListaAttivi] count] == 0)
    {
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Info" message:@"Nessun promemoria attivo" preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [self.navigationController popToRootViewControllerAnimated:YES];
        }];
        
        [alert addAction:ok];
        [self presentViewController:alert animated:YES completion:nil];
    }
}
@end
