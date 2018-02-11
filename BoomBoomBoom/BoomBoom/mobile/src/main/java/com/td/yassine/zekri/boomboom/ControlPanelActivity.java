package com.td.yassine.zekri.boomboom;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.td.yassine.zekri.boomboom.Account.MainActivity;
import com.td.yassine.zekri.boomboom.Account.ProfilActivity;
import com.td.yassine.zekri.boomboom.fragments.Action_session_Fragment;
import com.td.yassine.zekri.boomboom.fragments.Create_session_fragment;
import com.td.yassine.zekri.boomboom.fragments.Display_session_Fragment;
import com.td.yassine.zekri.boomboom.fragments.Show_session_Fragment;
import com.td.yassine.zekri.boomboom.fragments.Start_session_Fragment;

public class ControlPanelActivity extends AppCompatActivity implements Create_session_fragment.OnFragmentInteractionListener, Show_session_Fragment.OnFragmentInteractionListener, Start_session_Fragment.OnFragmentInteractionListener, Display_session_Fragment.OnFragmentInteractionListener, Action_session_Fragment.OnFragmentInteractionListener{

    private FirebaseAuth firebaseAuth;

    private FragmentManager fm = null;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        fragment = new Create_session_fragment();
        fm.beginTransaction().replace(R.id.testView, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.menuMyProfil:

                startActivity(new Intent(this, ProfilActivity.class));
                break;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() == null) {

            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
