package com.example.mycoupons;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    /**
     * Stringa per identificazione Log
     */
    private final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();

    /**
     * Variabile in auto increment per id notifiche
     */
    private static final AtomicInteger id_count = new AtomicInteger(0);

    /**
     * Contesto su cui lavorare
     */
    private Context context;

    /**
     * Id del canale di notifica utilizzato
     */
    private final String CHANNEL_ID = "MyNotifica";

    /**
     * metodo utilizzato per stabilire quando vengono triggerate le geofence
     *
     * @param context contesto su cui lavorare
     * @param intent  informazioi geofence
     */
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Ricevuto messaggio");

        this.context = context;
        if (intent == null) {
            Log.e(TAG, "Intent null");
            return;
        }

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent == null)
            return;
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        //Per le notifiche
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notManager = context.getSystemService(NotificationManager.class);
            notManager.createNotificationChannel(channel);
        }

        //Prendo il tipo di transizione
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Controllo se sto entrando
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Controllo quante geofence si sono triggherate
            GeofencingEvent event = GeofencingEvent.fromIntent(intent);

            if (event == null || event.getTriggeringGeofences() == null) {
                Log.e(TAG, "event null");
                return;
            }

            // Prendo le info delle geofence
            for (Geofence geofence : event.getTriggeringGeofences()) {
                String[] parts = geofence.getRequestId().split("&&&");
                notifica(parts[0] + " nelle vicinanze", parts[0] + ", " + parts[1] + " in cui sfruttare un Coupon!");
            }

        } else {
            Log.w(TAG, "non e una transizione di ingresso");
        }
    }


    /**
     * Genera una notifica locale con i parametri passati
     *
     * @param titolo titolo della notifica
     * @param msg    corpo della notifica
     */
    private void notifica(String titolo, String msg) {
        //Id del canale di notifica utilizzato
        final String CHANNEL_ID = "MyNotifica";

        //Creo la notifica
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titolo)
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        //Lancio la notifica
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id_count.incrementAndGet(), builder.build());
    }
}