package com.zekri.yassine.wear.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zekri.yassine.linked.Constants;
import com.zekri.yassine.wear.R;
import com.zekri.yassine.wear.StartActivity;
import com.zekri.yassine.wear.service.WearHeartbeatEmulatorService;
import com.zekri.yassine.wear.tasks.SynchronizeAsyncTaskHeart;
import com.zekri.yassine.wear.utils.DailyHeartbeat;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeartRateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeartRateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartRateFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static String TAG = "HeartRateFragmentDebug";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView textViewHeartRate;
    private TextView textViewMoyenne;
    private int moyenne = 80;

    private View root;

    private boolean wearBodySensorsPermissionApproved = false;
    private static final int PERMISSION_REQUEST_BODY_SENSORS = 1;

    private Intent heartBeatServiceIntent;

    private BroadcastReceiver br;

    private FirebaseFirestore mFireStore;

    private SynchronizeAsyncTaskHeart mSynchronizeAsynTask;

    private Button buttonMinus;
    private Button buttonPlus;
    private Button buttonQuitterSeance;

    private DocumentReference docRef;

    private Map<String, Object> userMap = new HashMap<>();
    private String currentIdUser;
    private String idSession;

    public HeartRateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeartRateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeartRateFragment newInstance(String param1, String param2) {
        HeartRateFragment fragment = new HeartRateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_heart_rate, container, false);


        mFireStore = FirebaseFirestore.getInstance();


        buttonMinus = root.findViewById(R.id.buttonMinus);
        buttonPlus = root.findViewById(R.id.buttonPlus);
        buttonQuitterSeance = root.findViewById(R.id.buttonQuitterSeance);
        textViewMoyenne = root.findViewById(R.id.textViewMoyenne);

        textViewMoyenne.setText(String.valueOf(moyenne));

        buttonMinus.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonQuitterSeance.setOnClickListener(this);

        Bundle bundle =  this.getArguments();
        String userName = bundle.getString("username");
        Bundle bundleIdSession = bundle.getBundle("idSession");
        idSession = bundleIdSession.getString("idSession");

        docRef = FirebaseFirestore.getInstance().document("sessions/"+idSession);

        Toast.makeText(getContext(), "Bienvenue: " + userName, Toast.LENGTH_SHORT).show();

        userMap.put("nom", userName);

        docRef.collection("users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Success, id: " + documentReference.getId());
                currentIdUser = documentReference.getId();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed : "+ e.getMessage());
            }
        });

        textViewHeartRate = root.findViewById(R.id.textViewHeartRate);

        if(!wearBodySensorsPermissionApproved) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BODY_SENSORS}, PERMISSION_REQUEST_BODY_SENSORS);
            wearBodySensorsPermissionApproved = true;
        }

        heartBeatServiceIntent = new Intent(getActivity(), WearHeartbeatEmulatorService.class);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int heartbeats = intent.getExtras().getInt(Constants.HEARTBEAT_COUNT_VALUE, 10);
                textViewHeartRate.setText(String.valueOf(heartbeats));

                mSynchronizeAsynTask = new SynchronizeAsyncTaskHeart(getActivity());
                mSynchronizeAsynTask.execute(new Integer(heartbeats));


                sendToDatabase(heartbeats);

            }
        };

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void sendToDatabase(int heartbeats) {

        Log.e("DEBUGG", "hey");

//        Map<String, String> userMap = new HashMap<>();

//        userMap.put("Nom", "Yassine");
        userMap.put("frequence", String.valueOf(heartbeats));

        docRef = FirebaseFirestore.getInstance().document("sessions/"+idSession+"/users/"+currentIdUser);
        docRef.update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.getMessage();

                Toast.makeText(getContext(), "Send FAILED: " + error, Toast.LENGTH_SHORT).show();
            }
        });
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
    public void onPause() {
        super.onPause();
        getActivity().stopService(heartBeatServiceIntent);
        getActivity().unregisterReceiver(br);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().startService(heartBeatServiceIntent);
        getActivity().registerReceiver(br, new IntentFilter(Constants.HEARTBEAT_COUNT_MESSAGE));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonMinus:

                this.moyenne -= 10;
                textViewMoyenne.setText(String.valueOf(moyenne));
                break;

            case R.id.buttonPlus:

                this.moyenne += 10;
                textViewMoyenne.setText(String.valueOf(moyenne));
                break;

            case R.id.buttonQuitterSeance:

                docRef = FirebaseFirestore.getInstance().document("sessions/"+idSession+"/users/"+currentIdUser);
                docRef.delete();
                getActivity().finish();
                Intent intent = new Intent(getContext(), StartActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
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
