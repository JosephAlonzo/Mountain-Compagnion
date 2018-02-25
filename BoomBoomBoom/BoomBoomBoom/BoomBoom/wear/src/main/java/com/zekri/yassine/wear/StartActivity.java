package com.zekri.yassine.wear;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.wearable.activity.WearableActivity;
import com.zekri.yassine.wear.fragments.Show_Session_Fragment;
import com.zekri.yassine.wear.fragments.UserInfo_Fragment;


public class StartActivity extends FragmentActivity implements Show_Session_Fragment.OnFragmentInteractionListener, UserInfo_Fragment.OnFragmentInteractionListener{

    private FragmentManager fm = null;
    private Fragment mFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        fm = getSupportFragmentManager() ;
        mFragment = new Show_Session_Fragment();
        fm.beginTransaction().replace(R.id.content_start, mFragment).commit();

        // Enables Always-on
//        setAmbientEnabled();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
