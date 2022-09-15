//
//  StatoPromemoria.m
//  Memorandum
//
//  Created by Luca on 14/07/22.
//

#import "StatoPromemoria.h"

@implementation StatoPromemoria

-(instancetype) initWithString : (NSString*) stato{
    if(self = [super init]){
        if([stato isEqualToString: @"a"]) {
            _attivo = true;
            _scaduto = false;
            _completato = false;
        }
        if([stato isEqualToString: @"s"]) {
            _attivo = false;
            _scaduto = true;
            _completato = false;
        }
        if([stato isEqualToString: @"c"]) {
            _attivo = false;
            _scaduto = false;
            _completato = true;
        }
        
    }
    return self;
    
}


-(instancetype) initWithStringAttivo : (NSString*) attivo
                             scaduto : (NSString*) scaduto
                          completato : (NSString*) completato{
    if(self = [super init]){
        if ([attivo isEqualToString:@"attivo"])
        {
            _attivo = true;
        } else {
            _attivo = false;
        }
        if ([scaduto isEqualToString:@"scaduto"])
        {
            _scaduto = true;
        } else {
            _scaduto = false;
        }
        if ([completato isEqualToString:@"completato"])
        {
            _completato = true;
        } else {
            _completato = false;
        }
    }
    return self;
}

-(instancetype) initWithBoolAttivo : (BOOL) attivo
                           scaduto : (BOOL) scaduto
                        completato : (BOOL) completato{
    if(self = [super init]){
        _attivo = attivo;
        _scaduto = scaduto;
        _completato = completato;
    }
    return self;
}


/**
 Restituisce una stringa contenente tutte le info dello stato
 */
-(NSString*) toString {
    
    if(_completato)
        return @"Completato";
    
    if(_attivo)
    {
        if(_scaduto)
            return @"Attivo/Scaduto";
        else
            return @"Attivo";
    }
    
    if(_scaduto)
        return @"Scaduto";
    
    return @"errore";
}


-(void) setAttivo{
    _attivo = true;
    _scaduto = false;
    _completato = false;
}

-(void) setScaduto{
    _attivo = false;
    _scaduto = true;
    _completato = false;
}

-(void) setCompletato
{
    _attivo = false;
    _scaduto = false;
    _completato = true;
}

/**
 Ritorna lo stato corrente sotto forma di stringa
 */
-(NSString*) getStato
{
    if(_completato)
        return @"c";
    
    if(_attivo)
        return @"a";
    
    if(_scaduto)
        return @"s";
    
    return @"errore";
}
@end
