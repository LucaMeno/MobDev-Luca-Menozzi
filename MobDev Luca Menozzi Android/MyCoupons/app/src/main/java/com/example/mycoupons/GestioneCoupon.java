package com.example.mycoupons;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GestioneCoupon {

    /**
     * Lista di coupon da gestire
     */
    private static ArrayList<Coupon> gestCoupon = null;

    public GestioneCoupon() {
        if (gestCoupon == null)
            gestCoupon = new ArrayList<>();
    }

    /**
     * Costruttore da stringa JSON
     *
     * @param json lista di coupon in formato JSON
     */
    public GestioneCoupon(String json) {
        if (gestCoupon == null) {
            gestCoupon = new ArrayList<>();

            // \n: carattere utilizzato come separatore
            String[] array = json.split("\n");

            for (String s : array)
                gestCoupon.add(new Coupon(s, true));
        }
    }

    /**
     * Rimpiazza tutti i parametri del vecchio coupon con quelli del nuovo
     *
     * @param new_c nuovo coupon
     */
    public void modificaCoupon(Coupon new_c) {
        Coupon c = getCouponId(new_c.getId());

        if (c == null) {
            Log.e("GestioneCoupon", "Id non trovato");
            return;
        }

        c.setCodice(new_c.getCodice());
        c.setFormato(new_c.getFormato());
        c.setPosizioni(new_c.getPosizioni());
        c.setScadenza(new_c.getScadenza());
        c.setNomeAzienda(new_c.getNomeAzienda());
    }

    /**
     * Trova, se esiste, Coupon con id cercato
     *
     * @param id id del coupon da cercare
     * @return Coupon cercato, null altrimenti
     */
    private Coupon getCouponId(int id) {
        for (Coupon c : gestCoupon)
            if (c.getId() == id)
                return c;
        return null;
    }

    /**
     * Ritorna l'intero oggetto convertito in stringa JSON
     *
     * @return stringa JSON
     */
    public String getJson() {
        StringBuilder json = new StringBuilder();

        for (Coupon c : gestCoupon) {
            json.append(c.toJSON());
            json.append('\n');
        }

        if (json.length() > 0)
            json = new StringBuilder(json.substring(0, json.length() - 1));

        return json.toString();
    }

    public ArrayList<Coupon> getLista() {
        return gestCoupon;
    }

    /**
     * Aggiunge un Coupon alla lista solo se non è già presente
     *
     * @param c Coupon da aggiungere
     */
    public void addCoupon(Coupon c) {
        if (!gestCoupon.contains(c))
            gestCoupon.add(c);
    }

    /**
     * Rimuove il coupon passato
     *
     * @param c coupon da rimuovere
     */
    @SuppressWarnings("unused")
    public void remouveCoupon(Coupon c) {
        if (!gestCoupon.contains(c))
            gestCoupon.remove(c);
    }

    @SuppressWarnings("unused")
    public Coupon getCouponByIndex(int i) {
        return gestCoupon.get(i);
    }

    /**
     * Rimuove il coupon con id corrispondente a quello passato
     *
     * @param c coupon da cui estrapolare l'id
     */
    public void remouveCouponId(Coupon c) {
        Coupon c_eliminato = getCouponId(c.getId());

        if (c_eliminato == null)
            Log.e("GestioneCoupon", "Id non trovato");
        else
            gestCoupon.remove(c_eliminato);
    }

    /**
     * Svuota la lista dei coupon
     */
    @SuppressWarnings("unused")
    public void clear() {
        gestCoupon.clear();
    }

    /**
     * ritorna tutte le informazioni di tutti i coupon presenti
     *
     * @return stringa info coupon
     */
    @SuppressWarnings("unused")
    public String mostraCoupon() {
        StringBuilder str = new StringBuilder();
        for (Coupon c : gestCoupon)
            str.append("{").append(c.toString()).append("}");
        return str.toString();
    }

    /**
     * Inserisce un insieme di Coupon prestabiliti nella lista (per DEBUG)
     */
    public void popolaLista() {
        clear();

        Posizione Parma = new Posizione(44.801485, 10.3279036, "Parma");
        Posizione Milano = new Posizione(45.4654219, 9.1859243, "Milano");
        Posizione Bologna = new Posizione(44.4949, 11.3426, "Bologna");
        Posizione Ferrara = new Posizione(44.8381, 11.6198, "Ferrara");
        Posizione Piacenza = new Posizione(45.0526, 9.6930, "Piacenza");

        List<Posizione> l = new ArrayList<>();

        l.add(Parma);
        l.add(Milano);
        l.add(Bologna);
        l.add(Ferrara);
        l.add(Piacenza);

        gestCoupon.add(new Coupon("Conad", "A32DSL", 1, l));
        gestCoupon.add(new Coupon("Coop", "59HF83", 0));
        gestCoupon.add(new Coupon("Mediaworld", "GFER817", 1));
        gestCoupon.add(new Coupon("Game7Athletics", "LD89ID", 1));
        gestCoupon.add(new Coupon("CC Eurosia", "A32DS1", 1, l));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1988);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        gestCoupon.add(new Coupon("Trony", "YD76H1", 0, cal.getTime()));

        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.YEAR, 1988);
        cal1.set(Calendar.MONTH, Calendar.APRIL);
        cal1.set(Calendar.DAY_OF_MONTH, 1);
        gestCoupon.add(new Coupon("Camst", "FBDKAO", 1, cal1.getTime()));

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.YEAR, 2021);
        cal2.set(Calendar.MONTH, Calendar.DECEMBER);
        cal2.set(Calendar.DAY_OF_MONTH, 7);
        gestCoupon.add(new Coupon("Lidl", "M9D3RL", 1, cal2.getTime(), l));
    }

}
