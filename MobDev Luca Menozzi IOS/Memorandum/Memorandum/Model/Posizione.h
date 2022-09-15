//
//  Posizione.h
//  Memorandum
//
//  Created by Luca on 14/07/22.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface Posizione : NSObject

- (instancetype) initWithlatitudine : (double)latitudine
                        longitudine : (double)longitudine;

@property (nonatomic) double latitudine;
@property (nonatomic) double longitudine;

/**
 Ritorna tutte le info dell oggetto sotto forma di stringa
 */
-(NSString*) toString;

/**
 Ritorna la posizione dell oggetto sotto forma di stringa
 */
-(NSString*) getPos;


@end

NS_ASSUME_NONNULL_END
