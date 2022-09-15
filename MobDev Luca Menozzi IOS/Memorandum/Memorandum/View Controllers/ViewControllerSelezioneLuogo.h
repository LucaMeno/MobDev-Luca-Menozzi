//
//  ViewControllerSelezioneLuogo.h
//  Memorandum
//
//  Created by Luca on 16/08/22.
//

/**
 View per la selezione di una posizione in modifica/aggiungi
 */

#import <UIKit/UIKit.h>
#import "Posizione.h"
#import "ViewControllerSelezioneLuogo.h"

NS_ASSUME_NONNULL_BEGIN

@interface ViewControllerSelezioneLuogo : UIViewController<UIGestureRecognizerDelegate>

/**
 Posizione selezionata
 */
@property(nonatomic) Posizione* pos;

/**
 Posizione precedente
 */
@property(nonatomic) Posizione* posPrec;

@end


NS_ASSUME_NONNULL_END
