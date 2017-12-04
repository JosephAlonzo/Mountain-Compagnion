package com.example.valentin.moutain_companion.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.valentin.moutain_companion.R;
import com.example.valentin.moutain_companion.views.Camera.CameraActivity;
import com.example.valentin.moutain_companion.views.Preferences.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,

        About_fragment.OnFragmentInteractionListener,
        Calibration_fragment.OnFragmentInteractionListener,
        Map_fragment.OnFragmentInteractionListener,
        Settings_fragment.OnFragmentInteractionListener,
        Tutorial_fragment.OnFragmentInteractionListener

{
    private FragmentManager fm = null;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            /*    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //lancer fragment map (page d'accueil)
        fm = getSupportFragmentManager();
        fragment = new Map_fragment();
//        fragment = new Tutorial_fragment();
        fm.beginTransaction().replace(R.id.content_main,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = new Map_fragment();

        switch(id) {

            case R.id.nav_view0:

                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                // fragment = new CameraActivity();
                break;

            case R.id.nav_map:
                fragment = new Map_fragment();
                fm.beginTransaction().replace(R.id.content_main,fragment).commit();
                break;

            case R.id.nav_tutorial:
                fragment = new Tutorial_fragment();
                fm.beginTransaction().replace(R.id.content_main,fragment).commit();
                break;

            case R.id.nav_calibration:
                fragment = new Calibration_fragment();
                fm.beginTransaction().replace(R.id.content_main,fragment).commit();
                break;

            case R.id.nav_settings:
                fragment = new Settings_fragment();
                fm.beginTransaction().replace(R.id.content_main,fragment).commit();
                break;

            case R.id.nav_about:
                fragment = new About_fragment();
                fm.beginTransaction().replace(R.id.content_main,fragment).commit();
                break;

        }

//        fm.beginTransaction().replace(R.id.content_main,fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
