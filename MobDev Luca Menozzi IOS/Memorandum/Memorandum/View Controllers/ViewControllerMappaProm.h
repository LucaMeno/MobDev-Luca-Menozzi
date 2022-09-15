//
//  ViewControllerMappaPromViewController.h
//  Memorandum
//
//  Created by Luca on 15/07/22.
//

/*
 ViewController per mostrare i promemoria sulla mappa
 */

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "GestionePromemoria.h"

NS_ASSUME_NONNULL_BEGIN

@interface ViewControllerMappaProm : UIViewController {
    /**
     Per mostrare tutti i promemoria attivi sull mappa
     */
    GestionePromemoria* _gp;
};

/**
 Per mostrare un singolo promemoria sulla mappa
 */
@property(nonatomic, strong) Promemoria* prom;

/**
 Mappa sulla quale lavorare
 */
@property(weak, nonatomic) IBOutlet MKMapView *mapView;

@end

NS_ASSUME_NONNULL_END
