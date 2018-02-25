package com.zekri.yassine.wear;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.support.wearable.activity.WearableActivity;
import android.widget.Toast;

import com.zekri.yassine.wear.fragments.HeartRateFragment;
import com.zekri.yassine.wear.utils.DrawerItem;

import java.util.ArrayList;

public class MainActivity extends WearableActivity implements WearableNavigationDrawerView.OnItemSelectedListener, HeartRateFragment.OnFragmentInteractionListener{

    public final static int ITEM_MENU_HEART_RATE = 1;
    private int mSelectedScreen;

    private WearableNavigationDrawerView mWearableNavigationDrawerView;
    private ArrayList<DrawerItem> mDrawerItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerItemArrayList = initializeScreenSystem();
        mSelectedScreen = 1;

        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        String idSession = intent.getStringExtra("idSession");

        Bundle bundle1,bundle2;
        bundle1 = new Bundle();
        bundle2 = new Bundle();

        bundle1.putString("username", userName);
        bundle2.putString("idSession", idSession);
        bundle1.putBundle("idSession", bundle2);

        HeartRateFragment fragment = new HeartRateFragment();
        fragment.setArguments(bundle1);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mWearableNavigationDrawerView = findViewById(R.id.top_navigation_drawer);
        mWearableNavigationDrawerView.setAdapter(
                new WearableNavigationDrawerView.WearableNavigationDrawerAdapter() {
                    @Override
                    public CharSequence getItemText(int pos) {
                        return mDrawerItemArrayList.get(pos).getName();
                    }

                    @Override
                    public Drawable getItemDrawable(int pos) {
                        return null;
                    }

                    @Override
                    public int getCount() {
                        return mDrawerItemArrayList.size();
                    }
                }
        );
        // Enables Always-on
        setAmbientEnabled();

        mWearableNavigationDrawerView.getController().peekDrawer();
        mWearableNavigationDrawerView.addOnItemSelectedListener(this);
    }

    private ArrayList<DrawerItem> initializeScreenSystem() {

        ArrayList<DrawerItem> screens = new ArrayList<>();
        String[] fragmentArrayNames = getResources().getStringArray(R.array.screens);

        for(int i = 0; i < fragmentArrayNames.length; i++){

            String fragmentName = fragmentArrayNames[i];
            int fragmentResourceId =
                    getResources().getIdentifier(fragmentName, "array", getPackageName());
            String[] fragmentInformation = getResources().getStringArray(fragmentResourceId);

            screens.add(new DrawerItem(
                    fragmentInformation[0]
            ));
        }

        return screens;
    }


    @Override
    public void onItemSelected(int pos) {

        mSelectedScreen = pos;

        switch (mSelectedScreen) {

            case ITEM_MENU_HEART_RATE:

                HeartRateFragment fragment = new HeartRateFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();

                break;
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
