package com.td.yassine.zekri.boomboom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.td.yassine.zekri.boomboom.fragments.Display_session_Fragment;
import com.td.yassine.zekri.boomboom.fragments.Show_session_Fragment;

import java.util.List;

/**
 * Created by Zekri on 08/02/2018.
 */

public class SessionsListAdapter extends RecyclerView.Adapter<SessionsListAdapter.ViewHolder> {


    public List<Sessions> sessionsList;
    public Context context;

    private FragmentManager fm = null;
    private Fragment fragment = null;

    private FragmentActivity myContext;

    private FirebaseFirestore mFirestore;

    public SessionsListAdapter(Context context, List<Sessions> sessionsList) {

        this.sessionsList = sessionsList;
        this.context = context;
        this.myContext = (FragmentActivity) context;
    }


    @Override
    public SessionsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_start_session_, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionsListAdapter.ViewHolder holder, int position) {

        holder.nameText.setText(sessionsList.get(position).getNom());
        holder.dateText.setText("Date: " + sessionsList.get(position).getDate());
        holder.heureText.setText("Heure: " + sessionsList.get(position).getHeure());

        final String user_id = sessionsList.get(position).sessionId;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("idSession", user_id);

                fm = myContext.getSupportFragmentManager();
                fragment = new Display_session_Fragment();
                fragment.setArguments(bundle);
                fm.beginTransaction().addToBackStack(fragment.toString())
                        .replace(R.id.testView, fragment)
                        .commit();
//                Toast.makeText(context, "Id: " + user_id, Toast.LENGTH_SHORT).show();
            }
        });

//        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                mFirestore = FirebaseFirestore.getInstance();
//
//                mFirestore.collection("sessions").document(user_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        Toast.makeText(context, "Session deleted !", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return sessionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView nameText;
        public TextView dateText;
        public TextView heureText;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            nameText = mView.findViewById(R.id.textViewNom);
            dateText = mView.findViewById(R.id.textViewDate);
            heureText = mView.findViewById(R.id.textViewHeure);
        }
    }

}
