package com.example.mycoupons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class AggiungiCoupon extends AppCompatActivity {

    /**
     * Stringa per identificazione Log
     */
    private final String TAG = AggiungiCoupon.class.getSimpleName();

    private RadioButton rbQr;
    private RadioButton rbBar;
    private DatePicker scadenza;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch scadenzaOn;
    private EditText txtAzienda;
    private EditText txtCodice;
    private Coupon c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_coupon);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //
        // c = new Coupon();

        //Associo i controlli
        rbQr = findViewById(R.id.radio_qr);
        rbBar = findViewById(R.id.radio_bar);
        scadenza = findViewById(R.id.data_scadenza);
        scadenzaOn = findViewById(R.id.switch_scadenza);
        txtAzienda = findViewById(R.id.txt_nomeAzienda);
        txtCodice = findViewById(R.id.txt_codice);

        scadenzaOn.setChecked(true);
        rbQr.setChecked(true);

        //Disablilita la data in caso non sia richiesta
        scadenzaOn.setOnCheckedChangeListener((buttonView, isChecked) -> scadenza.setEnabled(isChecked));

        //Associazione azioni bottoni
        Button btn_inserisci = findViewById(R.id.btn_inserisci);
        btn_inserisci.setOnClickListener(v -> aggiungi());

        Button btn = findViewById(R.id.btn_aggiungiPosizioni);
        btn.setOnClickListener(v -> aggiugiPosizioni());

        FloatingActionButton myFab = findViewById(R.id.btn_addCouponFoto);
        myFab.setOnClickListener(v -> addCouponFoto());

    }

    /**
     * Spostaento verso Activity per selezione posizione
     */
    private void aggiugiPosizioni() {
        Intent intent = new Intent(AggiungiCoupon.this, GestionePosizioni.class);
        Bundle b = new Bundle();
        if (c == null) {
            c = new Coupon();
        }
        b.putString("mappaCp", c.toJSON());

        b.putString("editable", "true");
        intent.putExtras(b);
        activityAggiungiPos.launch(intent);
    }

    /**
     * Gestione activity per aggiunta posizioni Coupon
     */
    ActivityResultLauncher<Intent> activityAggiungiPos = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null)
                        return;
                    String res = data.getStringExtra("modificaCp");
                    c = new Coupon(res);
                }
            });


    /**
     * ritorno ad activity precedente
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Qunado si torna indietro ritorno il Coupon
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("ok", "false");
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    /**
     * btn aggiungi Coupon premuto
     */
    private void aggiungi() {

        //controllo se tutti i campi sono stati riempiti
        if (!controlloInserimento())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Conferma");
        builder.setMessage("Sicuro di voler aggiungere il Coupon?");

        builder.setPositiveButton("Si", (dialog, which) -> {
            aggiungiOk();
            dialog.dismiss();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();


    }

    /**
     * Se premuto ok su dialog di aggiunta aggiungo il Coupon
     */
    private void aggiungiOk() {
        if (c == null) {
            c = new Coupon();
        }
        //Imposto formato
        if (rbQr.isChecked())
            c.setFormato(1);
        else
            c.setFormato(0);

        //Imposto codice e nome azienda
        c.setCodice(txtCodice.getText().toString());
        c.setNomeAzienda(txtAzienda.getText().toString());

        //Se selezionata imposto la data di scadenza
        if (scadenzaOn.isChecked()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(scadenza.getYear(), scadenza.getMonth(), scadenza.getDayOfMonth());
            c.setScadenza(calendar.getTime());
        }

        //Ritorno alla Activity main con il coupon nuovo
        Intent intent = new Intent();
        intent.putExtra("cpAggiunto", c.toJSON());
        intent.putExtra("ok", "true");
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Funzione per il controllo dei dati inseriti
     *
     * @return true se il controllo e andato a buon fine, false altrimenti
     */
    private boolean controlloInserimento() {

        if (txtAzienda.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Inseire nome azienda", Toast.LENGTH_LONG).show();
            return false;
        }

        if (txtCodice.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Inseire codice", Toast.LENGTH_LONG).show();
            return false;
        }

        if (txtCodice.getText().toString().length() < GlobalVar.CODICE_LENGTH) {
            Toast.makeText(getApplicationContext(), "Codice troppo corto", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!rbQr.isChecked()) {
            if (!rbBar.isChecked()) {
                Toast.makeText(getApplicationContext(), "Inseire il tipo", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    /**
     * Spostamento verso l'activity per lo scanner dei coupon
     */
    private void addCouponFoto() {

        Intent scanner = new Intent(AggiungiCoupon.this, ScannerCoupon.class);
        activityScannerCp.launch(scanner);
    }

    /**
     * Ritorno da Activity per scanner Coupon
     */
    ActivityResultLauncher<Intent> activityScannerCp = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null)
                        return;
                    String scan = data.getStringExtra("scanTextOk");
                    if (scan.equals("true")) {
                        String res = data.getStringExtra("scanText");

                        //Solo codice
                        if (res.length() == GlobalVar.CODICE_LENGTH) {
                            this.txtCodice.setText(res);
                            return;
                        }

                        //Tutto il coupon
                        try {
                            c = new Coupon(res);
                            this.txtAzienda.setText(c.getNomeAzienda());
                            this.txtCodice.setText(c.getCodice());

                            if (c.getScadenza() == null) {
                                scadenzaOn.setChecked(false);
                            } else {
                                scadenzaOn.setChecked(true);
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(c.getScadenza());
                                this.scadenza.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                            }

                            if (c.getFormato() == 0) {
                                rbBar.setChecked(true);
                                rbQr.setChecked(false);
                            } else {
                                rbQr.setChecked(true);
                                rbBar.setChecked(false);
                            }

                            Log.e(TAG, "NC: " + c.toString());

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                            Toast.makeText(getApplicationContext(), "Formato coupon errato", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Log.e(TAG, "Return_KO scanner");
                }
            });

}