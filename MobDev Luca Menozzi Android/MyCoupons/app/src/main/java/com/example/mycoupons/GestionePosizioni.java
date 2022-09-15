package com.example.mycoupons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.util.Objects;

public class GestionePosizioni extends AppCompatActivity {

    /**
     * Stringa per identificazione Log
     */
    private final String TAG = GestionePosizioni.class.getSimpleName();

    /**
     * Coupon da gestire
     */
    private Coupon c;

    /**
     * se true le posizioni sono modificabili, se false no
     */
    private boolean editable;

    /**
     * mappa
     */
    private MappaCoupons mappaCp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_posizioni);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String jsonC = extras.getString("mappaCp");
            String op = extras.getString("editable");
            editable = op.equals("true");

            Log.d(TAG, jsonC);

            //Creo coupon da json
            try {
                c = new Coupon(jsonC);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        //Inizilaizzo fragment
        mappaCp = new MappaCoupons(c.getAllPuntiMappa(), editable);

        //apro fragment
        getSupportFragmentManager().beginTransaction().add(R.id.frame_mappa, mappaCp).commit();
    }

    /**
     * Qunado si torna indietro ritorno il Coupon con le posizioni modificate
     */
    @Override
    public void onBackPressed() {
        c.setPosizioni(mappaCp.getPosizioni());
        Intent intent = new Intent();
        intent.putExtra("modificaCp", c.toJSON());
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    /**
     * ritorno ad activity precedente
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}