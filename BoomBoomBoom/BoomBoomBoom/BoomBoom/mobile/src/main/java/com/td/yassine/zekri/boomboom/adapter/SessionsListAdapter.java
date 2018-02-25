package com.td.yassine.zekri.boomboom.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.td.yassine.zekri.boomboom.R;
import com.td.yassine.zekri.boomboom.Sessions;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Zekri on 23/02/2018.
 */

public class SessionsListAdapter extends FirestoreAdapter<SessionsListAdapter.SessionsItemHolder> {


    ClickListener clickListener;

    public SessionsListAdapter(Query query, ClickListener clickListener) {
        super(query);
        this.clickListener = clickListener;
    }

    @Override
    public SessionsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new SessionsItemHolder(layoutInflater.inflate(R.layout.item_session, parent, false), clickListener);
    }

    @Override
    public void onBindViewHolder(SessionsItemHolder holder, int position) {

        holder.bindData(getSnapshot(position));
    }

    public static class SessionsItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_foreground)
        public RelativeLayout viewForeground;
        @BindView(R.id.view_background)
        public RelativeLayout viewBackground;
        @BindView(R.id.textViewSessionName)
        TextView textViewSessionName;
        @BindView(R.id.textViewSessionHeure)
        TextView textViewSessionHeure;
        @BindView(R.id.textViewSessionDate)
        TextView textViewSessionDate;

        public SessionsItemHolder(View itemView, final ClickListener clickListener) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        public void bindData(DocumentSnapshot documentSnapshot) {

            Sessions session = documentSnapshot.toObject(Sessions.class);
            textViewSessionName.setText(session.getNom());
            textViewSessionHeure.setText("Heure: " + session.getHeure());
            textViewSessionDate.setText("Date: le " + session.getDate());
        }


    }
}
