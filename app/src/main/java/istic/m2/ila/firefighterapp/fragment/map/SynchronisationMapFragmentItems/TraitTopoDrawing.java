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
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.fragment.map.Common.MapItem;

/**
 * Created by adou on 24/04/18.
 */

public class TraitTopoDrawing extends MapItem
{
    //region Members
    private TraitTopoDTO _TraitTopoDTO;
    private Marker _traitTopoMarker;
    //endregion

    //region Properties
    public long getId() { return _TraitTopoDTO.getId(); }
    //endregion

    //region Constructor

    /**
     * Initialise les valeurs requises pour la création et l'affichage d'un marker
     * @param TraitTopoDTO DTO qui contient les informations sur le marker qui sera créé
     * @param map map à utiliser
     * @param contextActivity le contexte de notre activité
     */
    public TraitTopoDrawing(TraitTopoDTO TraitTopoDTO, GoogleMap map, Activity contextActivity)
    {
        super(map, contextActivity);
        _TraitTopoDTO = TraitTopoDTO;

        initTraitTopo();
    }

    /**
     * Réalise l'affichage du marker
     */
    private void initTraitTopo()
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
     * @param TraitTopoDTO dto à utiliser pour la mise à jour
     */
    public void update(final TraitTopoDTO TraitTopoDTO)
    {
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                // Supprimer l'ancien sinistre, Dessiner et Mémoriser le nouveau sinistre
                _traitTopoMarker.remove();
                _TraitTopoDTO = TraitTopoDTO;
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
                _traitTopoMarker.remove();
                _TraitTopoDTO = null;
            }
        });
    }

    //region drawing

    /**
     * Crée une collection de clé-valeurs permettant de retrouver la ressource ID de l'image Bitmap
     * suivant son type afin de personnaliser les marker (forme, couleur)
     * @return Le référentiel clé-valeurs
     */
    private static final Map<ETypeTraitTopo,Integer> referentielTraitTopo = createReferentielTraitTopo ();
    private static Map<ETypeTraitTopo,Integer> createReferentielTraitTopo(){
        Map<ETypeTraitTopo,Integer> map = new HashMap<>();
        map.put(ETypeTraitTopo.DANGER, R.drawable.danger_24dp);
        map.put(ETypeTraitTopo.SENSIBLE, R.drawable.sensible_24dp);
        return map;
    }

    /**
     * Dessine sur la googleMap le marqueur en fonction des données de la DTO
     * @return la référence du marqueur qui a été déssiné
     */
    private void draw() {
        int rIcone = referentielTraitTopo.get(_TraitTopoDTO.getType());

        String rgbNoA = _TraitTopoDTO.getComposante().getCouleur().substring(0,7);
        Bitmap icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);

        // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
        LatLng pos = new LatLng(_TraitTopoDTO.getPosition().getLatitude(), _TraitTopoDTO.getPosition().getLongitude());
        Marker marker = _googleMap.addMarker(new MarkerOptions()
            .position(pos)
            .title(_TraitTopoDTO.getComposante().getLabel())
            .snippet(_TraitTopoDTO.getType().name() + " - " + _TraitTopoDTO.getComposante().getDescription())
            .icon(BitmapDescriptorFactory.fromBitmap(icon))
            // Les traits topographiques qu'on ajoute manuellement sont déplaçables
            .draggable(false));
        marker.setTag(_TraitTopoDTO);

        _traitTopoMarker = marker;
    }
    // endregion

    //endregion
}
