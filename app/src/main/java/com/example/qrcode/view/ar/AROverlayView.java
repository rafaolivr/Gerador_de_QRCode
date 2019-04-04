package com.example.qrcode.view.ar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import com.example.qrcode.R;
import com.example.qrcode.helper.LocationHelper;
import com.example.qrcode.model.ARPoint;

import static com.example.qrcode.util.Constantes.lat;
import static com.example.qrcode.util.Constantes.lng;


/**
 * Created by ntdat on 1/13/17.
 */

public class AROverlayView extends View {



    Context context;
    private float[] rotatedProjectionMatrix = new float[16];
    private Location currentLocation;
    private List<ARPoint> arPoints;
    private double distance;


    public AROverlayView(Context context) {
        super(context);

        this.context = context;

        //Demo points
        arPoints = new ArrayList<ARPoint>() {{
            add(new ARPoint("", -23.62034468, -46.69905871, 750));
        }};
    }

    public void updateRotatedProjectionMatrix(float[] rotatedProjectionMatrix) {
        this.rotatedProjectionMatrix = rotatedProjectionMatrix;
        this.invalidate();
    }

    public void updateCurrentLocation(Location currentLocation){
        this.currentLocation = currentLocation;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentLocation == null) {
            return;
        }

        final int radius = 30;
        Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        //paint.setTextSize(60);

        for (int i = 0; i < arPoints.size(); i ++) {
            float[] currentLocationInECEF = LocationHelper.WSG84toECEF(currentLocation);
            float[] pointInECEF = LocationHelper.WSG84toECEF(arPoints.get(i).getLocation());
            float[] pointInENU = LocationHelper.ECEFtoENU(currentLocation, currentLocationInECEF, pointInECEF);

            float[] cameraCoordinateVector = new float[4];
            Matrix.multiplyMV(cameraCoordinateVector, 0, rotatedProjectionMatrix, 0, pointInENU, 0);

            //Drawable d = getResources().getDrawable(R.drawable.teste, null);

            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.everislogo);
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);


            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
            if (cameraCoordinateVector[2] < 0) {
                float x  = (0.5f + cameraCoordinateVector[0]/cameraCoordinateVector[3]) * canvas.getWidth();
                float y = (0.5f - cameraCoordinateVector[1]/cameraCoordinateVector[3]) * canvas.getHeight();

                DecimalFormat df = new DecimalFormat();

                distance = calcDistance(lat, -23.62034468, lng, -46.69905871);

                String distanceS = df.format(distance);

                Log.d("Abacaxi", distanceS);

                canvas.drawText(arPoints.get(i).getName(), x - (28 * arPoints.get(i).getName().length() / 10), y - 200, paint);
                canvas.drawText(distanceS, x - (28 * arPoints.get(i).getName().length() / 10), y - 100, paint);
                canvas.drawBitmap(bitmap, x - (30 * arPoints.get(i).getName().length() / 4), y - 500, paint);
                //d.draw(canvas);
            }
        }
    }

    private double calculaDistancia(double lat1, double lat2, double lng1, double lng2) {
        //double earthRadius = 3958.75;//miles
        double earthRadius = 6371;//kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist * 1000; //em metros
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double calcDistance(double lat1, double lat2, double lng1, double lng2){
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6366000 * c;
    }
}
