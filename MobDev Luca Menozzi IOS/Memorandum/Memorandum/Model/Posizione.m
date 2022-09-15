//
//  posizione.m
//  Memorandum
//
//  Created by Luca on 14/07/22.
//

#import "Posizione.h"

@implementation Posizione

- (instancetype) initWithlatitudine : (double)latitudine
                        longitudine : (double)longitudine{
    if(self = [super init]){
        _latitudine = latitudine;
        _longitudine = longitudine;
    }
    return self;
}

/**
 Ritorna tutte le info dell oggetto sotto forma di stringa
 */
-(NSString*) toString {
    NSMutableString *tmp = [NSMutableString string];
    
    NSNumber *num = [NSNumber numberWithDouble:_latitudine];
    [tmp appendString:@" Lat: "];
    [tmp appendString:[num stringValue]];
    
    num = [NSNumber numberWithDouble:_longitudine];
    [tmp appendString:@" Lon: "];
    [tmp appendString:[num stringValue]];
    
    NSString *s = (NSString *)tmp;
    return s;
}


/**
 Ritorna la posizione dell oggetto sotto forma di stringa
 */
-(NSString*) getPos{
    NSMutableString *tmp = [NSMutableString string];
    
    NSNumber *num = [NSNumber numberWithDouble:_latitudine];
    [tmp appendString:[num stringValue]];
    [tmp appendString:@"#"];
    num = [NSNumber numberWithDouble:_longitudine];
    [tmp appendString:[num stringValue]];
    
    NSString *s = (NSString *)tmp;
    return s;
}
@end
