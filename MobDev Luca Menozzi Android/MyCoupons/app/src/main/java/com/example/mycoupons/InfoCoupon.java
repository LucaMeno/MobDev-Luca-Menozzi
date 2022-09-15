package com.example.mycoupons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;


public class InfoCoupon extends AppCompatActivity {

    private final String TAG = InfoCoupon.class.getSimpleName();
    private boolean edit = false;

    /**
     * Coupon del quale mostrare le informazioni
     */
    private Coupon c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_coupon);

        //Associazione azioni ai controlli
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab_modifica = findViewById(R.id.btn_modificaCoupon);
        fab_modifica.setOnClickListener(v -> modificaCoupon());

        FloatingActionButton fab_elimina = findViewById(R.id.btn_eliminaCoupon);
        fab_elimina.setOnClickListener(v -> eliminaCoupon());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String jsonC = extras.getString("infoCp");
            Log.d(TAG, jsonC);

            //Creo coupon da json e popolo i campi
            try {
                c = new Coupon(jsonC);
                popolaCampi();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Spostamento verso activity per mostrare le posizioni sulla mappa
     */
    private void mostraInfoMappa() {
        Intent intent = new Intent(InfoCoupon.this, GestionePosizioni.class);
        Bundle b = new Bundle();
        b.putString("mappaCp", c.toJSON());
        b.putString("editable", "false");
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * Azione per tasto eliminaCoupon
     */
    private void eliminaCoupon() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Conferma");
        builder.setMessage("Sicuro di voler eliminare il Coupon?");

        builder.setPositiveButton("Si", (dialog, which) -> {
            //Ritorno alla Activity precedente con il coupon
            Intent intent = new Intent();
            intent.putExtra("cpNuovo", c.toJSON());
            intent.putExtra("cpEliminato", "true");
            setResult(RESULT_OK, intent);
            finish();
            dialog.dismiss();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * Prende le informazioni dal Coupon e riempie tutti i campi dell activity
     */
    @SuppressLint("SetTextI18n")
    private void popolaCampi() {

        //Imposto il nome dell azienda
        TextView tv_nomeAzienda = findViewById(R.id.lbl_nomeAzienda);
        tv_nomeAzienda.setText(c.getNomeAzienda());

        //Imposto il codice
        TextView tv_codice = findViewById(R.id.lbl_codice);
        tv_codice.setText(c.getCodice());

        //Se impostata imposto la data di scadenza
        TextView tv_scadenza = findViewById(R.id.lbl_scadenza);

        if (c.getScadenza() == null) {
            tv_scadenza.setText("Nessuna scadenza");
        } else {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(c.getScadenza());
            tv_scadenza.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        }

        //Se il coupon dispone di posizioni preparo il tasto per mostrarle sulla mappa
        if (c.hasPosizioni()) {
            LinearLayout ll = findViewById(R.id.layout_btnMostraMappa);
            ll.setVisibility(View.VISIBLE);
            TextView tv_posizioni = findViewById(R.id.lbl_posizioni);
            tv_posizioni.setText("");
            ListView lv = findViewById(R.id.list_posizioni);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, c.getInfoPosizioni());
            lv.setAdapter(adapter);

            Button btn = findViewById(R.id.btn_mappa);
            btn.setOnClickListener(v -> mostraInfoMappa());

        }
        //Altrimenti imposto la textView
        else {
            LinearLayout ll = findViewById(R.id.layout_btnMostraMappa);
            ll.setVisibility(View.GONE);
            TextView tv_posizioni = findViewById(R.id.lbl_posizioni);
            tv_posizioni.setText("Nessuna posizione");
        }

        //Imposto l'immagine scansionabile del codice
        ImageView iv = findViewById(R.id.img_qr);
        try {
            if (c.getFormato() == 1)
                iv.setImageBitmap(creaQrBitmap(c.getCodice()));
            else
                iv.setImageBitmap(creaBarBitmap(c.getCodice()));
        } catch (Exception e) {
            Log.e(TAG, "popolaCampi img");
        }

    }

    /**
     * Ritorno alla Activity main con il coupon
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
        intent.putExtra("cpNuovo", c.toJSON());
        intent.putExtra("cpEliminato", "false");
        if (edit)
            intent.putExtra("cpModificato", "true");
        else
            intent.putExtra("cpModificato", "false");

        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    /**
     * Spostamento verso l activity di modifica
     */
    private void modificaCoupon() {
        Intent intent = new Intent(InfoCoupon.this, ModificaCoupon.class);
        Bundle b = new Bundle();
        b.putString("modificaCp", c.toJSON());
        intent.putExtras(b);
        activityModificaCp.launch(intent);
    }

    /**
     * Gestione interazione con activity modifica Cp
     */
    ActivityResultLauncher<Intent> activityModificaCp = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null) {
                        Log.e(TAG, "Data null");
                        return;
                    }
                    String modifica = data.getStringExtra("boolEdit");
                    if (modifica.equals("true")) {
                        String res = data.getStringExtra("infoCp");
                        c = new Coupon(res);
                        popolaCampi();
                        edit = true;
                    }
                } else {
                    Log.e(TAG, "result_KO modifica cp");
                }
            });

    /**
     * Creo l immagine QR CODE da viuslizzare
     *
     * @param str codice del coupon
     * @return immaginw QR code del coupon
     */
    private Bitmap creaQrBitmap(String str) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 400, 400);

        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pixels[y * w + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    /**
     * Creo l immagine BAR CODE da viuslizzare
     *
     * @param str codice del coupon
     * @return immaginw BAR code del coupon
     */
    private Bitmap creaBarBitmap(String str) throws Exception {
        MultiFormatWriter writer = new MultiFormatWriter();
        String finalData = Uri.encode(str);

        BitMatrix bm = writer.encode(finalData, BarcodeFormat.CODE_128, 400, 150);
        int bmWidth = bm.getWidth();

        Bitmap imageBitmap = Bitmap.createBitmap(bmWidth, 400, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < bmWidth; i++) {
            int[] column = new int[150];
            Arrays.fill(column, bm.get(i, 0) ? Color.BLACK : Color.WHITE);
            imageBitmap.setPixels(column, 0, 1, i, 0, 1, 150);
        }

        return imageBitmap;
    }

}


