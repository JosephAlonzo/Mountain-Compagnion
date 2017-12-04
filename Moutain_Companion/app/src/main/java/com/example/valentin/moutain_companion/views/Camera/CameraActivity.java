package com.example.valentin.moutain_companion.views.Camera;

import android.content.Intent;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.valentin.moutain_companion.R;
import com.example.valentin.moutain_companion.views.About_fragment;
import com.example.valentin.moutain_companion.views.Calibration_fragment;
import com.example.valentin.moutain_companion.views.Map_fragment;
import com.example.valentin.moutain_companion.views.Settings_fragment;
import com.example.valentin.moutain_companion.views.Tutorial_fragment;

public class CameraActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    private FragmentManager fm = null;
    private Fragment mFragment =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        try {

            mCamera = Camera.open();
        } catch (Exception e) {

            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if(mCamera != null ){

            mCameraView = new CameraView(this, mCamera);
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        mFragment = new Map_fragment();

        switch(id) {

            case R.id.nav_view0:

                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                // fragment = new CameraActivity();
                break;

            case R.id.nav_map:
                mFragment = new Map_fragment();
                fm.beginTransaction().replace(R.id.content_main,mFragment).commit();
                break;

            case R.id.nav_tutorial:
                mFragment = new Tutorial_fragment();
                fm.beginTransaction().replace(R.id.content_main,mFragment).commit();
                break;

            case R.id.nav_calibration:
                mFragment = new Calibration_fragment();
                fm.beginTransaction().replace(R.id.content_main,mFragment).commit();
                break;

            case R.id.nav_settings:
                mFragment = new Settings_fragment();
                fm.beginTransaction().replace(R.id.content_main,mFragment).commit();
                break;

            case R.id.nav_about:
                mFragment = new About_fragment();
                fm.beginTransaction().replace(R.id.content_main,mFragment).commit();
                break;

        }

        return true;
    }
}
