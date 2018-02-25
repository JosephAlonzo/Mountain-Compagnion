package com.td.yassine.zekri.boomboom.adapterUsers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.td.yassine.zekri.boomboom.R;
import com.td.yassine.zekri.boomboom.adapter.ClickListener;
import com.td.yassine.zekri.boomboom.adapter.FirestoreAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zekri on 23/02/2018.
 */

public class UserListAdapter extends FirestoreAdapter<UserListAdapter.UserItemHolder> {

    ClickListener clickListener;

    public UserListAdapter(Query query, ClickListener clickListener) {
        super(query);
        this.clickListener = clickListener;
    }

    @Override
    public UserItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new UserItemHolder(layoutInflater.inflate(R.layout.item_user, parent, false), clickListener);
    }

    @Override
    public void onBindViewHolder(UserItemHolder holder, int position) {

        holder.bindData(getSnapshot(position));
    }

    public static class UserItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_foreground)
        public RelativeLayout viewForeground;
        @BindView(R.id.textViewNomUser)
        TextView textViewNomUser;
        @BindView(R.id.textViewFrequenceUser)
        TextView textViewFrequenceUser;


        public UserItemHolder(View itemView, final ClickListener clickListener) {
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

            User user = documentSnapshot.toObject(User.class);
            textViewNomUser.setText(user.getNom());
            textViewFrequenceUser.setText(user.getFrequence());
        }

    }
}
