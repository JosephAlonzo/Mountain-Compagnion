package com.td.yassine.zekri.boomboom.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.td.yassine.zekri.boomboom.Account.MainActivity;
import com.td.yassine.zekri.boomboom.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Create_session_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Create_session_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Create_session_fragment extends Fragment implements View.OnClickListener, Show_session_Fragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View root;

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore mFirestore;

    private TextView textViewNomUser;
    private EditText editTextNomSession;

    private Button buttonConfirm;

    private TextView textViewChooseDate;
    private TextView textViewChooseHour;

    private Calendar myCalendar;

    private FragmentManager fm = null;
    private Fragment fragment = null;

    public Create_session_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Create_session_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Create_session_fragment newInstance(String param1, String param2) {
        Create_session_fragment fragment = new Create_session_fragment();
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
        root = inflater.inflate(R.layout.fragment_create_session, container, false);



        editTextNomSession = root.findViewById(R.id.editTextNomSession);
        textViewNomUser = root.findViewById(R.id.textViewNomUser);
        buttonConfirm = root.findViewById(R.id.buttonConfirm);

        textViewChooseDate = root.findViewById(R.id.textViewChooseDate);
        textViewChooseHour = root.findViewById(R.id.textViewChooseHour);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() == null){

            getActivity().finish();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user.getDisplayName() != null){
            textViewNomUser.setText("Bienvenue " + user.getDisplayName());
        }


        buttonConfirm.setOnClickListener(this);

        /***    Date et Heure  ***/
        textViewChooseDate.setOnClickListener(this);
        textViewChooseHour.setOnClickListener(this);

        myCalendar = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {

                myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                myCalendar.set(Calendar.MINUTE, min);
                updateLabelHour();
            }
        };
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabelDate();
            }
        };

        textViewChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        textViewChooseHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new TimePickerDialog(getContext(),time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        return root;
    }

    private void updateLabelHour() {

        textViewChooseHour.setText(myCalendar.get(Calendar.HOUR_OF_DAY)+":"+myCalendar.get(Calendar.MINUTE));
    }
    private void updateLabelDate() {

        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textViewChooseDate.setText(sdf.format(myCalendar.getTime()));
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
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.buttonConfirm :

                createSession();
                break;
        }
    }

    private void createSession() {

        String hour = textViewChooseHour.getText().toString().trim();
        String date = textViewChooseDate.getText().toString().trim();
        String name = editTextNomSession.getText().toString().trim();

        Map<String, String> sessionMap = new HashMap<>();

        sessionMap.put("Nom", name);
        sessionMap.put("Heure", hour);
        sessionMap.put("Date", date);

        mFirestore.collection("sessions").add(sessionMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(getContext(), "Confirmed, session created !",Toast.LENGTH_SHORT).show();

                fm = getActivity().getSupportFragmentManager();
                fragment = new Show_session_Fragment();
                fm.beginTransaction().addToBackStack(fragment.toString())
                        .replace(R.id.testView, fragment)
                        .commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String error = e.getMessage();

                Toast.makeText(getContext(), "Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });

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
