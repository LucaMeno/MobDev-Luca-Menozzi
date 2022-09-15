//
//  ViewControllerAggiungiProm.m
//  Memorandum
//
//  Created by Luca on 09/08/22.
//

/**
 ViewController per aggiunta di un promemoria
 */

#import "ViewControllerAggiungiProm.h"

@interface ViewControllerAggiungiProm ()

/**
 Elementi grafici
 */
@property (weak, nonatomic) IBOutlet UITextField *txtNewTitolo;
@property (weak, nonatomic) IBOutlet UITextView *txtNewDescrizione;
@property (weak, nonatomic) IBOutlet UIDatePicker *dataPickerNewData;
@property (weak, nonatomic) IBOutlet UISegmentedControl *SegContNewStato;

@end

@implementation ViewControllerAggiungiProm

- (void)viewDidLoad {
    [super viewDidLoad];
    _gp =[[GestionePromemoria alloc] init];
    _pos = [[Posizione alloc] initWithlatitudine:-1 longitudine:-1];
}

- (IBAction)btnAggiungiClick:(id)sender {
    
    //Controllo validita dei campi
    if(![self checkDati])
        return;
    
    //Possibili scelte per lo stato del promemoria
    NSArray *v = [NSArray arrayWithObjects: @"a", @"c", @"s", nil];
    
    //Creo il nuovo promemoria
    Promemoria *p = [[Promemoria alloc] initWithtitolo:_txtNewTitolo.text descrizione:_txtNewDescrizione.text dataOra:_dataPickerNewData.date statoStringa:[v objectAtIndex:[_SegContNewStato selectedSegmentIndex]] luogo:_pos];
    
    //Lo aggiungo alla lista
    [_gp addPromemoria:p];
    
    //MessageBox avviso aggiunta
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Info" message:@"Promemoria aggiunto" preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self.navigationController popToRootViewControllerAnimated:YES];
    }];
    
    [alert addAction:ok];
    [self presentViewController:alert animated:YES completion:nil];
    
    //Aggiorno le geofence dei promemoria
    NSNotification *myNotification = [NSNotification notificationWithName:@"Update"
                                                                   object:self
                                                                 userInfo:nil];
    
    [[NSNotificationCenter defaultCenter] postNotification:myNotification];
}

/**
 return true se tutti i campi sono stati impostati correttamente
 */
-(bool)checkDati{
    
    if([_txtNewTitolo.text isEqual:@""]){
        [self messageBox:@"Inserire titolo"];
        return false;
    }
    
    if(_pos.latitudine == -1){
        if(_pos.longitudine == -1){
            [self messageBox:@"Scegliere il luogo"];
            return false;
        }
    }
    
    if([_txtNewDescrizione.text isEqual:@""]){
        [self messageBox:@"Inserire descrizione"];
        return false;
    }
    
    return true;
}


/**
 Mostra una MesasgeBox contenente la stringa passata come parametro
 */
-(void)messageBox: (NSString*)str{
    
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Info" message:str preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) { }];
    [alert addAction:ok];
    [self presentViewController:alert animated:YES completion:nil];
    return;
}


#pragma mark - Navigation

/**
 Metodo per spostamento tra view
 */
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    //Per selezione luogo
    if([segue.identifier isEqualToString:@"mostraSelezioneLuogo"]){
        if([segue.destinationViewController isKindOfClass:[ViewControllerSelezioneLuogo class]]){
            ViewControllerSelezioneLuogo *selLuogo = (ViewControllerSelezioneLuogo*) segue.destinationViewController;
            //Passo il luovo alla view
            selLuogo.pos = _pos;
        }
    }
}


@end



