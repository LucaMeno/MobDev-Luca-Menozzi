//
//  ViewControllerModificaProm.m
//  Memorandum
//
//  Created by Luca on 07/08/22.
//

/**
 Viev Controller per la modifica di un promemoria
 */

#import "ViewControllerModificaProm.h"

@interface ViewControllerModificaProm ()

/**
 Elementi grafici
 */
@property (weak, nonatomic) IBOutlet UITextField *tbTitolo;
@property (weak, nonatomic) IBOutlet UIDatePicker *dataOra;
@property (weak, nonatomic) IBOutlet UITextView *tbDescrzione;

@property (weak, nonatomic) IBOutlet UISegmentedControl *statoProm;
@property (strong, nonatomic) Posizione* pos;

@end

@implementation ViewControllerModificaProm

- (void)viewDidLoad {
    [super viewDidLoad];
    //Popolo i campi della grafica
    [self setDati];
    //Salvo la posizine precedente
    _posPrec = [[Posizione alloc] initWithlatitudine: self.prom.luogo.latitudine  longitudine:self.prom.luogo.longitudine];
}

-(void)setDati{
    
    //Creo la posizione per un eventuale modifica
    self.pos = [[Posizione alloc] initWithlatitudine:self.prom.luogo.latitudine longitudine:self.prom.luogo.longitudine];
    
    //Setto il titolo
    _tbTitolo.text = _prom.titolo;
    
    //Setto lo stato
    if(_prom.stato.attivo)
        [_statoProm setSelectedSegmentIndex : 0];
    if(_prom.stato.completato)
        [_statoProm setSelectedSegmentIndex : 1];
    if(_prom.stato.scaduto)
        [_statoProm setSelectedSegmentIndex : 2];
    
    //Setto la descrizione
    _tbDescrzione.text = _prom.descrizione;
    
    
    //Setto la data
    _dataOra.date = _prom.dataOra;
}

/**
 Controlli prima della modifica
 */
-(bool)checkDati{
    
    if([_tbTitolo.text isEqual:@""]){
        [self messageBox:@"Inserire titolo"];
        return false;
    }
    
    if([_tbDescrzione.text isEqual:@""]){
        [self messageBox:@"Inserire descrizione"];
        return false;
    }
    
    return true;
}

/**
 Mostra una MessageBox contentente la stringa passata come parametro
 */
-(void)messageBox: (NSString*)str{
    
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Info" message:str preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) { }];
    [alert addAction:ok];
    [self presentViewController:alert animated:YES completion:nil];
    return;
}

/**
 Quando viene premuto il pulsante salva
 */
- (IBAction)salvaProm:(id)sender {
    
    //Controllo che tutti i campi abbiano valore
    if(![self checkDati])
        return;
    
    //Modifico i dati del promemoria
    _prom.titolo = _tbTitolo.text;
    
    _prom.descrizione = _tbDescrzione.text;
    
    if([_statoProm selectedSegmentIndex] == 0)
        [_prom.stato setAttivo];
    
    if([_statoProm selectedSegmentIndex] == 1)
        [_prom.stato setCompletato];
    
    if([_statoProm selectedSegmentIndex] == 2)
        [_prom.stato setScaduto];
    
    _prom.dataOra = _dataOra.date;
    
    self.prom.luogo = self.pos;
    
    //Message box avviso di modifica promemoria
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Info" message:@"Promemoria modificato" preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        UIViewController *prevVC = [self.navigationController.viewControllers objectAtIndex:1];
        [self.navigationController popToViewController:prevVC animated:YES];
    }];
    
    //Salvo i dati su file
    [_gp scriviFile];
    
    [alert addAction:ok];
    [self presentViewController:alert animated:YES completion:nil];
    
    //Aggiorno le geofence dei promemoria se e stata modificata la posizione
    if(_posPrec.latitudine != self.prom.luogo.latitudine || _posPrec.longitudine != self.prom.luogo.longitudine){
        NSNotification *myNotification = [NSNotification notificationWithName:@"Update"
                                                                       object:self
                                                                     userInfo:nil];
        
        [[NSNotificationCenter defaultCenter] postNotification:myNotification];
    }
    
}

/**
 Metodo per spostamento tra view
 */
#pragma mark - Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    //Per eventuale modifica della posizione
    if([segue.identifier isEqualToString:@"mostraSelezioneLuogo"]){
        
        if([segue.destinationViewController isKindOfClass:[ViewControllerSelezioneLuogo class]]){
            ViewControllerSelezioneLuogo *selLuogo = (ViewControllerSelezioneLuogo*) segue.destinationViewController;
            //Passo i dati alla nuova view
            selLuogo.pos = _pos;
            selLuogo.posPrec = self.prom.luogo;
        }
    }
}


@end
