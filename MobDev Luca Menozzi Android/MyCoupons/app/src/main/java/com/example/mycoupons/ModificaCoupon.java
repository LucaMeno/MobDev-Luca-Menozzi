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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class ModificaCoupon extends AppCompatActivity {

    /**
     * Stringa per identificazione Log
     */
    private final String TAG = ModificaCoupon.class.getSimpleName();

    /**
     * radio button per QR CODE
     */
    private RadioButton rbQr;

    /**
     * radio button per BAR CODE
     */
    private RadioButton rbBar;

    /**
     * Data scadenza
     */
    private DatePicker scadenza;

    /**
     * Switch per stabilire se impostare la scadenza o meno
     */
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch scadenzaOn;

    /**
     * nome azienda
     */
    private EditText txtAzienda;

    /**
     * Codice coupon
     */
    private EditText txtCodice;

    /**
     * Coupon da modificare
     */
    private Coupon c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_coupon);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Associo i controlli
        rbQr = findViewById(R.id.radio_qr_modifica);
        rbBar = findViewById(R.id.radio_bar_modifica);
        scadenza = findViewById(R.id.data_scadenza_modifica);
        scadenzaOn = findViewById(R.id.switch_scadenza_modifica);
        txtAzienda = findViewById(R.id.txt_nomeAzienda_modifica);
        txtCodice = findViewById(R.id.txt_codice_modifica);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String jsonC = extras.getString("modificaCp");
            Log.d(TAG, jsonC);
            c = new Coupon(jsonC);
            popolaCampi();
        }

        //Associo le azioni
        Button btn_modifica = findViewById(R.id.btn_modificaCoupon);
        btn_modifica.setOnClickListener(v -> modifica());

        Button btn = findViewById(R.id.btn_modificaPosizioni);
        btn.setOnClickListener(v -> modificaPosizioni());

        //Disablilita la data in caso non sia richiesta
        scadenzaOn.setOnCheckedChangeListener((buttonView, isChecked) -> scadenza.setEnabled(isChecked));
    }

    /**
     * Popola tutti i campi della view con i dati del coupon in esame
     */
    private void popolaCampi() {

        try {
            this.txtAzienda.setText(c.getNomeAzienda());
            this.txtCodice.setText(c.getCodice());

            if (c.getScadenza() == null) {
                scadenzaOn.setChecked(false);
                scadenza.setEnabled(false);
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

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * azione del btn modifica posizioni, mostra activity per gestirle
     */
    private void modificaPosizioni() {
        Intent intent = new Intent(ModificaCoupon.this, GestionePosizioni.class);
        Bundle b = new Bundle();
        b.putString("mappaCp", c.toJSON());
        b.putString("editable", "true");
        intent.putExtras(b);
        //startActivityForResult(intent, 1);
        activityModificaPos.launch(intent);
    }

    /**
     * Gestione interazione con activity modifica poszione
     */
    ActivityResultLauncher<Intent> activityModificaPos = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null) {
                        Log.e(TAG, "data null");
                        return;
                    }
                    String res = data.getStringExtra("modificaCp");
                    c = new Coupon(res);
                } else {
                    Log.w(TAG, "RESULT_KO modifica pos");
                }
            });

    /**
     * Ritorno alla Activity precedente con il coupon vecchio
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
        intent.putExtra("infoCp", c.toJSON());
        intent.putExtra("boolEdit", "false");
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    /**
     * Azione pulsante modifica
     * Ritorno alla Activity precedente con il coupon modificato
     */
    private void modifica() {
        //controllo se tutti i campi sono stati riempiti
        if (!controlloInserimento())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Conferma");
        builder.setMessage("Sicuro di voler modificare il Coupon?");

        builder.setPositiveButton("Si", (dialog, which) -> {
            modificaOk();
            dialog.dismiss();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Effettiva modifica del cp, avviene dopo avere dato OK al dialog
     */
    private void modificaOk() {
        c.setNomeAzienda(txtAzienda.getText().toString());
        c.setCodice(txtCodice.getText().toString());

        if (rbQr.isChecked())
            c.setFormato(1);
        else
            c.setFormato(0);

        if (scadenzaOn.isChecked()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(scadenza.getYear(), scadenza.getMonth(), scadenza.getDayOfMonth());
            c.setScadenza(calendar.getTime());
        } else {
            c.setScadenza(null);
        }

        //Ritorno alla Activity precedente con il coupon nuovo
        Intent intent = new Intent();
        intent.putExtra("infoCp", c.toJSON());
        intent.putExtra("boolEdit", "true");
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Controllo validita dati inseriti
     *
     * @return true se i dati sono inseriti correttamente, false altrimenti
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
}