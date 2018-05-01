package istic.m2.ila.firefighterapp.map.Common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import com.google.android.gms.maps.GoogleMap;

public abstract class MapItem
{
    //region Members

    protected GoogleMap _googleMap;
    protected Activity _contextActivity;

    //endregion

    //region Constructor

    public MapItem(GoogleMap map, Activity activity)
    {
        _googleMap = map;
        _contextActivity = activity;
    }

//    protected abstract Marker draw();


    /**
     * Fonction pour changer la couleur d'une image
     * @param resDrawableId id de la ressource, notre image, à changer
     * @param colorRequested couleur qu'on attend
     * @return la nouvelle image avec la couleur
     */
    protected Bitmap getNewBitmapRenderedWithColor(int resDrawableId, String colorRequested) {
        Bitmap icon;// Copier le bitmap et le passer en Canvas sinon on aura une exception
        icon = BitmapFactory.decodeResource(_contextActivity.getResources(), resDrawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(Color.parseColor(colorRequested), PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);

        Canvas canvas = new Canvas(icon);
        canvas.drawBitmap(icon, 0, 0, paint);
        return icon;
    }

    //endregion
}
