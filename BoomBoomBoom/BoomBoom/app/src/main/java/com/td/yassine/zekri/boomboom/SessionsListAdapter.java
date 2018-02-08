package com.td.yassine.zekri.boomboom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Zekri on 08/02/2018.
 */

public class SessionsListAdapter extends RecyclerView.Adapter<SessionsListAdapter.ViewHolder> {


    public List<Sessions> sessionsList;

    public SessionsListAdapter(List<Sessions> sessionsList) {

        this.sessionsList = sessionsList;
    }


    @Override
    public SessionsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_start_session_, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionsListAdapter.ViewHolder holder, int position) {

        holder.nameText.setText(sessionsList.get(position).getNom());
        holder.dateText.setText(sessionsList.get(position).getDate());
        holder.heureText.setText(sessionsList.get(position).getHeure());
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
