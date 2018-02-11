package com.td.yassine.zekri.boomboom.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.td.yassine.zekri.boomboom.ControlPanelActivity;
import com.td.yassine.zekri.boomboom.R;
import com.zekri.yassine.linked.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Action_session_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Action_session_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Action_session_Fragment extends Fragment implements DataClient.OnDataChangedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView textViewNomSession;
    private TextView textViewHeartRate;

    private View root;

    public Action_session_Fragment() {
        // Required empty public constructor
    }

    public static Action_session_Fragment newInstance(String param1, String param2) {
        Action_session_Fragment fragment = new Action_session_Fragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_action_session_, container, false);

        textViewNomSession = root.findViewById(R.id.textViewNomSession);
        textViewHeartRate = root.findViewById(R.id.textViewHeartRate);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            String nomSession = bundle.getString("nomSession", "nullSession");
            textViewNomSession.setText(nomSession);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

// GET DATA FROM WATCH
    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {

        for(DataEvent event : dataEventBuffer) {
            if(event.getType() == DataEvent.TYPE_CHANGED) {

                DataItem item = event.getDataItem();
                if(item.getUri().getPath().compareTo(Constants.HEART_COUNT_PATH) == 0){

                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    int st = dataMap.getInt(Constants.HEARTBEAT_COUNT_VALUE, 0);
                    textViewHeartRate.setText(String.valueOf(st) + " battements de coeurs !");
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(getActivity()).addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Wearable.getDataClient(getActivity()).removeListener(this);
    }
}
