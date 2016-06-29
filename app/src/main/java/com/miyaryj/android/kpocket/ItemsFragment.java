package com.miyaryj.android.kpocket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.miyaryj.android.kpocket.model.Item;


public class ItemsFragment extends Fragment {
    public static final String TAG = ItemsFragment.class.getSimpleName();

    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView;

    private FirebaseRecyclerAdapter mAdapter;

    public ItemsFragment() {
    }

    public static ItemsFragment newInstance() {
        ItemsFragment fragment = new ItemsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView)inflater.inflate(R.layout.fragment_items, container, false);
        mRecyclerView.setHasFixedSize(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return mRecyclerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query itemsQuery = mDatabase.child("user-items").child(getUid()).limitToLast(100);
        mAdapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(Item.class, R.layout.listitem_item, ItemViewHolder.class, itemsQuery) {
            @Override
            protected void populateViewHolder(final ItemViewHolder viewHolder, final Item model, final int position) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(model.getUri()));
                        startActivity(intent);
                    }
                });

                viewHolder.bindToItem(model);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;

        public ItemViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        public void bindToItem(Item item) {
            mIdView.setText(item.getTitle());
            mContentView.setText(item.getUri());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
