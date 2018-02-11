package com.td.yassine.zekri.boomboom.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.td.yassine.zekri.boomboom.ControlPanelActivity;
import com.td.yassine.zekri.boomboom.R;
import com.td.yassine.zekri.boomboom.Sessions;
import com.td.yassine.zekri.boomboom.SessionsListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Show_session_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Show_session_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Show_session_Fragment extends Fragment implements Start_session_Fragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FireLOG";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ListView listView;

    private View root;
    private FragmentManager fm = null;
    private Fragment fragment = null;

    private RecyclerView mMainList;

    private FirebaseFirestore mFirestore;

    private SessionsListAdapter mSessionsListAdapter;
    private List<Sessions> sessionsList;

    public Show_session_Fragment() {
        // Required empty public constructor
    }


        /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Show_session_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Show_session_Fragment newInstance(String param1, String param2) {
        Show_session_Fragment fragment = new Show_session_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_show_session_, container, false);

        sessionsList = new ArrayList<>();
        mSessionsListAdapter = new SessionsListAdapter(getContext(), sessionsList);

        mMainList = (RecyclerView) root.findViewById(R.id.mainList);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMainList.setAdapter(mSessionsListAdapter);


        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("sessions").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(e != null) {
                    Log.d(TAG, "Error : " + e.getMessage());
                }

                for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                    if(doc.getType() == DocumentChange.Type.ADDED){

//                        String userId = doc.getDocument().getId();

                        Sessions sessions = doc.getDocument().toObject(Sessions.class).widthId(doc.getDocument().getId());
                        sessionsList.add(sessions);

                        mSessionsListAdapter.notifyDataSetChanged();
                    }

                }

            }
        });

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
    public void onFragmentInteraction(Uri uri) {

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
