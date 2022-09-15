package com.example.mycoupons;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
Formato:
0 -> barCode
1 -> QR code
 */

public class Coupon {

    /**
     * Stringa per identificazione Log
     */
    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * id univoco per Coupon in autoincrement
     */
    private static final AtomicInteger id_count = new AtomicInteger(0);

    /**
     * id univoco per Coupo
     */
    private int id;

    /**
     * Nome azienda fornitrice del coupon
     */
    private String nomeAzienda = null;

    /**
     * Codice coupon
     */
    private String codice = null;

    /**
     * Formato:
     * 0 -> barCode
     * 1 -> QR code
     */
    private int formato = -1;

    /**
     * Scadenza del Coupon (opzionale)
     */
    private Date scadenza = null;

    /**
     * Posizioni in cui i coupon sono utilizzabili (opzionali)
     */
    private List<Posizione> posizioni = null;

    public Coupon() {
        this.id = id_count.incrementAndGet();
        this.posizioni = new ArrayList<>();
    }

    /**
     * Costruttore privato con possiblita di copiare impostare l id
     */
    private Coupon(int id, String nomeAzienda, String codice, int formato, Date scadenza, List<Posizione> posizioni) {
        this.id = id;
        this.codice = codice;
        this.formato = formato;
        this.nomeAzienda = nomeAzienda;
        this.scadenza = scadenza;
        this.posizioni = new ArrayList<>();
        setPosizioni(posizioni);
    }

    public Coupon(String nomeAzienda, String codice, int formato, Date scadenza, List<Posizione> posizioni) {
        this.codice = codice;
        this.formato = formato;
        this.nomeAzienda = nomeAzienda;
        this.scadenza = scadenza;
        this.id = id_count.incrementAndGet();
        this.posizioni = new ArrayList<>();
        setPosizioni(posizioni);
    }

    public Coupon(String nomeAzienda, String codice, int formato, List<Posizione> posizioni) {
        this.codice = codice;
        this.formato = formato;
        this.nomeAzienda = nomeAzienda;
        this.scadenza = null;
        this.id = id_count.incrementAndGet();
        this.posizioni = new ArrayList<>();
        setPosizioni(posizioni);
    }

    public Coupon(String nomeAzienda, String codice, int formato, Date scadenza) {
        this.codice = codice;
        this.formato = formato;
        this.nomeAzienda = nomeAzienda;
        this.scadenza = scadenza;
        this.id = id_count.incrementAndGet();
        posizioni = new ArrayList<>();
    }


    public Coupon(String nomeAzienda, String codice, int formato) {
        this.codice = codice;
        this.formato = formato;
        this.nomeAzienda = nomeAzienda;
        this.scadenza = null;
        this.id = id_count.incrementAndGet();
        this.posizioni = new ArrayList<>();
    }

    /**
     * Costruttore da string JSON
     *
     * @param json JSON contenente oggetto Coupon
     */
    public Coupon(String json) {
        try {
            Coupon c = new Gson().fromJson(json, Coupon.class);
            this.codice = c.getCodice();
            this.nomeAzienda = c.getNomeAzienda();
            this.scadenza = c.getScadenza();
            this.formato = c.getFormato();
            this.posizioni = new ArrayList<>();
            setPosizioni(c.getPosizioni());
            this.id = c.getId();

        } catch (Exception e) {
            Log.e(TAG, "Costruttore json " + e);
        }
    }

    /**
     * Costruttore da string JSON, reimpostando l'id
     *
     * @param json    JSON contenente oggetto Coupon
     * @param resetId parametro per reset ID
     */
    public Coupon(String json, boolean resetId) {
        try {
            Coupon c = new Gson().fromJson(json, Coupon.class);
            this.codice = c.getCodice();
            this.nomeAzienda = c.getNomeAzienda();
            this.scadenza = c.getScadenza();
            this.formato = c.getFormato();
            this.posizioni = new ArrayList<>();
            setPosizioni(c.getPosizioni());
            if (resetId)
                this.id = id_count.incrementAndGet();
            else
                this.id = c.getId();
        } catch (Exception e) {
            Log.e(TAG, "Costruttore json " + e);
        }
    }

    /**
     * toString dell'oggetto
     *
     * @return stringa contentente le info dell oggetto
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID: ").append(id);
        str.append(" nomeAzienda: ").append(nomeAzienda);
        str.append(" codice: ").append(codice);

        str.append((formato == 1) ? " formato: QR code" : " formato: BAR code");
        str.append((scadenza == null) ? " Nessuna scadenza" : " Scadenza: " + scadenza);

        if (posizioni != null)
            for (Posizione p : posizioni)
                str.append(p.toString()).append("  ");

        return str.toString();
    }

    /**
     * @return true se il coupon ha delle posizioni
     */
    public boolean hasPosizioni() {
        return !posizioni.isEmpty();
    }

    public String getNomeAzienda() {
        return nomeAzienda;
    }

    public void setNomeAzienda(String nomeAzienda) {
        this.nomeAzienda = nomeAzienda;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public int getFormato() {
        return formato;
    }

    public void setFormato(int formato) {
        this.formato = formato;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public List<Posizione> getPosizioni() {
        return posizioni;
    }

    /**
     * Imposta le posozioni eseguendo delle copie di quelle passate
     *
     * @param pos posizioni da settare
     */
    public void setPosizioni(List<Posizione> pos) {
        this.posizioni.clear();
        for (Posizione p : pos) {
            p.setNomePosizione(nomeAzienda);
            posizioni.add(p.clonePos());
        }

    }

    public int getId() {
        return id;
    }

    /**
     * Clona oggetto
     *
     * @return Coupon identico a se stesso
     */
    @NonNull
    @SuppressWarnings("unused")
    public Coupon cloneCp() {
        return new Coupon(id, nomeAzienda, codice, formato, scadenza, posizioni);
    }

    /**
     * @return oggetto convertito in formato JSON
     */
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Converte tutte le posizioni in punti per essere visualizzati sulla mappa
     *
     * @return punti per mappa
     */
    public ArrayList<MarkerOptions> getAllPuntiMappa() {
        ArrayList<MarkerOptions> array = new ArrayList<>();
        for (Posizione p : posizioni) {
            array.add(p.getPuntoMappa());
        }
        return array;
    }

    /**
     * @return tutti i nomi delle posizioni associate al Coupon
     */
    public ArrayList<String> getInfoPosizioni() {
        ArrayList<String> str = new ArrayList<>();
        for (Posizione p : posizioni) {
            str.add(p.getNomePosizione() + " " + p.getIndirizzo());
        }
        return str;
    }

}
