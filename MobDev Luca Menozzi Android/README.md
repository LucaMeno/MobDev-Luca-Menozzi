Luca Menozzi 317730

Progetto Android: MyCoupon

FUNZIONAMENTO APPLICAZIONE IN BREVE:

l’applicazione parte mostrand tutti i coupon disponibili. I coupon verranno letti da SharedPreferences se precedentemente scritti oppure verranno mostrati quelli di default.
cliccando su un coupon si puo accedere ai suoi dettagli; dalla activity dei dettagli vi sarà la possibilita di modificarlo oppure eliminarlo.

dalla schermata principale è inoltre possibile aggiungere un nuovo Coupon, tale aggiunta potrà essere fatta o inserendo manualmente i dati dei coupon oppure utilzzando lo scanner di codici.
Lo scanner potrà funzionare in due modi:
Se la scansione rileva una stringa di X caratteri essa sarà interpretata com el codice del coupon
Se la scansione rileva una stringa di lunghezza diversa da X essa sarà interpretata come un coupon scritto in formato JSON e l’app tenterà di tradurlo

Mappe:
i coupon potranno anche essere visualizzati su una mappa, sia in modalita di semplice visualizzione sia in modalita di modifica dove sarà possibile aggiungere luoghi o eliminare i luoghi gia esistenti.
Per aggiungere un luogo basterà cliccare sulla mappa nel posto desiderato, per rimuoverlo bisognerà cliccare sul marker che si intende eliminare e cliccare sul pulsante elimina che apparirà in basso a destra.

All’avvio verranno inostre impostate le geofence su tutte le posizioni dei coupon e l app manderà un messaggio locale quando ci si avvicinerà ad uno di essi.

DETTAGLI TECNICI:
- Per poter usufruire di tutte le potenzialità dell’applicazione bisongerà garantirle i permessi sulla posizione e alle notifiche
- La lunghezza X del codice del coupon è stabilita all’interno della classe GlobalVar
- All interno del file Scan_Test sono presenti 3 codici da scansionare per testare l’app


