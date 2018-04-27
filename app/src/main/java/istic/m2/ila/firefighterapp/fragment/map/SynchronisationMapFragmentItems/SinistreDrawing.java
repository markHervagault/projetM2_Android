package istic.m2.ila.firefighterapp.fragment.map.SynchronisationMapFragmentItems;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.ESinistre;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems.MapItem;

/**
 * Created by adou on 24/04/18.
 */

public class SinistreDrawing extends MapItem
{
    //region Members
    private SinistreDTO _sinistreDTO;
    private Marker _sinistreMarker;
    //endregion

    //region Properties
    public long getId() { return _sinistreDTO.getId(); }
    //endregion

    //region Constructor

    /**
     * Initialise les valeurs requises pour la création et l'affichage d'un marker
     * @param sinistreDTO DTO qui contient les informations sur le marker qui sera créé
     * @param map map à utiliser
     * @param contextActivity le contexte de notre activité
     */
    public SinistreDrawing(SinistreDTO sinistreDTO, GoogleMap map, Activity contextActivity)
    {
        super(map, contextActivity);
        _sinistreDTO = sinistreDTO;

        initSinistre();
    }

    /**
     * Réalise l'affichage du marker
     */
    private void initSinistre()
    {
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
            draw();
            }
        });
    }
    //endregion

    //region Update Methods

    /**
     * Mise à jour d'un marker en fonction de la DTO qui lui est associée
     * @param sinistreDTO dto à utiliser pour la mise à jour
     */
    public void update(final SinistreDTO sinistreDTO)
    {
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                // Supprimer l'ancien sinistre, Dessiner et Mémoriser le nouveau sinistre
                _sinistreMarker.remove();
                _sinistreDTO = sinistreDTO;
                draw();
            }
        });
    }

    /**
     * Supprime le marker
     */
    public void delete()
    {
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                // Supprimer l'ancien sinistre
                _sinistreMarker.remove();
                _sinistreDTO = null;
            }
        });
    }

    //region drawing

    private static final Map<ESinistre,Integer> referentielSinistre = createReferentielSinistre ();

    /**
     * Crée une collection de clé-valeurs permettant de retrouver la ressource ID de l'image Bitmap
     * suivant son type afin de personnaliser les marker (forme, couleur)
     * @return Le référentiel clé-valeurs
     */
    private static Map<ESinistre,Integer> createReferentielSinistre(){
        Map<ESinistre,Integer> map = new HashMap<>();
        map.put(ESinistre.CENTRE, R.drawable.centre_sinistre);
        map.put(ESinistre.POINT, R.drawable.ic_star_black_24dp);
        map.put(ESinistre.ZONE, R.drawable.boom70x70);
        return map;
    }

    /**
     * Dessine sur la googleMap le marqueur
     */
    private void draw() {
        /*int rIcone = referentielSinistre.get(_sinistreDTO.getType());

        String rgbNoA = _sinistreDTO.getComposante().getCouleur().substring(0, 7);
        Bitmap icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);

        // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
        LatLng pos = new LatLng(_sinistreDTO.getGeoPosition().getLatitude(), _sinistreDTO.getGeoPosition().getLongitude());
        _sinistreMarker = _googleMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(_sinistreDTO.getComposante().getLabel())
                .snippet(_sinistreDTO.getType().name() + " - " + _sinistreDTO.getComposante().getDescription())
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                // Les sinistres qu'on ajoute manuellement sont déplaçables
                .draggable(true)
        );*/
    }
    // endregion

    //endregion
}
