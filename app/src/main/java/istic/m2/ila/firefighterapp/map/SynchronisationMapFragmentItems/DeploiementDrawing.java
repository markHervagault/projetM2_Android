package istic.m2.ila.firefighterapp.map.SynchronisationMapFragmentItems;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.EEtatDeploiement;
import istic.m2.ila.firefighterapp.map.Common.MapItem;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by adou on 24/04/18.
 */

public class DeploiementDrawing extends MapItem
{
    //region Members
    private DeploiementDTO _deploiementDTO;
    private Marker _deploimentMarker;
    //endregion

    //region Properties
    public long getId() { return _deploiementDTO.getId(); }
    //endregion

    //region Constructor

    /**
     * Initialise les valeurs requises pour la création et l'affichage d'un marker
     * @param DeploiementDTO DTO qui contient les informations sur le marker qui sera créé
     * @param map map à utiliser
     * @param contextActivity le contexte de notre activité
     */
    public DeploiementDrawing(DeploiementDTO DeploiementDTO, GoogleMap map, Activity contextActivity)
    {
        super(map, contextActivity);
        _deploiementDTO = DeploiementDTO;

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
     * @param DeploiementDTO dto à utiliser pour la mise à jour
     */
    public void update(final DeploiementDTO DeploiementDTO)
    {
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                // Supprimer l'ancien sinistre, Dessiner et Mémoriser le nouveau sinistre
                if(_deploimentMarker != null){
                    _deploimentMarker.remove();
                }
                _deploiementDTO = DeploiementDTO;
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
                _deploimentMarker.remove();
                _deploiementDTO = null;
            }
        });
    }

    //region drawing

    /**
     * Crée une collection de clé-valeurs permettant de retrouver la ressource ID de l'image Bitmap
     * suivant son type afin de personnaliser les marker (forme, couleur)
     * @return Le référentiel clé-valeurs
     */
    private static final Map<EEtatDeploiement,Integer> referentielMoyen = createReferentielMoyen ();
    private static Map<EEtatDeploiement,Integer> createReferentielMoyen(){
        Map<EEtatDeploiement,Integer> map = new HashMap<>();
        map.put(EEtatDeploiement.DEMANDE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.VALIDE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.ENGAGE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.EN_ACTION, R.drawable.moyen);
        return map;
    }

    public static Bitmap fusionImg(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 7, 40, null);
        return bmOverlay;
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    /**
     * Dessine sur la googleMap le marqueur en fonction des données de la DTO
     */
    public void draw() {
        if(!(_deploiementDTO.getState() == EEtatDeploiement.DESENGAGE
                || _deploiementDTO.getState() == EEtatDeploiement.REFUSE)
                && !_deploiementDTO.isPresenceCRM()
                && _deploiementDTO.getGeoPosition() != null) {
            // Récupération des icônes en fonction du type (change ou change pas)
            int rIcone = referentielMoyen.get(_deploiementDTO.getState());

            String rgbNoA = _deploiementDTO.getComposante().getCouleur().substring(0, 7);
            Bitmap icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);
            String label = "";
            if (_deploiementDTO.getState() != EEtatDeploiement.DEMANDE) {
                label = _deploiementDTO.getVehicule().getLabel();
            }
            Bitmap bm = textAsBitmap(label, 13, Color.parseColor(rgbNoA));

            icon = fusionImg(icon, bm);


            // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
            LatLng pos = new LatLng(_deploiementDTO.getGeoPosition().getLatitude(), _deploiementDTO.getGeoPosition().getLongitude());
            _deploimentMarker = _googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(label)
                    .snippet(label + " - " + _deploiementDTO.getComposante().getDescription())
                    .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    .draggable(false));
            _deploimentMarker.setTag(_deploiementDTO);







        }
    }
    // endregion

    //endregion
}
