package com.example.mycoupons;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Posizione {

    /**
     * Latitudine posizione
     */
    private final double latitudine;

    /**
     * Longitudine posizione
     */
    private final double longitudine;

    /**
     * Nome della posizione
     */
    private String nomePosizione;

    /**
     * Indirizzo della posizione
     */
    private final String indirizzo;

    public Posizione(double lat, double lon, String indirizzo, String nomePosizione) {
        this.latitudine = lat;
        this.longitudine = lon;
        this.nomePosizione = nomePosizione;
        this.indirizzo = indirizzo;
    }

    public Posizione(double lat, double lon, String indirizzo) {
        this.latitudine = lat;
        this.longitudine = lon;
        this.indirizzo = indirizzo;
        this.nomePosizione = "";
    }

    @SuppressWarnings("unused")
    public double getLatitudine() {
        return latitudine;
    }

    @SuppressWarnings("unused")
    public double getLongitudine() {
        return longitudine;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getNomePosizione() {
        return nomePosizione;
    }

    public void setNomePosizione(String nomePosizione) {
        this.nomePosizione = nomePosizione;
    }

    /**
     * Colona la posizione corrente
     *
     * @return posizione identica a se stessa
     */
    @NonNull
    public Posizione clonePos() {
        return new Posizione(latitudine, longitudine, indirizzo, nomePosizione);
    }


    /**
     * Trasforma la posizione in un Marker adatto alla mappa
     *
     * @return Marker per mappa
     */
    public MarkerOptions getPuntoMappa() {
        LatLng l = new LatLng(latitudine, longitudine);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(l);
        markerOptions.title(indirizzo);
        return markerOptions;
    }

    /**
     * to String dell oggetto
     *
     * @return Stringa contenente tutte le informazioni dell oggetto
     */
    @NonNull
    public String toString() {
        String str = "";
        str += "Latitudine: " + latitudine;
        str += " Longitudine: " + longitudine;
        str += " Nome: " + nomePosizione;
        str += " Indirizzo: " + indirizzo;
        return str;
    }
}
