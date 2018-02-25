package com.td.yassine.zekri.boomboom.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.td.yassine.zekri.boomboom.R;
import com.td.yassine.zekri.boomboom.Sessions;
import com.td.yassine.zekri.boomboom.adapterUsers.RecyclerItemTouchHelper;
import com.td.yassine.zekri.boomboom.adapterUsers.User;
import com.td.yassine.zekri.boomboom.adapter.ClickListener;
import com.td.yassine.zekri.boomboom.adapter.FirestoreAdapter;
import com.td.yassine.zekri.boomboom.adapterUsers.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Action_session_Fragment extends Fragment implements ClickListener, OnFailureListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static String TAG = "ACTION_FRAGMENT_DEBUG";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FirebaseFirestore mFirestore;

    private TextView textViewNomSession;

    private View root;

    private String idSession;
    private String nomSession;

    private Button buttonFermerSession;

    private int cnt = 0;

    private Query query;
    private static final String USERS = "users";
    private static final int LIMIT = 10;

    @BindView(R.id.usersList)
    RecyclerView usersListView;

    private List<User> users;
    FirestoreAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_action_session_, container, false);

        textViewNomSession = root.findViewById(R.id.textViewNomSession);
        buttonFermerSession = root.findViewById(R.id.buttonFermerSession);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            idSession = bundle.getString("idSession", "nullSession");
        }

        mFirestore = FirebaseFirestore.getInstance();

        DocumentReference docRef = mFirestore.collection("sessions").document(idSession);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(e != null) {
                    Log.w(TAG, "Listen failed." , e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {

                    nomSession = documentSnapshot.getString("nom");
                    textViewNomSession.setText(nomSession);
                    Log.d(TAG, "Current data: " + documentSnapshot.getData());
                }else {

                    Log.d(TAG, "Current data: NULL");
                }
            }
        });


        DocumentReference docRef2 = mFirestore.document("sessions/"+idSession);

        query = docRef2.collection(USERS).orderBy("nom", Query.Direction.DESCENDING)
                .limit(LIMIT);

        users = new ArrayList<>();

        adapter = new UserListAdapter(query,this){

            @Override
            protected void onDataChanged() {
                Log.d(TAG, "Data Change");
                usersListView.scrollToPosition(0);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                handleFirebaseException(e);
            }
        };

        usersListView = root.findViewById(R.id.usersList);

        layoutManager = new GridLayoutManager(getContext(), 2);
        usersListView.setLayoutManager(layoutManager);

        usersListView.setItemAnimator(new DefaultItemAnimator());
        usersListView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        usersListView.setAdapter(adapter);

//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(usersListView);

        buttonFermerSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(adapter != null) {

            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {

        if (e instanceof FirebaseFirestoreException) {
            handleFirebaseException((FirebaseFirestoreException) e);
        }
        Log.e(TAG, "Document can't be upload" + e.toString());
    }


    private void handleFirebaseException(@NonNull FirebaseFirestoreException e) {
        if (e.getCode() == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
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
    public void onItemClick(View view, int position) {
        final User users = adapter.getSnapshot(position).toObject(User.class);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
