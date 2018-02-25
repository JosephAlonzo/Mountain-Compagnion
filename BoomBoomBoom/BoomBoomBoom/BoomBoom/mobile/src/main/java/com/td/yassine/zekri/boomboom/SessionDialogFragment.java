package com.td.yassine.zekri.boomboom;

//import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SessionDialogFragment extends DialogFragment {

    public static final String TAG = "SessionsDialog";

    @BindView(R.id.nomSession)
    EditText editTextNomSession;
    @BindView(R.id.textViewChooseDate)
    TextView textViewChooseDate;
    @BindView(R.id.textViewChooseHour)
    TextView textViewChooseHour;

    SessionsListener sessionsListener;

    private Calendar myCalendar;

    private TimePickerDialog.OnTimeSetListener time;
    private DatePickerDialog.OnDateSetListener date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_session_dialog, container, false);
        ButterKnife.bind(this, view);

        myCalendar = Calendar.getInstance();

        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabelHour();
            }
        };

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate();
            }
        };

        return view;
    }

    private void updateLabelHour() {

        textViewChooseHour.setText(myCalendar.get(Calendar.HOUR_OF_DAY)+":"+myCalendar.get(Calendar.MINUTE));
    }

    private void updateLabelDate() {

        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        textViewChooseDate.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if (context instanceof SessionsListener) {

            sessionsListener = (SessionsListener) context;
        }
    }

    @OnClick(R.id.textViewChooseHour)
    public void chooseHour(View view){
        new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
    }

    @OnClick(R.id.textViewChooseDate)
    public void chooseDate(View view) {

        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(TAG, "onDismiss");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @OnClick(R.id.buttonCreateSession)
    public void onCreateSession(View view) {

        String nomSession = editTextNomSession.getText().toString().trim();
        String hourSession = textViewChooseHour.getText().toString().trim();
        String dateSession = textViewChooseDate.getText().toString().trim();
        if (!TextUtils.isEmpty(nomSession) && !hourSession.equals("Click here to choose an hour") && !dateSession.equals("Click here to choose a date")) {
            Sessions sessions = new Sessions(nomSession, dateSession, hourSession);
            sessionsListener.onCreateSession(sessions);
            editTextNomSession.setText("");
            textViewChooseDate.setText("Click here to choose a date");
            textViewChooseHour.setText("Click here to choose an hour");
            dismiss();
        }else {
            Toast.makeText(getActivity(), "Please, Enter proper Values !", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.buttonCancel)
    public void onCancel(View view) {
        dismiss();
    }

    public interface SessionsListener {
        void onCreateSession(Sessions sessions);
    }


}
