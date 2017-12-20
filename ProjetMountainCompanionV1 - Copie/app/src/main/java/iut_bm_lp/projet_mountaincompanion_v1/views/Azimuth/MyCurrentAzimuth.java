package iut_bm_lp.projet_mountaincompanion_v1.views.Azimuth;

import android.content.Context;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Zekri on 10/12/2017.
 */

public class MyCurrentAzimuth implements SensorEventListener {


    private OnAzimuthChangedListener mAzimutListener;
    Context mContext;



    private int azimuthFrom = 0;
    private int azimuthTo = 0;

    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    float[] mGravity;
    float[] mGeomagnetic;

    float mDeclination = 0;

    private boolean mHasAccurateGravity = false;
    private boolean mHasAccurateAccelerometer = false;


    public MyCurrentAzimuth(OnAzimuthChangedListener azimuthListener, Context context) {

        this.mAzimutListener = azimuthListener;
        this.mContext= context;
    }

    public void start() {

        mSensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);


    }

    public void stop() {

        mSensorManager.unregisterListener(this);
    }

    public void setOnShakeListener(OnAzimuthChangedListener listener) {

        mAzimutListener = listener;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        azimuthFrom = azimuthTo;

        if(sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && mHasAccurateAccelerometer) {
                return;
            }
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && mHasAccurateGravity) {
                return;
            }
        }else{
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mHasAccurateAccelerometer = true;
            }
            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mHasAccurateGravity = true;
            }
        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            mGravity = sensorEvent.values;
        }
        if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            mGeomagnetic = sensorEvent.values;
        }


        if(mGravity != null && mGeomagnetic != null) {

            float[] rotationMatrixA = new float[9];
            if(SensorManager.getRotationMatrix(rotationMatrixA, null, mGravity, mGeomagnetic)) {

                Matrix tmpA = new Matrix();
                tmpA.setValues(rotationMatrixA);
                tmpA.postRotate( -mDeclination);
                tmpA.getValues(rotationMatrixA);

                float[] dv = new float[3];
                SensorManager.getOrientation(rotationMatrixA, dv);
                azimuthTo = (int) Math.toDegrees(dv[0] + 360) % 360;

                mAzimutListener.onAzimuthChanged(azimuthFrom, azimuthTo);
                    //Log.e("orientation", ""+ test);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
