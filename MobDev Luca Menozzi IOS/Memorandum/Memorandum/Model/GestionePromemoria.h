//
//  GestionePromemoria.h
//  Memorandum
//
//  Created by Luca on 14/07/22.
//

/**
 Classe per la gestionde dei promemoria
 */

#import <Foundation/Foundation.h>
#import "Promemoria.h"

NS_ASSUME_NONNULL_BEGIN

/**
 Lista di promemoria statica per essere comune a tutte le classi che la utilizzano
 */
static NSMutableArray* prom = nil;

@interface GestionePromemoria : NSObject

-(instancetype)init;

/**
 Ritorna la lista di promemoria
 */
-(NSMutableArray*) getLista;

/**
 Ritorna la lista dei soli promemoria attivi
 */
-(NSMutableArray*) getListaAttivi;

/**
 Aggiunge un promemoria alla lista
 */
-(void) addPromemoria:(Promemoria *)pr;

/**
 Rimuove un promemoria dalla lista
 */
-(void) remouvePromemoria:(Promemoria *)pr;

/**
 Salva i dati dei promemoria per la persistenza
 */
-(void) scriviFile;

/**
 Legge i dati salvati, se ci sono, per la persistenza
 Se non ci sono prom salvati popola la lista con quelli di default
 */
-(void) leggiFile;

@end

NS_ASSUME_NONNULL_END
