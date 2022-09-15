package com.example.mycoupons;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import java.util.Objects;

/**
 * Activity per scannerizzare un eventuale coupon da QRcode o BARcode
 * Fonte GitHub: https://github.com/yuriy-budiyev/code-scanner
 */
public class ScannerCoupon extends AppCompatActivity {

    private CodeScanner mCodeScanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_coupon);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            //Ritorno alla activity chiamante
            Intent intent = new Intent();
            intent.putExtra("scanText", result.getText());
            intent.putExtra("scanTextOk", "true");
            setResult(RESULT_OK, intent);
            finish();
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    /**
     * Ritorno ad activity precedente
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Ritorno ad activity precedente
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("scanTextOk", "false");
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}