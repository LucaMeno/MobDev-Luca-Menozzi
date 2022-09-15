//
//  ViewControllerSelezioneLuogo.m
//  Memorandum
//
//  Created by Luca on 16/08/22.
//

/**
 View per la selezione di una posizione in modifica/aggiungi
 */

#import "ViewControllerSelezioneLuogo.h"
#import <MapKit/MapKit.h>

@interface ViewControllerSelezioneLuogo ()<MKMapViewDelegate>

/**
 Elementi grafic
 */
@property (weak, nonatomic) IBOutlet MKMapView *mapViewSelezione;
@property (strong, nonatomic) IBOutlet MKPointAnnotation *point;

@end

@implementation ViewControllerSelezioneLuogo

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //Aggiunta gesture UILongPressGestureRecognizer
    UILongPressGestureRecognizer* lpgr = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPress:)];
    lpgr.minimumPressDuration = 1.3;
    lpgr.delegate = self;
    [self.mapViewSelezione addGestureRecognizer:lpgr];
    
    //Imposto pin precedente
    if(self.posPrec != nil){
        CLLocationCoordinate2D coord = {.latitude = self.posPrec.latitudine, .longitude = self.posPrec.longitudine};
        MKCoordinateSpan span ={.latitudeDelta = 0.005f, .longitudeDelta = 0.005f};
        MKCoordinateRegion region ={ coord, span};
        [self.mapViewSelezione setRegion:region];
        
        MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
        CLLocationCoordinate2D coordPin ={.latitude = self.posPrec.latitudine, .longitude = self.posPrec.longitudine};
        [point setCoordinate:coordPin];
        [point setTitle:@"Posizione precedente"];
        [self.mapViewSelezione addAnnotation:point];
    }
    
}

- (void) handleLongPress:(UILongPressGestureRecognizer *)gestureRecognizer {
    
    //Scelta posizione su mappa
    if (gestureRecognizer.state == UIGestureRecognizerStateBegan) {
        
        //Se presente, rimuovo vecchio pin
        if(self.pos.latitudine != self.posPrec.latitudine){
            if(self.pos.longitudine != self.posPrec.longitudine){
                [self.mapViewSelezione removeAnnotation:self.point];
                self.point = nil;
            }
        }
        
        //Setto nuovo pin sulla mappa
        CLLocationCoordinate2D coord = [self.mapViewSelezione convertPoint:[gestureRecognizer locationInView:self.mapViewSelezione] toCoordinateFromView:self.mapViewSelezione];
        
        [self.mapViewSelezione setCenterCoordinate:coord animated:YES];
        
        MKCoordinateSpan span ={.latitudeDelta = 0.005f, .longitudeDelta = 0.005f};
        MKCoordinateRegion region ={ coord, span};
        [self.mapViewSelezione setRegion:region];
        
        self.point = [[MKPointAnnotation alloc] init];
        CLLocationCoordinate2D coordPin ={.latitude = coord.latitude, .longitude = coord.longitude};
        [self.point setCoordinate:coordPin];
        [self.point setTitle:@"Nuovo Promemoria"];
        [self.mapViewSelezione addAnnotation:self.point];
        
        //Salvo i dati
        [self.pos setLatitudine:coord.latitude];
        [self.pos setLongitudine:coord.longitude];
        
        NSLog(@"nuova LAT: %f nuova LON: %f", coord.latitude, coord.longitude);
        
        //MessageBox ok selezione
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Info" message:@"Posizione impostata" preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {}];
        
        [alert addAction:ok];
        [self presentViewController:alert animated:YES completion:nil];
    }
}



@end
