//
//  gestionePromemoria.m
//  Memorandum
//
//  Created by Luca on 14/07/22.
//

/**
 Classe per la gestionde dei promemoria
 */

#import "GestionePromemoria.h"

@implementation GestionePromemoria

-(instancetype)init{
    if(prom == nil){
        prom = [NSMutableArray new];
        [self leggiFile];
        //[self popolaLista];
        [self scriviFile];
    }
    return self;
}

/**
 Aggiunge un promemoria alla lista
 */
-(void) addPromemoria:(Promemoria *)pr{
    [prom addObject:pr];
    [self scriviFile];
}

/**
 Rimuove un promemoria dalla lista
 */
-(void) remouvePromemoria:(Promemoria *)pr{
    [prom removeObject:pr];
    [self scriviFile];
}

/**
 Ritorna la lista di promemoria
 */
-(NSMutableArray*) getLista{
    return prom;
}

/**
 Ritorna la lista dei soli promemoria attivi
 */
-(NSArray*) getListaAttivi{
    NSMutableArray *v = [[NSMutableArray alloc] init];
    
    for(int i=0; i<prom.count; i++)
        if([[[prom objectAtIndex:i] stato] isAttivo])
            [v addObject: [prom objectAtIndex:i]];
    
    //Ordino i promemoria per la data
    return [v sortedArrayUsingSelector:@selector(compare:)];
}

/**
 Salva i dati dei promemoria per la persistenza
 */
-(void)scriviFile{
    NSLog(@"Scrivo dati\n");
    NSMutableArray *v = [[NSMutableArray alloc] init];
    
    for(int i=0; i<prom.count; i++){
        [v addObject: [[prom objectAtIndex:i] getDictionary]];
        /*for(NSString *key in [[[prom objectAtIndex:i] getDictionary] allKeys]) {
         NSLog(@"%@   ->   %@",key, [[[prom objectAtIndex:i] getDictionary] objectForKey:key]);
         }*/
    }
    
    NSArray *arrayProm = [v copy];
    
    //Converto in JSON
    NSError *writeError = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:arrayProm options:NSJSONWritingPrettyPrinted error:&writeError];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
    //Scrivo i dati
    [[NSUserDefaults standardUserDefaults]setObject:jsonString forKey:@"txtProm"];
    [[NSUserDefaults standardUserDefaults]synchronize];
}

/**
 Legge i dati salvati, se ci sono, per la persistenza
 Se non ci sono prom salvati popola la lista con quelli di default
 */
-(void) leggiFile{
    NSLog(@"Leggo dati\n");
    [prom removeAllObjects];
    
    //Leggo i dati
    NSString *jsonString = [[NSUserDefaults standardUserDefaults]stringForKey:@"txtProm"];
    
    //Se non sono stati trovati dati precedentemente salvati imposto quelli di default
    if(jsonString == nil) {
        [self popolaLista];
        return;
    }
    
    //Creo i promemoria dalle stringhe JSON
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    NSError *e = nil;
    NSArray *jsonArray = [NSJSONSerialization JSONObjectWithData: jsonData options: NSJSONReadingMutableContainers error: &e];
    
    if (!jsonArray) {
        NSLog(@"Error parsing JSON: %@", e);
    } else {
        for(NSDictionary *item in jsonArray) {
            Promemoria* p = [[Promemoria alloc] initWithJson:item];
            [prom addObject:p];
        }
    }
    
    /*for(Promemoria *p in prom){
     NSLog(@"STAMPO %@",p.toString);
     }*/
}


/**
 Inserimento manuale di valori di default
 */
-(void)popolaLista{
    NSLog(@"Popolo la lista con i valori di default");
    [prom removeAllObjects];
    
    NSString *str = @"7/12/2022 9:15 PM";
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"dd/MM/yyyy HH:mm a";
    NSDate *data = [formatter dateFromString:str];
    
    Posizione* pos1 =[[Posizione alloc] initWithlatitudine:44.8036 longitudine:10.33];
    Promemoria *p1 = [[Promemoria alloc] initWithtitolo:@"Cena" descrizione:@"Cena di famiglia" dataOra:data statoStringa:@"a" luogo:pos1];
    
    Posizione* pos2 =[[Posizione alloc] initWithlatitudine:44.6444 longitudine:10.3014];
    Promemoria *p2 = [[Promemoria alloc] initWithtitolo:@"Meeting" descrizione:@"Incontro di lavoro per decisioni importanti" dataOra:data statoStringa:@"c" luogo:pos2];
    
    Posizione* pos3 =[[Posizione alloc] initWithlatitudine:44.6136 longitudine:10.2666];
    Promemoria *p3 = [[Promemoria alloc] initWithtitolo:@"Computer" descrizione:@"Andare a riprendere il computer in riparazione" dataOra:data statoStringa:@"s" luogo:pos3];
    
    Posizione* pos4 =[[Posizione alloc] initWithlatitudine:45.6136 longitudine:11.2666];
    Promemoria *p4 = [[Promemoria alloc] initWithtitolo:@"Barbiere" descrizione:@"taglio di capelli" dataOra:data statoStringa:@"a" luogo:pos4];
    
    Posizione* pos5 =[[Posizione alloc] initWithlatitudine:44.7136 longitudine:10.3666];
    Promemoria *p5 = [[Promemoria alloc] initWithtitolo:@"Meccanico" descrizione:@"portare la macchina dal meccanico per il tgliando" dataOra:data statoStringa:@"a" luogo:pos5];
    
    [prom addObject:p1];
    [prom addObject:p2];
    [prom addObject:p3];
    [prom addObject:p4];
    [prom addObject:p5];
}

@end
