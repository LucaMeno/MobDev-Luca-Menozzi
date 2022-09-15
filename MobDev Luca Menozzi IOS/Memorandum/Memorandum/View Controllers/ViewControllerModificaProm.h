//
//  ViewControllerModificaProm.h
//  Memorandum
//
//  Created by Luca on 07/08/22.
//

/**
 Viev Controller per la modifica di un promemoria
 */

#import <UIKit/UIKit.h>
#import "Promemoria.h"
#import "GestionePromemoria.h"
#import "ViewControllerSelezioneLuogo.h"

NS_ASSUME_NONNULL_BEGIN

@interface ViewControllerModificaProm : UIViewController{
    /**
     Posizione prima della modifica
     */
    Posizione* _posPrec;
};

/**
 Promemoria da modificare
 */
@property(nonatomic, strong) Promemoria* prom;

/**
 Tutti i promemoria
 */
@property(nonatomic, strong) GestionePromemoria* gp;

@end

NS_ASSUME_NONNULL_END
