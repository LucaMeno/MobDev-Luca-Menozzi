//
//  ViewControllerInfoProm.m
//  Memorandum
//
//  Created by Luca on 15/07/22.
//

/*
 ViewController per mostrare i dettagli di un promemoria
 */

#import "ViewControllerInfoProm.h"

@interface ViewControllerInfoProm ()

//Elementi grafici per rappresentare i dati
@property (weak, nonatomic) IBOutlet UILabel *lblTitolo;
@property (weak, nonatomic) IBOutlet UILabel *lblData;
@property (weak, nonatomic) IBOutlet UILabel *lblOra;
@property (weak, nonatomic) IBOutlet UILabel *lblStato;
@property (weak, nonatomic) IBOutlet UITextView *txtDescrizione;


@end

@implementation ViewControllerInfoProm

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //Setto i dati nella view
    [self setDati];
}

/**
 Metodo chiamato sul click del pulsante elimina
 */
- (IBAction)btnEliminaClick:(id)sender {
    //Richiedo conferma eliminazione
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Attenzione" message:@"Eliminare il promemoria?" preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self->_gp remouvePromemoria:self.prom];
        [self.navigationController popToRootViewControllerAnimated:YES];
    }];
    
    //Confermo eliminazione avvenuta
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:nil];
    [alert addAction:cancel];
    [alert addAction:ok];
    [self presentViewController:alert animated:YES completion:nil];
}

-(void) viewDidAppear:(BOOL)animated{
    //Popola elementi grafici con valori del promemoria
    [self setDati];
}

/**
 Popolo tutti i campi grafici
 */
-(void) setDati {
    //Imposto titolo
    self.lblTitolo.text= self.prom.titolo;
    
    
    //Imposto data
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"dd-MM-yyyy"];
    NSString *data = [dateFormat stringFromDate:self.prom.dataOra];
    self.lblData.text= data;
    
    //Imposto ora
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"HH:mm"];
    NSString *ora = [formatter stringFromDate:self.prom.dataOra];
    self.lblOra.text= ora;
    
    //Imposto descrizione
    self.txtDescrizione.text= self.prom.descrizione;
    
    //Imposto stato
    self.lblStato.text = self.prom.stato.toString;
}


#pragma mark - Navigation

/**
 Metodo per spostamento tra view
 */
- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    
    //Modifica il promemoria
    if([segue.identifier isEqualToString:@"mostraModificaProm"]){
        
        if([segue.destinationViewController isKindOfClass:[ViewControllerModificaProm class]]){
            ViewControllerModificaProm *modificaProm = (ViewControllerModificaProm*) segue.destinationViewController;
            //Passo il promemoria da modificare
            modificaProm.prom = _prom;
            modificaProm.gp = _gp;
        }
    }
    
    //Mostra il promemoria sulla mappa
    if([segue.identifier isEqualToString:@"mostraPromInfoMappa"]){
        
        if([segue.destinationViewController isKindOfClass:[ViewControllerMappaProm class]]){
            ViewControllerMappaProm *mappaProm = (ViewControllerMappaProm*) segue.destinationViewController;
            //Passo il promemoria da visualizzare
            mappaProm.prom = _prom;
        }
    }
}



@end
