package com.example.mycoupons;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MappaCoupons extends Fragment {

    /**
     * Stringa per identificazione Log
     */
    private final String TAG = MappaCoupons.class.getSimpleName();

    /**
     * se true le posizioni sono modificabili, se false no
     */
    private final boolean editable;

    /**
     * Lista dei punti da visualizzare
     */
    private final ArrayList<MarkerOptions> punti;

    /**
     * bottone per eliminare il Marker
     */
    private FloatingActionButton fab_eliminaMarker;

    /**
     * Marker selezionato
     */
    private Marker selectedMarker;

    /**
     * Mappa
     */
    private GoogleMap googleMap;


    public MappaCoupons(ArrayList<MarkerOptions> punti, boolean editable) {
        this.editable = editable;
        this.punti = punti;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inizializzo view
        View v = inflater.inflate(R.layout.fragment_mappa_coupons, container, false);

        //Inizializzo mappa
        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        v.post(() -> {
            //Async map
            assert smf != null;
            smf.getMapAsync(gm -> {
                this.googleMap = gm;
                //Quando la mappa e pronta
                //Imposto la visuale della mappa sull italia
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.8719, 12.5674), 4));

                //Nascondo il bottone per eliminare il Marker
                fab_eliminaMarker.setVisibility(View.INVISIBLE);

                if (this.getContext() == null) {
                    Log.e(TAG, "Contexr null");
                    return;
                }

                if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Errore permessi");
                }
                googleMap.setMyLocationEnabled(true);


                //Se si puo modificare i Marker
                if (editable) {
                    mostraPuntiMappa();

                    googleMap.setOnMarkerClickListener(this::markerClick);

                    googleMap.setOnMapClickListener(latLng -> {
                        //Quando clicco sulla mappa
                        fab_eliminaMarker.setVisibility(View.INVISIBLE);
                        //metto puntattore
                        MarkerOptions mo = new MarkerOptions();
                        mo.position(latLng);

                        //Aggiungo il nome della posizione come titolo del Marker
                        try {
                            Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
                            List<Address> str = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                            if (str.isEmpty())
                                mo.title(latLng.latitude + " - " + latLng.longitude);
                            else
                                mo.title(str.get(0).getAddressLine(0));

                        } catch (Exception e) {
                            mo.title(latLng.latitude + " - " + latLng.longitude);
                            Log.e(TAG, e.toString());
                        }

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        googleMap.addMarker(mo);
                        punti.add(mo);

                    });
                } else {
                    mostraPuntiMappa();
                }

            });
        });
        return v;
    }

    /**
     * Stampa tutti i Marker della lista nell mappa
     */
    private void mostraPuntiMappa() {
        if (punti != null)
            if (!punti.isEmpty())
                for (MarkerOptions mo : punti)
                    googleMap.addMarker(mo);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Setto l azione del pulsante per eliminare i Marker
        if (getView() == null) {
            Log.e(TAG, "getView null");
            return;
        }
        fab_eliminaMarker = getView().findViewById(R.id.btn_eliminaPos);
        fab_eliminaMarker.setOnClickListener(vw -> eliminaMarker());
    }


    /**
     * Quando si clicca sul marker viene mostrato il tasto per eliminarlo
     *
     * @param marker marker selezionato
     */
    private boolean markerClick(Marker marker) {
        selectedMarker = marker;
        fab_eliminaMarker.setVisibility(View.VISIBLE);
        return false;
    }

    /**
     * Ritorna tutti i marker della lista tradotti come Posizioni
     *
     * @return Posizioni della lista
     */
    public ArrayList<Posizione> getPosizioni() {
        ArrayList<Posizione> p = new ArrayList<>();
        for (MarkerOptions mo : punti) {
            p.add(new Posizione(mo.getPosition().latitude, mo.getPosition().longitude, mo.getTitle()));
        }
        return p;
    }

    /**
     * azione su click del pulsante elimina Marker
     */
    private void eliminaMarker() {

        for (MarkerOptions mo : punti) {
            if (mo.getPosition().latitude == selectedMarker.getPosition().latitude) {
                if (mo.getPosition().longitude == selectedMarker.getPosition().longitude) {
                    //Se lo trovo elimino il marker e lo tolgo dalla lista
                    punti.remove(mo);
                    selectedMarker.remove();
                    //Nascondo il btn
                    fab_eliminaMarker.setVisibility(View.INVISIBLE);
                    return;
                }
            }

        }
    }
}