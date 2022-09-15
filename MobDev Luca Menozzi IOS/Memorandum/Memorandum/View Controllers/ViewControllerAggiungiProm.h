//
//  ViewControllerAggiungiProm.h
//  Memorandum
//
//  Created by Luca on 09/08/22.
//

/**
 ViewController per aggiunta di un promemoria
 */

#import <UIKit/UIKit.h>
#import "GestionePromemoria.h"
#import "ViewControllerSelezioneLuogo.h"

NS_ASSUME_NONNULL_BEGIN

@interface ViewControllerAggiungiProm : UIViewController{
    /**
     Tutti i promemoria
     */
    GestionePromemoria* _gp;
    
    /**
     Posizione nuovo promemoria
     */
    Posizione* _pos;
}



@end

NS_ASSUME_NONNULL_END
