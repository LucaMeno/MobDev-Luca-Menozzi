//
//  ViewControllerInfoProm.h
//  Memorandum
//
//  Created by Luca on 15/07/22.
//

/*
 ViewController per mostrare i dettagli di un promemoria
 */

#import <UIKit/UIKit.h>
#include "GestionePromemoria.h"
#include "ViewControllerModificaProm.h"
#include "ViewControllerMappaProm.h"

NS_ASSUME_NONNULL_BEGIN

@interface ViewControllerInfoProm : UIViewController

/**
 Promemoria da visualizzare
 */
@property(nonatomic, strong) Promemoria* prom;

/**
 Tutti i promemoria
 */
@property(nonatomic, strong) GestionePromemoria* gp;

@end

NS_ASSUME_NONNULL_END
