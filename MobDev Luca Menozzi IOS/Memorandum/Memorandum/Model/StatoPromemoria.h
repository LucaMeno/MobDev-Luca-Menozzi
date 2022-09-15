//
//  StatoPromemoria.h
//  Memorandum
//
//  Created by Luca on 14/07/22.
//


#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface StatoPromemoria : NSObject

/**
 Possibili stati dei promemoria
 */
@property (nonatomic, assign, getter=isAttivo) BOOL attivo;
@property (nonatomic, assign, getter=isScaduto) BOOL scaduto;
@property (nonatomic, assign, getter=isCompletato) BOOL completato;

-(instancetype) initWithString : (NSString*) stato;

-(instancetype) initWithStringAttivo : (NSString*) attivo
                             scaduto : (NSString*) scaduto
                          completato : (NSString*) completato;

-(instancetype) initWithBoolAttivo : (BOOL) attivo
                           scaduto : (BOOL) scaduto
                        completato : (BOOL) completato;


/**
 Restituisce una stringa contenente tutte le info dello stato
 */
-(NSString*) toString;


-(void) setAttivo;
-(void) setScaduto;
-(void) setCompletato;

/**
 Ritorna lo stato corrente sotto forma di stringa
 */
-(NSString*) getStato;

@end

NS_ASSUME_NONNULL_END
