//
//  Promemoria.h
//  Memorandum
//
//  Created by Luca on 13/07/22.
//

#import <Foundation/Foundation.h>
#import "StatoPromemoria.h"
#import "Posizione.h"

NS_ASSUME_NONNULL_BEGIN

@interface Promemoria : NSObject

/**
 Titolo del promemoria
 */
@property (nonatomic, strong) NSString *titolo;

/**
 descrizione del promemoria
 */
@property (nonatomic, strong) NSString *descrizione;

/**
 data e ora del promemoria
 */
@property (nonatomic, strong) NSDate *dataOra;

/**
Stato del promemoria
 */
@property (nonatomic, strong) StatoPromemoria* stato;

/**
 Posizione del promemoria
 */
@property (nonatomic, strong) Posizione* luogo;

-(instancetype) initWithtitolo : (NSString*) titolo
                   descrizione : (NSString*) descrizione
                       dataOra : (NSDate*) dataOra
                   statoAttivo : (BOOL) statoAttivo
                  statoScaduto : (BOOL) statoScaduto
               statoCompletato : (BOOL) statoCompletato
                      latLuogo : (double) latLuogo
                      lonLuogo : (double) lonLuogo;

-(instancetype) initWithtitolo : (NSString*) titolo
                   descrizione : (NSString*) descrizione
                       dataOra : (NSDate*) dataOra
                         stato : (StatoPromemoria*) stato
                         luogo : (Posizione*) luogo;

-(instancetype) initWithtitolo : (NSString*) titolo
                   descrizione : (NSString*) descrizione
                       dataOra : (NSDate*) dataOra
                  statoStringa : (NSString*) stato
                         luogo : (Posizione*) luogo;

/**
 Costruttore per inizilizzare il promemoria con una stringa json
 */
-(instancetype) initWithJson : (NSDictionary*) json;

/**
 Ritorna tutto il promemoria scomposto in un dictionary
 */
-(NSDictionary *)getDictionary;

/**
 Metodo utilizzato per l ordinamento
 */
-(NSComparisonResult)compare:(Promemoria *)p;

/**
 restituisce una stringa contenente tutte le info del promemoria
 */
-(NSString*) toString;

@end

NS_ASSUME_NONNULL_END
