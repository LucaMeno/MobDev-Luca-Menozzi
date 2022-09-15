//
//  Promemoria.m
//  Memorandum
//
//  Created by Luca on 13/07/22.
//

#import "Promemoria.h"

@implementation Promemoria


-(instancetype) initWithtitolo : (NSString*) titolo
                   descrizione : (NSString*) descrizione
                       dataOra : (NSDate*) dataOra
                   statoAttivo : (BOOL) statoAttivo
                  statoScaduto : (BOOL) statoScaduto
               statoCompletato : (BOOL) statoCompletato
                      latLuogo : (double) latLuogo
                      lonLuogo : (double) lonLuogo{
    
    if((self = [super init])) {
        _titolo = [titolo copy];
        _descrizione = [descrizione copy];
        _dataOra = [dataOra copy];
        _stato = [[StatoPromemoria alloc] initWithBoolAttivo:statoAttivo scaduto:statoScaduto completato:statoCompletato];
        _luogo = [[Posizione alloc] initWithlatitudine:latLuogo longitudine:lonLuogo];
    }
    return self;
    
}

-(instancetype) initWithtitolo : (NSString*) titolo
                   descrizione : (NSString*) descrizione
                       dataOra : (NSDate*) dataOra
                         stato : (StatoPromemoria*) stato
                         luogo : (Posizione*) luogo{
    if((self = [super init])) {
        _titolo = [titolo copy];
        _descrizione = [descrizione copy];
        _dataOra = [dataOra copy];
        _stato = [stato copy];
        _luogo = [luogo copy];
    }
    return self;
}

-(instancetype) initWithtitolo : (NSString*) titolo
                   descrizione : (NSString*) descrizione
                       dataOra : (NSDate*) dataOra
                  statoStringa : (NSString*) stato
                         luogo : (Posizione*) luogo{
    if((self = [super init])) {
        _titolo = [titolo copy];
        _descrizione = [descrizione copy];
        _dataOra = [dataOra copy];
        _stato = [[StatoPromemoria alloc] initWithString: stato];
        _luogo = luogo;
    }
    return self;
    
}

/**
 Costruttore per inizilizzare il promemoria con una stringa json
 */
-(instancetype) initWithJson : (NSDictionary*) json{
    if((self = [super init])) {
        
        _titolo = [json objectForKey:@"titolo"];
        _descrizione = [json objectForKey:@"descrizione"];
        _stato = [[StatoPromemoria alloc] initWithString: [json objectForKey:@"stato"]];
        
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss zzz"];
        self.dataOra = [dateFormatter dateFromString: [json objectForKey:@"dataOra"]];
        
        _luogo = [[Posizione alloc] initWithlatitudine:[[json objectForKey:@"lat"] doubleValue] longitudine:[[json objectForKey:@"lon"] doubleValue]];
        
    }
    return self;
}

/**
 Metodo utilizzato per l ordinamento
 */
- (NSComparisonResult)compare:(Promemoria *)p {
    return [self.dataOra compare:p.dataOra];
}

/**
 restituisce una stringa contenente tutte le info del promemoria
 */
-(NSString*) toString {
    NSMutableString *tmp = [NSMutableString string];
    [tmp appendString:_titolo];
    [tmp appendString:@" "];
    [tmp appendString:_descrizione];
    [tmp appendString:@" "];
    NSString *dateString = [NSDateFormatter localizedStringFromDate:[NSDate date]
                                                          dateStyle:NSDateFormatterShortStyle
                                                          timeStyle:NSDateFormatterFullStyle];
    [tmp appendString:dateString];
    
    [tmp appendString:@"Stato: "];
    [tmp appendString:[_stato toString]];
    
    if(_luogo != nil) {
        [tmp appendString:@" Luogo: "];
        [tmp appendString:[_luogo toString]];
    } else {
        [tmp appendString:@" Luogo non presente"];
    }
    return (NSString *)tmp;
}

/**
 Ritorna tutto il promemoria scomposto in un dictionary
 */
-(NSDictionary *)getDictionary {
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss zzz"];
    NSString *dateString = [dateFormatter stringFromDate:_dataOra];
    
    NSNumber *lat = [NSNumber numberWithDouble:self.luogo.latitudine];
    NSNumber *lon = [NSNumber numberWithDouble:self.luogo.longitudine];
    
    return [NSDictionary dictionaryWithObjectsAndKeys:
            self.titolo,@"titolo",
            dateString,@"dataOra",
            self.stato.getStato, @"stato",
            self.descrizione, @"descrizione",
            [lat stringValue], @"lat",
            [lon stringValue], @"lon",
            nil];
}


@end
