package com.zekri.yassine.wear.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.zekri.yassine.wear.R;
import com.zekri.yassine.wear.Sessions;
import com.zekri.yassine.wear.SessionsListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Show_Session_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Show_Session_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Show_Session_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "FireLOG";

    private View root;

    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;

    private SessionsListAdapter mSessionsListAdapter;
    private List<Sessions> mSessionsList;

    public Show_Session_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Show_Session_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Show_Session_Fragment newInstance(String param1, String param2) {
        Show_Session_Fragment fragment = new Show_Session_Fragment();
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

        root = inflater.inflate(R.layout.fragment_show__session_, container, false);

        mSessionsList = new ArrayList<>();
        mSessionsListAdapter = new SessionsListAdapter(getContext(), mSessionsList);

        mMainList = root.findViewById(R.id.mainList);
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
                        mSessionsList.add(sessions);

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
