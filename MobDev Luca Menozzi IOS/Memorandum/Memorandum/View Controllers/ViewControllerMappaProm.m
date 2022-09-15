//
//  ViewControllerMappaPromViewController.m
//  Memorandum
//
//  Created by Luca on 15/07/22.
//

/*
 ViewController per mostrare i promemoria attivi sulla mappa
 */

#import "ViewControllerMappaProm.h"

@interface ViewControllerMappaProm ()<MKMapViewDelegate>

@end

@implementation ViewControllerMappaProm

- (void)viewDidLoad {
    [super viewDidLoad];
}

/**
 Setto tutti i pin necessari sulla mappa
 */
-(void)setPin{
    //mostra tutti i pin dei promemoria
    if(self.prom == nil)
    {
        NSLog(@"Tutti prom");
        if(_gp == nil)
            _gp = [GestionePromemoria alloc];
        CLLocationCoordinate2D coord = {.latitude = 44.8036, .longitude = 10.33};
        MKCoordinateSpan span ={.latitudeDelta = 13.050f, .longitudeDelta = 13.05f};
        MKCoordinateRegion region ={ coord, span};
        [_mapView setRegion:region];
        
        //setto tutti pin dei promemoria attivi
        for(Promemoria *p in _gp.getListaAttivi){
            MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
            CLLocationCoordinate2D coordPin = {.latitude = p.luogo.latitudine, .longitude = p.luogo.longitudine};
            [point setCoordinate:coordPin];
            [point setTitle:p.titolo];
            [point setSubtitle:p.descrizione];
            [_mapView addAnnotation:point];
        }
    }
    //mostra singolo promemoria
    else
    {
        NSLog(@"Singolo prom");
        CLLocationCoordinate2D coord = {.latitude = self.prom.luogo.latitudine, .longitude = self.prom.luogo.longitudine};
        MKCoordinateSpan span ={.latitudeDelta = 0.005f, .longitudeDelta = 0.005f};
        MKCoordinateRegion region ={ coord, span};
        [_mapView setRegion:region];
        
        MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
        CLLocationCoordinate2D coordPin ={.latitude = self.prom.luogo.latitudine, .longitude = self.prom.luogo.longitudine};
        [point setCoordinate:coordPin];
        [point setTitle:self.prom.titolo];
        [_mapView addAnnotation:point];
    }
}

-(void) viewDidAppear:(BOOL)animated{
    //Quando appare la view mostro i Pin
    [self setPin];
}

@end
