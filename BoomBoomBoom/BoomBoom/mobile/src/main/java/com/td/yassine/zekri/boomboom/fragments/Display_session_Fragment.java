package com.td.yassine.zekri.boomboom.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.td.yassine.zekri.boomboom.R;
import com.td.yassine.zekri.boomboom.Sessions;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Display_session_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Display_session_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Display_session_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View root;

    private FirebaseFirestore mFirestore;


    private TextView textViewNomSession;
    private TextView textViewDateSession;
    private TextView textViewHeureSession;
    private Button buttonStartSession;

    private String nomSession;
    private String idSession;

    private FragmentManager fm = null;
    private Fragment fragment = null;

    public Display_session_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Display_session_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Display_session_Fragment newInstance(String param1, String param2) {
        Display_session_Fragment fragment = new Display_session_Fragment();
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
        root = inflater.inflate(R.layout.fragment_display_session_, container, false);

        textViewDateSession = root.findViewById(R.id.textViewDateSession);
        textViewHeureSession = root.findViewById(R.id.textViewHeureSession);
        textViewNomSession = root.findViewById(R.id.textViewNomSession);
        buttonStartSession = root.findViewById(R.id.buttonStartSession);

        mFirestore = FirebaseFirestore.getInstance();

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            idSession = bundle.getString("idSession", "ErrorSession");
            DocumentReference docRef = mFirestore.collection("sessions").document(idSession);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())  {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {

                            nomSession = task.getResult().getString("Nom");

                            textViewDateSession.setText("Le " + task.getResult().getString("Date"));
                            textViewHeureSession.setText(task.getResult().getString("Heure"));
                            textViewNomSession.setText(task.getResult().getString("Nom"));

                            Log.d("DEBUGG", "DocumentSnapshot data: " + task.getResult().getData());
                        }else {
                            Log.d("DEBUGG", "NO SUCH DOCUMENT");
                        }
                    } else {
                        Log.d("DEBUGG", "FAILED with: ", task.getException());
                    }
                }
            });

        }

        buttonStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleNom = new Bundle();

                bundleNom.putString("nomSession", nomSession);

                fm = getActivity().getSupportFragmentManager();
                Fragment fragment = new Action_session_Fragment();
                fragment.setArguments(bundleNom);
                fm.beginTransaction().addToBackStack(fragment.toString())
                        .replace(R.id.testView, fragment)
                        .commit();
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
