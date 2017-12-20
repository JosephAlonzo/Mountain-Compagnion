package iut_bm_lp.projet_mountaincompanion_v1.views.Camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import iut_bm_lp.projet_mountaincompanion_v1.Location.RapidGPSLock;
import iut_bm_lp.projet_mountaincompanion_v1.R;
import iut_bm_lp.projet_mountaincompanion_v1.controllers.MountainDataSource;
import iut_bm_lp.projet_mountaincompanion_v1.models.Mountain;
import iut_bm_lp.projet_mountaincompanion_v1.views.Azimuth.MyCurrentAzimuth;
import iut_bm_lp.projet_mountaincompanion_v1.views.Azimuth.OnAzimuthChangedListener;
import iut_bm_lp.projet_mountaincompanion_v1.views.MountainCompanionActivity;

public class CameraActivity extends AppCompatActivity implements MountainCompanionActivity,
        SensorEventListener, View.OnTouchListener{

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    public float hfov = (float) 50.2;
    public float vfov = (float) 20.0;
    private SensorManager mSensorManager;
    private RapidGPSLock mGPS;
    Sensor accelerometer;
    Sensor magnetometer;
    float[] mGravity;
    float[] mGeomagnetic;

    Timer timer;
    private int GPSretryTime = 60;
    private int compassSmoothingWindow = 50;

    private float compassAdjustment = 0;
    private ArrayList<HillMarker> mMarkers = new ArrayList<>();

    float mRotationMatrixA[] = new float[9];
    float mRottaionMatrixB[] = new float[9];
    float mDeclination = 0;
    private boolean mHasAccurateGravity = false;
    private boolean mHasAccurateAccelerometer = false;


    public int scrwidth = 10;
    public int scrheight = 10;
    public int scrdpi = 10;


    public DrawOnTop mDraw;
//    public static CameraPreviewSurface cv;
    private MountainDataSource database;
    private filteredDirection fd = new filteredDirection();
    private filteredElevation fe = new filteredElevation();

    boolean showdir = false;
    boolean showdist = false;
    boolean typeunits = false;
    boolean showheight = false;
    boolean showhelp = true;

    private static final float TEXT_SIZE_DECREMENT = 1;
    private static final float TEXT_SIZE_MIN = 7;

    public static final int ALPHA_LABEL_MAX = 255;
    public static final int ALPHA_LINE_MAX = 205;
    public static final int ALPHA_DECREMENT = 10;
    public static final int ALPHA_STROKE_MIN = 200;
    public static final int ALPHA_LABEL_MIN = 180;
    public static final int ALPHA_LINE_MIN = 50;

    Float textsize = 1f;


    TextView dirText;
    TextView fovText;
    TextView locText;

    RangeSeekBar heightSeekBar;
    RangeSeekBar distanceSeekBar;



    public class HillMarker{
        public HillMarker(int id, Rect loc) { location = loc; hillid =id;}
        public Rect location;
        public int hillid;
    }

    public int getRotation(){

        Display display = getWindowManager().getDefaultDisplay();
        return display.getRotation();
    }

    void setSeekBars() {

        heightSeekBar = (RangeSeekBar) findViewById(R.id.heightSeekBar);
        heightSeekBar.setRangeValues(200, 9000);
        heightSeekBar.setOnTouchListener(new View.OnTouchListener() {
            int throttle = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                throttle++;
                if(throttle % 15 ==0) {
                    UpdateMarkers();
                }
                return false;
            }
        });

        distanceSeekBar = (RangeSeekBar) findViewById(R.id.distanceSeekBar);
        distanceSeekBar.setRangeValues(10, 20000);
    }

    protected void onResume(){
        super.onResume();

        fd = new filteredDirection();
        fe = new filteredElevation();

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        mGPS.switchOn();
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new LocationTimerTask(),GPSretryTime* 1000,GPSretryTime* 1000);
        UpdateMarkers();
    }

    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer = null;
        mGPS.switchOff();
        mSensorManager.unregisterListener(this);
        database.close();
    }

    protected void onStop() {

        super.onStop();
        mGPS.switchOff();
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        mSensorManager.unregisterListener(this);
        database.close();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        dirText = (TextView) findViewById(R.id.dirText);

        mGPS = new RapidGPSLock(this);
        mGPS.switchOn();
        mGPS.findLocation();

        if(timer != null){
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new LocationTimerTask(), GPSretryTime* 1000, GPSretryTime* 1000);


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);

        mDraw = new DrawOnTop(this);
        addContentView(mDraw, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        try {
            mCamera = Camera.open(0);
        } catch (Exception e) {

            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null ){

            mCameraView = new CameraView(this, mCamera);
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);

        }

        setSeekBars();
        UpdateMarkers();
        heightSeekBar = (RangeSeekBar) findViewById(R.id.heightSeekBar);
        heightSeekBar.setOnTouchListener(new View.OnTouchListener() {

            int throttle = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                throttle++;
                if(throttle % 15 == 0) {
                    UpdateMarkers();
                }
                return false;
            }
        });

        distanceSeekBar = (RangeSeekBar) findViewById(R.id.distanceSeekBar);
        distanceSeekBar.setOnTouchListener(new View.OnTouchListener() {

            int throttle = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                throttle++;
                if(throttle % 15 ==0) {
                    UpdateMarkers();
                }
                return false;
            }
        });
    }

    @Override
    public void UpdateMarkers() {
        Location curLocation = mGPS.getCurrentLocation();
        if(curLocation != null) {

            RangeSeekBar heightSeekBar = (RangeSeekBar) findViewById(R.id.heightSeekBar);
            RangeSeekBar distanceSeekBar = (RangeSeekBar) findViewById(R.id.distanceSeekBar);

            database = MountainDataSource.getInstance(this);
            database.open();
            database.setDirections(curLocation,1, 9000,1, 30000);
            database.close();
        }
    }

    @Override
    public LocationManager GetLocationManager() {
        return (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    class filteredDirection {

        double dir;
        double sinValues[] = new double[compassSmoothingWindow];
        double cosValues[] = new double[compassSmoothingWindow];
        int index = 0;

        void addLatest(double d){

            sinValues[index] = Math.sin(d);
            cosValues[index] = Math.cos(d);
            index++;
            if(index > compassSmoothingWindow - 1) index = 0;
            double sumc = 0;
            double sums = 0;
            for (int a = 0; a < compassSmoothingWindow; a++){

                sumc += cosValues[a];
                sums += sinValues[a];
            }
            dir = Math.atan2(sums/compassSmoothingWindow,sumc/compassSmoothingWindow);

         //   Log.e("sensorChanged:", "" + dir);
        }

        double getDirection() {

            return (Math.toDegrees(dir) + compassAdjustment + 720) % 360;
        }

        int getVariation() {

            double Q;
            double sumc = 0;
            double sums = 0;
            for(int a = 0; a < compassSmoothingWindow; a++){
                sumc += cosValues[a];
                sums += sinValues[a];
            }
            double avgc = sumc/compassSmoothingWindow;
            double avgs = sums/compassSmoothingWindow;

            sumc = 0;
            sums = 0;
            for (int a = 0; a < compassSmoothingWindow; a++){

                sumc += Math.pow(cosValues[a] - avgc, 2);
                sums += Math.pow(sinValues[a] - avgs, 2);
            }

            Q = (sumc/(compassSmoothingWindow-1)) + (sums/(compassSmoothingWindow-1));

            return (int)(Q*1000);
        }
    }

    class filteredElevation {

        int AVERAGINGWINDOW = 10;
        double dir;
        double sinValues[] = new double[AVERAGINGWINDOW];
        double cosValues[] = new double[AVERAGINGWINDOW];
        int index = 0;

        void addLatest(double d) {

            sinValues[index] = Math.sin(d);
            cosValues[index] = Math.cos(d);
            index++;
            if(index > AVERAGINGWINDOW - 1) index = 0;
            double sumc = 0;
            double sums = 0;
            for(int a = 0; a < AVERAGINGWINDOW; a++){

                sumc += cosValues[a];
                sums += sinValues[a];
            }

            dir = Math.atan2(sums/AVERAGINGWINDOW, sumc/AVERAGINGWINDOW);
        }

        double getDirection() { return dir;}
    }

     class tmpMountain {

        Mountain m;
        double ratio;
        int toppt;
     }

     class DrawOnTop extends View{

        private Paint strokePaint = new Paint();
        private Paint textPain = new Paint();
        private Paint paint = new Paint();
        private Paint transpRedPaint = new Paint();
        private Paint variationPaint = new Paint();

        private Paint settingPaint = new Paint();
        private Paint settingPaint2 = new Paint();

        int subwidth;
        int subheight;
        int gap;
        int txtgap;
        int vtxtgap;
        RectF fovrect;

        ArrayList<tmpMountain> mountainsToPlot;

        public DrawOnTop(Context context){

            super(context);
            textPain.setTextAlign(Paint.Align.CENTER);
            textPain.setTypeface(Typeface.DEFAULT_BOLD);

            strokePaint.setTextAlign(Paint.Align.CENTER);
            strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(2);

            paint.setARGB(255,255,255,255);
            transpRedPaint.setARGB(100,255,0,0);

            subwidth = (int) (scrwidth*0.7);
            subheight = (int) (scrheight*0.7);
            gap = (scrwidth - subwidth);
            txtgap = gap + (subwidth/30);
            vtxtgap = (subheight / 10);

            mountainsToPlot = new ArrayList<>();
            fovrect = new RectF(gap,vtxtgap, scrwidth-gap,vtxtgap*11);
        }

        @Override
         protected void onDraw(Canvas canvas) {

            database = MountainDataSource.getInstance(getContext());
            database.open();
            ArrayList<Mountain> mountains =  database.getAllMountains();
            database.close();

            int topPt = calculateHillsCanFitOnCanvas((int)(scrheight/1.6), mountains);

            drawHillLabelLines(canvas, topPt);
            drawHillLabelText(canvas, topPt);

            drawLocationAndOrientationStatus(canvas);

            drawSettingsButton(canvas);

            super.onDraw(canvas);
        }

        private int calculateHillsCanFitOnCanvas(int topPt, ArrayList<Mountain> mountains){

            Float drawtextsize = textsize;
            mountainsToPlot.clear();
            mMarkers.clear();
            for (int h =0; h < mountains.size() && topPt > 0; h++){

                Mountain m1 = mountains.get(h);

                // this is the angle of the peak from our ligne of sight
                double offset = fd.getDirection() - m1.getDirection();
                double offset2 = fd.getDirection() - (360+m1.getDirection());
                double offset3 = 360+fd.getDirection() - (m1.getDirection());
                double ratio = 0;

                // is it in our line of sight
                boolean inLineOfSight = false;
                if(Math.abs(offset) * 2 < hfov){
                    ratio = offset / hfov * -1;
                    inLineOfSight = true;


                    Log.e("Line of sight", "" + inLineOfSight);
                }
                if (Math.abs(offset2) * 2 < hfov){
                    ratio = offset2 / hfov * -1;
                    inLineOfSight = true;


                    Log.e("Line of sight", "" + inLineOfSight);
                }
                if (Math.abs(offset3) * 2 < hfov){
                    ratio = offset3 / hfov * -1;
                    inLineOfSight = true;


                    Log.e("Line of sight", "" + inLineOfSight);
                }
                if (inLineOfSight){

                    tmpMountain tm = new tmpMountain();

                    tm.m = m1;
                    tm.ratio = ratio;
                    tm.toppt = topPt;
                    mountainsToPlot.add(tm);

                    topPt -= (showdir || showdist || showheight && tm.m.getAltitude() > 0 )?(1 + drawtextsize*2):drawtextsize;


                    Log.e("Line of sight", "" + inLineOfSight);
                }
            }

                topPt -= Math.max(0, 13 - drawtextsize);
                return  topPt;
        }


        private void drawHillLabelLines(Canvas canvas, int toppt){

            int alpha = ALPHA_LABEL_MAX;

            //draw lines first
            for (int i = 0; i < mountainsToPlot.size(); i++){

                textPain.setARGB(alpha, 255,255,255);
                strokePaint.setARGB(alpha, 0, 0, 0);
                tmpMountain tm = mountainsToPlot.get(i);
                double vratio = Math.toDegrees(tm.m.getVisualElevation() - fe.getDirection());
                int yloc = (int) ((scrheight * vratio / vfov) + (scrheight/2));
                int xloc = (int) ((scrwidth * tm.ratio) + (scrheight/2));
                canvas.drawLine(xloc, yloc, xloc,tm.toppt - toppt, strokePaint);
                canvas.drawLine(xloc, yloc, xloc, tm.toppt - toppt, textPain);

                canvas.drawLine(xloc-20, tm.toppt - toppt, xloc+20, tm.toppt - toppt, strokePaint);
                canvas.drawLine(xloc-20, tm.toppt - toppt, xloc+20, tm.toppt - toppt, textPain);

                if(alpha - ALPHA_DECREMENT >= ALPHA_LINE_MIN){

                    alpha -= ALPHA_DECREMENT;
                }
            }
        }

        private void drawHillLabelText(Canvas canvas, int toppt){
            boolean moreinfo;
            Float drawtextsize = textsize;
            int alpha = ALPHA_LABEL_MAX;
            // draw text over top
            for (int i =0; i < mountainsToPlot.size(); i++){
                textPain.setARGB(alpha, 255, 255, 255);
                strokePaint.setARGB(Math.min(alpha, ALPHA_STROKE_MIN), 0, 0, 0);

                textPain.setTextSize(drawtextsize);
                strokePaint.setTextSize(drawtextsize);

                tmpMountain tm = mountainsToPlot.get(i);
                moreinfo = (showdir ||showdist || showheight && tm.m.getAltitude() > 0);
                int xloc = ((int)(scrwidth * tm.ratio) + (scrwidth/2));

                Rect bnds = new Rect();
                strokePaint.getTextBounds(tm.m.getNom(),0,tm.m.getNom().length(), bnds);
                bnds.left += xloc - (textPain.measureText(tm.m.getNom()) / 2.0);
                bnds.right += xloc - (textPain.measureText(tm.m.getNom()) / 2.0);
                bnds.top += tm.toppt -5 - toppt;
                if(moreinfo) bnds.top -= drawtextsize;
                bnds.bottom += tm.toppt-5 -toppt;

                // for debug - draws bounding box of touch region to select hill
                // canvas.drawRect(bnds, strokePaint)

                mMarkers.add(new HillMarker(tm.m.getId(), bnds));
                canvas.drawText(tm.m.getNom(), xloc, tm.toppt - ((moreinfo)?drawtextsize:0) - 5 - toppt, strokePaint);
                canvas.drawText(tm.m.getNom(), xloc, tm.toppt - ((moreinfo)?drawtextsize:0) - 5 - toppt, textPain);

                if (showdir || showdist || showheight) {

                    boolean hascontents = false;
                    String marker = " (";
                    if(showdir){
                        hascontents = true;
                        marker += Math.floor(10*tm.m.getDirection())/10 + "\u00B0";
                    }
                    if(showdist){
                        hascontents = true;
                        double multip = (typeunits)?1:0.621371;
                        marker += (showdir ? " " : "") + Math.floor(10*tm.m.getDistance()*multip)/10;
                        if(typeunits) marker += "km"; else marker += "miles";
                    }
                    if(showheight){
                        if(tm.m.getAltitude() > 0){
                            hascontents = true;
                            marker += ((showdir || showdist) ? " " : "") + distanceAsImperialOrMetric(tm.m.getAltitude());
                        }
                    }
                    marker += ")";
                    if(hascontents)
                    {
                        canvas.drawText(marker, xloc, tm.toppt-5 - toppt, strokePaint);
                        canvas.drawText(marker, xloc, tm.toppt-5 - toppt, textPain);
                    }
                }

                if(alpha - ALPHA_DECREMENT >= ALPHA_LABEL_MIN){
                    alpha -= ALPHA_DECREMENT;
                }

                if(drawtextsize - TEXT_SIZE_DECREMENT >= TEXT_SIZE_MIN){
                    drawtextsize -= TEXT_SIZE_DECREMENT;
                }
            }
        }

        private void drawLocationAndOrientationStatus(Canvas canvas) {
            textPain.setTextSize(textsize);
            strokePaint.setTextSize(textsize);
            textPain.setARGB(255, 255, 255, 255);
            strokePaint.setARGB(255, 0, 0 , 0);

                // Log.e("fdDir:", "" + fd.getDirection());
            String compadj = (compassAdjustment>= 0)?"+":"";
            compadj += String.format("%.01f", compassAdjustment);
            String basetext = "" + (int)fd.getDirection() + (char)0x00B0;
            basetext +=" (adj:"+compadj+")";

                 //   Log.e("Dir: ", basetext);

            if(dirText != null) {
                dirText.setText(basetext);
            }

            basetext ="FOV: "+String.format("%.01f", hfov);
            if(fovText != null) {
                fovText.setText(basetext);
            }

            String acc;
            Location curLocation = mGPS.getCurrentLocation();

            if(curLocation != null){
                acc = "+/- " + distanceAsImperialOrMetric(curLocation.getAccuracy());
            }else{
                acc ="?";
            }

            basetext ="\nLocation " + acc;
            if(locText != null) {
                locText.setText(basetext);
            }

            basetext = "";

            if(curLocation == null) basetext = "No GPS position yet";
            else if (curLocation.getAccuracy() > 200) basetext = "Warning - GPS position too inaccurate";

            if(basetext.equals("")){
                canvas.drawText(basetext, scrwidth/2, scrheight/2, strokePaint);
                canvas.drawText(basetext, scrwidth/2, scrheight/2, textPain);
            }
            int va = fd.getVariation();
            variationPaint.setARGB(255, 255 , 0, 0);
            variationPaint.setStrokeWidth(4);
            int dashlength = scrheight / 10;
            for(int i = 0; i < 360; i+= 15){

                if(i > va) variationPaint.setARGB(255,0,255,0);
                canvas.drawLine((scrwidth/7) + (dashlength/5*(float)Math.sin(Math.toRadians(i))),
                        scrheight-(scrheight/2.7f) - (dashlength/5*(float)Math.cos(Math.toRadians(i))),
                        (scrwidth/7) + (dashlength*(float)Math.sin(Math.toRadians(i))),
                        scrheight-(scrheight/2.7f) - (dashlength*(float)Math.cos(Math.toRadians(i))),
                        variationPaint);
            }
        }

        private void drawSettingsButton(Canvas canvas){
            settingPaint2.setStyle(Paint.Style.STROKE);
            settingPaint.setAntiAlias(true);
            settingPaint2.setAntiAlias(true);
            settingPaint2.setStrokeWidth((int)(scrwidth/100.0));
            settingPaint2.setARGB(255, 255, 255, 255);
            settingPaint.setARGB(255, 0, 0, 0);

            float barwidth = scrwidth/12.0f;
            int startPtw = scrwidth/60;
            int startPth = subheight/60;
            int baroffset= subwidth/50;
            canvas.drawRect(0.0f, 0.0f, barwidth + (startPtw*2), baroffset * 3.3f, settingPaint);
            canvas.drawLine(startPtw, startPth, startPtw + barwidth, startPth, settingPaint2);

            canvas.drawLine(startPtw, startPth + baroffset, startPtw + barwidth, startPth + baroffset, settingPaint2);
            baroffset += baroffset;
            canvas.drawLine(startPtw, startPth + baroffset, startPtw + barwidth, startPth +baroffset, settingPaint2);
        }
     }

     private String distanceAsImperialOrMetric(double distance) {

        if(typeunits) return (int)distance + "m";
        else return (int)(distance*3.2808399) + "ft";
     }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && mHasAccurateAccelerometer)
                return;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && mHasAccurateGravity)
                return;
        }else{

            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) mHasAccurateAccelerometer =true;
            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) mHasAccurateGravity = true;
        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) mGravity = sensorEvent.values;
        if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) mGeomagnetic = sensorEvent.values;

        if(mGravity != null && mGeomagnetic != null) {

            float[] rotationMatrixA = mRotationMatrixA;
            if(SensorManager.getRotationMatrix(rotationMatrixA, null, mGravity, mGeomagnetic)){

                Matrix tmpA = new Matrix();
                tmpA.setValues(rotationMatrixA);
                tmpA.postRotate( -mDeclination);
                tmpA.getValues(rotationMatrixA);

                float[] rotationMatrixB = mRottaionMatrixB;

                switch (getRotation()){

                    //portrait - normal
                    case Surface.ROTATION_0: SensorManager.remapCoordinateSystem(rotationMatrixA,SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrixB);
                    break;

                    //rotation left (landscape)
                    case Surface.ROTATION_90: SensorManager.remapCoordinateSystem(rotationMatrixA,SensorManager.AXIS_X, SensorManager.AXIS_Z,rotationMatrixB);
                    break;

                    //upside down
                    case Surface.ROTATION_180: SensorManager.remapCoordinateSystem(rotationMatrixA, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrixB);
                    break;

                    //rotated right (landscape)
                    case Surface.ROTATION_270: SensorManager.remapCoordinateSystem(rotationMatrixA, SensorManager.AXIS_MINUS_Z, SensorManager.AXIS_X, rotationMatrixB);
                    break;

                    default: break;
                }

                float[] dv = new float[3];
                SensorManager.getOrientation(rotationMatrixB, dv);

                fd.addLatest(dv[0]);
                fe.addLatest((double)dv[1]);
//                UpdateMarkers();

            }
            mDraw.invalidate();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    class LocationTimerTask extends TimerTask{

        @Override
        public void run() {
            Log.d("MountainCompanion", "renew GPS search");
            runOnUiThread(new Runnable(){
                public void run(){
                    mGPS.RenewLocation();
                }
            });
        }
    }
}
