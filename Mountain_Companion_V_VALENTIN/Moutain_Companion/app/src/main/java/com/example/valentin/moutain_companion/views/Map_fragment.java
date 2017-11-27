package com.example.valentin.moutain_companion.views;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.valentin.moutain_companion.managers.MountainDataSource;
import com.example.valentin.moutain_companion.managers.MountainDatabaseHandler;
import com.example.valentin.moutain_companion.models.Mountain;
import com.google.android.gms.location.LocationListener;

import com.example.valentin.moutain_companion.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Map_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Map_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map_fragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int REQUEST_LOCATION = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest;

    private Location mLastLocation;
    private GoogleMap mGoogleMap;

    private LatLng mLatLngMaposition;

    private int mRadius;

    private OnFragmentInteractionListener mListener;

    private MountainDataSource mDataSource;
    private boolean mLocationEnabled;

    public Map_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Map_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Map_fragment newInstance(String param1, String param2) {
        Map_fragment fragment = new Map_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if(mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_map_fragment, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {

        super.onStart();

        setLocationParameters();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {

        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop() {

        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onResume() {

        super.onResume();
        setLocationParameters();
        if(mGoogleApiClient.isConnected()) {

            if(mLocationEnabled) {

                startLocationUpdates();
            }
        }
    }

    /***************    Méthodes de l'interface LocationListener        *************************/

    @Override
    public void onLocationChanged(Location location) {

        Toast.makeText(getActivity(), "New Location !", Toast.LENGTH_SHORT).show();

        mountainUpdateUi(location);
    }

    /**************/

    /***************    Méthode de l'interface OnMapReadyCallback        *************************/
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        mountainUpdateUi(null);

    }

    /**********/


    /***************    Méthodes de l'interface GoogleApiClient.ConnectionCallbacks        *************************/
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestLocationPermission();
        } else {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {

                mountainUpdateUi(mLastLocation);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngMaposition, 10.0f));
            }

            if (mLocationEnabled) {

                startLocationUpdates();
            } else {

                stopLocationUpdates();
            }
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    /******/

    /***************    Méthode de l'interface GoogleApiClient.OnConnectionFailedListener       *************************/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    /******/




    private void requestLocationPermission() {

        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
    }

    private void setLocationParameters() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mLocationEnabled = sharedPref.getBoolean(getResources().getString(R.string.key_location_switch), false);

        if(mLocationEnabled) {

            Toast.makeText(getActivity(), "Localisation ON", Toast.LENGTH_SHORT).show();

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
        }
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestLocationPermission();
        } else {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    private void mountainUpdateUi(Location location) {

        if (location != null) {

            mGoogleMap.clear();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mRadius = Integer.parseInt(sharedPref.getString(getResources().getString(R.string.key_search_radius), "0"));

        //LatLngBounds.Builder b = new LatLngBounds.Builder();

        mLatLngMaposition = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.addMarker(new MarkerOptions().position(mLatLngMaposition).title("Ma position"));

        mDataSource = new MountainDataSource(getContext());
        mDataSource.open();

        ArrayList<Mountain> mountains = mDataSource.getAllMountains();

        for (Mountain m: mountains) {

            Location latLngMountain = new Location("");
            latLngMountain.setLatitude(m.getLatitude());
            latLngMountain.setLongitude(m.getLongitude());

            if (location.distanceTo(latLngMountain) <= mRadius) {

                LatLng latLngTest2 = new LatLng(m.getLatitude(), m.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(latLngTest2).title(m.getNom()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_montagne)));

              //  b.include(latLngTest2);
            }
        }

//                LatLngBounds bound = b.build();
//                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bound, 16.0f);

        //  mGoogleMap.animateCamera(cu);

        mDataSource.close();
    }

    }

    /***
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
