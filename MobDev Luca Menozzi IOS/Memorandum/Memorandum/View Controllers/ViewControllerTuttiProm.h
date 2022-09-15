//
//  ViewControllerTuttiProm.h
//  Memorandum
//
//  Created by Luca on 15/07/22.
//

/*
 ViewController per mostrare tutti i promemoria disponibili
 */

#import <UIKit/UIKit.h>
#import "GestionePromemoria.h"
#import "ViewControllerInfoProm.h"
#import "ViewControllerAggiungiProm.h"

NS_ASSUME_NONNULL_BEGIN

@interface ViewControllerTuttiProm : UIViewController <UITableViewDelegate, UITableViewDataSource>{
    /**
     Promemoria da mostrare
     */
    GestionePromemoria* _gp;
};

/**
 Tabella da utilizzare per rappresentare i promemoria
 */
@property (weak, nonatomic) IBOutlet UITableView *tableViewPromemoria;

@end

NS_ASSUME_NONNULL_END
