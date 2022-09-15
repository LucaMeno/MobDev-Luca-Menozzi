package com.example.mycoupons;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    /**
     * lista da Coupon da gestire
     */
    private static GestioneCoupon gCp = null;

    /**
     * Stringa per accedere ai dati per la persistenza
     */
    private final String sharedPrefKey = "CouponData";

    /**
     * Tabella per visualizzare i Coupon
     */
    private TableLayout tl;

    /**
     * Stringa per identificazione Log
     */
    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * Client per geofeve
     */
    private GeofencingClient geofencingClient;

    /**
     * Lista di geofence da monitorare
     */
    private ArrayList<Geofence> geofenceList;

    /**
     * Intent da associare alle geofence
     */
    private PendingIntent geofencePendingIntent;

    /**
     * Mappa nascosta per permettere funzionameto geofence
     */
    private MappaCoupons mappaCp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Controllo se i permessi necessari sono abilitati
        if (controlloPermessi()) {

            //all apertura della Activity leggo i dati per la persistenza
            if (gCp == null) {
                leggiDati();
            }

            tl = findViewById(R.id.tabellaCoupons);

            //Dopo la creazione della tabella la popolo
            tl.post(this::popolaTabella);

            //Associo il pulsante
            FloatingActionButton fab = findViewById(R.id.btn_addCoupon);
            fab.setOnClickListener(v -> addCoupon());

            //Setto le geofence
            geofencingClient = LocationServices.getGeofencingClient(this);
            geofenceList = new ArrayList<>();
            popolaGeoFence();

            //preparo la mappa
            setUpMap();
        }
    }


    /**
     * Controllo se tutti i permessi necessari sono abilitati
     * @return
     */
    private boolean controlloPermessi() {
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        permissions.add(Manifest.permission.CAMERA);

        for (int i = 0; i < permissions.size(); i++) {
            if (ContextCompat.checkSelfPermission(this, permissions.get(i)) == PermissionChecker.PERMISSION_DENIED) {
                Log.e(TAG, "Permesso " + permissions.get(i) + " non abilitato");
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Errore");
                alert.setMessage("Permesso " + permissions.get(i) + " non abilitato. Abilitarlo per utilizzare l applicazione");
                alert.setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });
                alert.show();
                return false;
            }
        }
        return true;
    }


    /**
     * Metodo per create un fragment nascosto con una mappa per permettere alle notifiche geofence di funzionare
     */
    private void setUpMap() {
        Log.d(TAG, "apro mappa");
        //Inizilaizzo fragment
        mappaCp = new MappaCoupons(null, false);
        //apro fragment
        getSupportFragmentManager().beginTransaction().add(R.id.frame_mappa_main, mappaCp).commit();
    }

    /**
     * prende tutte le posizioni di tutti i Coupon e imposta le relative geofence
     */
    private void popolaGeoFence() {

        //Popolo la lista delle geofence
        geofenceList.clear();
        for (Coupon c : gCp.getLista()) {
            if (c.hasPosizioni()) {
                for (Posizione p : c.getPosizioni()) {
                    Log.d(TAG, p.getNomePosizione() + "&&&" + p.getIndirizzo());
                    geofenceList.add(new Geofence.Builder()
                            // id della geofence
                            .setRequestId(p.getNomePosizione() + "&&&" + p.getIndirizzo())
                            .setCircularRegion(
                                    p.getLatitudine(),
                                    p.getLongitudine(),
                                    500
                            )
                            .setExpirationDuration(43200000)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                            .build());
                }
            }
        }

        //Controllo i permessi
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Controlla i permessi");
            finish();
            return;
        }

        //Aggiungo le geofence
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, aVoid -> Log.d(TAG, "Geofence aggiunte"))
                .addOnFailureListener(this, e -> Log.e(TAG, "Errore aggiunta geofence: " + e));

    }

    /**
     * Specifica l'elenco di geofence da monitorare e come devono essere segnalate le notifiche di geofence.
     */
    @NonNull
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    /**
     * Ritorna il PendingIntent da associare alla geofence
     *
     * @return intent per geofence
     */
    private PendingIntent getGeofencePendingIntent() {
        // se gia presente ritorno l intent
        if (geofencePendingIntent != null)
            return geofencePendingIntent;
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        //geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    /**
     * prima di chiudere scrivo i dati per la persistenza
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        chiudiGeofence();
        super.onDestroy();
    }

    /**
     * Chiudo tutte le geofence
     */
    private void chiudiGeofence() {
        if (geofenceList == null)
            return;
        Log.d(TAG, "Chiudo geofence");
        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(this, aVoid -> Log.d(TAG, "Geofence chiuse"))
                .addOnFailureListener(this, e -> Log.e(TAG, "Errore chiusura geofence"));
    }

    /**
     * creo le righe della tabella e le inserisco
     */
    @SuppressLint("SetTextI18n")
    private void popolaTabella() {
        tl.removeAllViews();
        for (Coupon c : gCp.getLista()) {

            //Creo riga
            final TableRow tableRow = new TableRow(MainActivity.this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

            int leftMargin = ((tl.getWidth() / 100) * 7);
            int topMargin = 15;
            int rightMargin = 0;
            int bottomMargin = 15;

            tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
            tableRow.setLayoutParams(tableRowParams);

            //Creo TextView con nome azienda
            final TextView text = new TextView(MainActivity.this);
            text.setText(c.getNomeAzienda());
            text.setLayoutParams(new TableRow.LayoutParams(((tl.getWidth() / 100) * 74), TableRow.LayoutParams.WRAP_CONTENT));
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            //Creo i btn
            final Button button = new Button(MainActivity.this);

            button.setText("info");
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(50);
            shape.setColor(Color.parseColor("#6200EE"));

            button.setBackground(shape);
            button.setTextColor(Color.WHITE);
            button.setLayoutParams(new TableRow.LayoutParams(((tl.getWidth() / 100) * 19), TableRow.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(v -> infoCoupon(c));

            tableRow.addView(text);
            tableRow.addView(button);

            //Aggiungo la riga alla tabella
            tl.addView(tableRow);
        }
    }

    /**
     * Spostamento verso Activity per mostrare info del coupon
     *
     * @param c coupon da visualizare
     */
    private void infoCoupon(@NonNull Coupon c) {
        Intent intent = new Intent(MainActivity.this, InfoCoupon.class);
        Bundle b = new Bundle();
        b.putString("infoCp", c.toJSON());
        intent.putExtras(b);
        //startActivityForResult(intent, 2);
        activityInfoCp.launch(intent);
    }

    /**
     * Gestione dell activity info Cp
     */
    ActivityResultLauncher<Intent> activityInfoCp = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null) {
                        Log.e(TAG, "data null");
                        return;
                    }

                    String res = data.getStringExtra("cpNuovo");
                    String eliminato = data.getStringExtra("cpEliminato");
                    String modificato = data.getStringExtra("cpModificato");

                    //gc.modificaCoupon(new Coupon(res));
                    //Se viene eliminato un Cp
                    if (eliminato.equals("true")) {
                        Coupon c_eliminato = new Coupon(res);
                        gCp.remouveCouponId(new Coupon(res));
                        updateActivity("Coupon " + c_eliminato.getNomeAzienda() + " eliminato");
                    } else if (modificato.equals("true")) {
                        //Se viene modificato un Cp
                        Coupon c_modificato = new Coupon(res);
                        gCp.modificaCoupon(c_modificato);
                        updateActivity("Coupon " + c_modificato.getNomeAzienda() + " modificato");
                    }
                } else {
                    Log.e(TAG, "RESULT_KO infoCp");
                }
            });

    /**
     * Azione sul click del FloatingActionButton
     */
    private void addCoupon() {
        Intent intent = new Intent(MainActivity.this, AggiungiCoupon.class);
        activityAggiungiCp.launch(intent);
    }

    ActivityResultLauncher<Intent> activityAggiungiCp = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data == null) {
                        Log.e(TAG, "data null");
                        return;
                    }

                    try {
                        String aggiunto = data.getStringExtra("ok");
                        if (aggiunto.equals("true")) {
                            String res = data.getStringExtra("cpAggiunto");
                            Coupon c = new Coupon(res, true);
                            gCp.addCoupon(c);
                            updateActivity("Coupon " + c.getNomeAzienda() + " aggiunto");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "errore creazione Coupon");
                    }

                } else {
                    Log.e(TAG, "RESULT_KO aggiungiCp");
                }
            });


    /**
     * Aggiorna le informazioni
     *
     * @param str stringa da mostrare da snackbar
     */
    private void updateActivity(String str) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), str, Snackbar.LENGTH_LONG);
        snackbar.show();
        popolaTabella();
        chiudiGeofence();
        popolaGeoFence();
        scriviDati();
    }

    /**
     * Scrivo i dati per la persistenza
     */
    private void scriviDati() {
        Log.d(TAG, "Scrivo dati");
        try {
            SharedPreferences sharedPref = getSharedPreferences(sharedPrefKey, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(sharedPrefKey, gCp.getJson());
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Errore scrivi dati: " + e);
        }
    }

    /**
     * Legge i dati per la persistenza
     */
    private void leggiDati() {
        try {
            SharedPreferences sharedPref = getSharedPreferences(sharedPrefKey, MODE_PRIVATE);
            String json = sharedPref.getString(sharedPrefKey, "nessuno valore definito");
            Log.d(TAG, "LETTO: " + json);
            if (json.equals("nessuno valore definito")) {
                Log.w(TAG, "JSON vuoto");
                gCp = new GestioneCoupon();
                gCp.popolaLista();
            } else {
                gCp = new GestioneCoupon(json);
            }

        } catch (Exception e) {
            Log.e(TAG, "Errore leggi dati: " + e);
            gCp = new GestioneCoupon();
            gCp.popolaLista();
        }
    }
}