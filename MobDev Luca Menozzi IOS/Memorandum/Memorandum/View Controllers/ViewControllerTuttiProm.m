//
//  ViewControllerTuttiProm.m
//  Memorandum
//
//  Created by Luca on 15/07/22.
//

/**
 View per mostrare tutti i promemoria presenti
 */

#import "ViewControllerTuttiProm.h"

@interface ViewControllerTuttiProm ()

@end

@implementation ViewControllerTuttiProm

- (void)viewDidLoad {
    [super viewDidLoad];
    _gp = [GestionePromemoria alloc];
    
    //Imposto la tabella
    self.tableViewPromemoria.dataSource = self;
    self.tableViewPromemoria.delegate = self;
}

/**
 Numero riche tabella
 */
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [[_gp getLista] count];
}

/**
 Elementi tabella
 */
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    cell.textLabel.text = [[[_gp getLista] objectAtIndex:indexPath.row] titolo];
    return cell;
}

/**
 Metodo per spostamento tra view
 */
- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    
    //Mostra dettagli promemoria
    if([segue.identifier isEqualToString:@"mostraInfoProm"]){
        
        if([segue.destinationViewController isKindOfClass:[ViewControllerInfoProm class]]){
            ViewControllerInfoProm *infoProm = (ViewControllerInfoProm*) segue.destinationViewController;
            
            //Passo il promemoria da mostrare
            NSIndexPath *indexPath = [self.tableViewPromemoria indexPathForCell:sender];
            Promemoria *prom = [[_gp getLista] objectAtIndex:indexPath.row];
            infoProm.prom = prom;
            infoProm.gp = _gp;
        }
        
    }
}

/**
 Metodo chiamato quando viene mostrata la view
 */
-(void) viewDidAppear:(BOOL)animated{
    //Rileggo i promemoria
    [_tableViewPromemoria reloadData];
    
    //Se non ce ne sono mostro MessageBox
    if([[_gp getLista] count] == 0)
    {
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Info" message:@"Nessun promemoria presente" preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [self.navigationController popToRootViewControllerAnimated:YES];
        }];
        
        [alert addAction:ok];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
}

@end
