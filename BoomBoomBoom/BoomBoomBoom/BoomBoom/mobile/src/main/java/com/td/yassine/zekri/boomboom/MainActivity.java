package com.td.yassine.zekri.boomboom;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.td.yassine.zekri.boomboom.Account.LoginActivity;
import com.td.yassine.zekri.boomboom.Account.ProfilActivity;
import com.td.yassine.zekri.boomboom.adapter.*;
import com.td.yassine.zekri.boomboom.adapter.SessionsListAdapter;
import com.td.yassine.zekri.boomboom.fragments.Action_session_Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, SessionDialogFragment.SessionsListener, OnFailureListener, Action_session_Fragment.OnFragmentInteractionListener {

    public static final String SESSIONS = "sessions";
    private static final String TAG = "MainActivity";
    private static final int LIMIT = 10;

    @BindView(R.id.sessionsList)
    RecyclerView sessionsListView;

    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;

    DocumentReference docRef;
    FirestoreAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    private SessionDialogFragment sessionDialogFragment;
    private List<Sessions> sessions;
    private Query query;

    private FragmentManager fm;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

        query = firebaseFirestore.collection(SESSIONS).orderBy("nom", Query.Direction.ASCENDING)
                .limit(LIMIT);
        sessions = new ArrayList<>();

        adapter = new SessionsListAdapter(query, this){
            @Override
            protected void onDataChanged() {
                Log.d(TAG, "Data Change");
                sessionsListView.scrollToPosition(0);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                handleFirebaseException(e);
            }
        };
        layoutManager = new LinearLayoutManager(this);
//        layoutManager = new GridLayoutManager(this, 2);
        sessionsListView.setLayoutManager(layoutManager);

        sessionsListView.setItemAnimator(new DefaultItemAnimator());
        sessionsListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        sessionsListView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(sessionsListView);

        sessionDialogFragment = new SessionDialogFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(adapter != null) {

            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_create_session:
                onAddItemClicks();
                break;
            case R.id.action_logout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.action_profil:

                startActivity(new Intent(this, ProfilActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAddItemClicks() {
        sessionDialogFragment.show(getSupportFragmentManager(), SessionDialogFragment.TAG);
    }

    @Override
    public void onItemClick(View view, int position) {

        final Sessions sessions = adapter.getSnapshot(position).toObject(Sessions.class);

        String sessionId =  adapter.getSnapshot(position).getId();
        Bundle bundle = new Bundle();
        bundle.putString("idSession", sessionId);

//        ((ConstraintLayout) findViewById(R.id.constraintLayout)).removeAllViews();

        fm = getSupportFragmentManager();
        fragment = new Action_session_Fragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().addToBackStack(fragment.toString())
                .replace(R.id.constraintLayout, fragment)
                .commit();

        Log.d(TAG, "" +   adapter.getSnapshot(position).getId());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof  SessionsListAdapter.SessionsItemHolder) {
            final DocumentSnapshot documentSnapshot = adapter.getSnapshot(position);
            final Sessions sessions = documentSnapshot.toObject(Sessions.class);


            firebaseFirestore.collection(SESSIONS).document(documentSnapshot.getId()).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Item delete");
                        }
                    })
                    .addOnFailureListener(this);

            Snackbar snackbar = Snackbar.make(constraintLayout, sessions.getNom() + " removed from cart! ", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDocument(documentSnapshot.getId(), sessions);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public void setDocument(String docId, Sessions sessions) {
        firebaseFirestore.collection(SESSIONS).document(docId).set(sessions).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document Undo Successfully");
                    }
                }).
                addOnFailureListener(this);
    }

    @Override
    public void onCreateSession(Sessions sessions) {

        firebaseFirestore.collection(SESSIONS).add(sessions)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Document successfully add");
                    }
                })
                .addOnFailureListener(this);
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
            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
