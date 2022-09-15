//
//  ViewController.h
//  Memorandum
//
//  Created by Luca on 15/07/22.
//

/**
 ViewController per mostrare solo i promemoria attivi
 */

#import <UIKit/UIKit.h>
#import "GestionePromemoria.h"
#import "ViewControllerInfoProm.h"
#import "ViewControllerAggiungiProm.h"


@interface ViewControllerPromAttivi : UIViewController <UITableViewDelegate, UITableViewDataSource, CLLocationManagerDelegate> {
    /**
     Promemoria da gestire
     */
    GestionePromemoria* _gp;
    
    /**
     Tabella nella quale inserire i promemoria
     */
    IBOutlet UITableView* _tableViewPromemoriaAttivi;
    
    /**
     Gestore della posizione
     */
    CLLocationManager* _locationManager;
    
    /**
     Lista di geofence da monitorare
     */
    NSMutableArray* _geoFence;
    
    /**
     Utilizato per l accesso alle notifihce
     */
    BOOL _isGrantedNotificationAccess;
}


/**
 Promemoria da gestire
 */
//@property(nonatomic, strong) GestionePromemoria* gp;

/**
 Tabella nella quale inserire i promemoria
 */
//@property (weak, nonatomic) IBOutlet UITableView *tableViewPromemoriaAttivi;

/**
 Gestore della posizione
 */
//@property(nonatomic, strong)CLLocationManager *locationManager;

/**
 Lista di geofence da monitorare
 */
//@property(nonatomic, strong)NSMutableArray *geoFence;

/**
 Gestore della posizione
 */
//@property(nonatomic)BOOL isGrantedNotificationAccess;



@end

