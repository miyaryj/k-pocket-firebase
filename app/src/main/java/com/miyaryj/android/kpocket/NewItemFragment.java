package com.miyaryj.android.kpocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miyaryj.android.kpocket.model.Item;

import java.util.HashMap;
import java.util.Map;

public class NewItemFragment extends Fragment {
    public static final String TAG = NewItemFragment.class.getSimpleName();

    private static final String KEY_INTENT = "key_intent";

    private DatabaseReference mDatabase;

    public NewItemFragment() {
    }

    public static NewItemFragment newInstance(Intent intent) {
        NewItemFragment fragment = new NewItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_INTENT, intent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getArguments().getParcelable(KEY_INTENT);
        if (intent != null) {
            Item item = new Item(intent.getStringExtra(Intent.EXTRA_SUBJECT), intent.getStringExtra(Intent.EXTRA_TEXT));
            pushItem(item);
        }

        getFragmentManager().popBackStack();
    }

    private void pushItem(Item item) {
        String key = mDatabase.child("items").push().getKey();
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("/items/" + key, item.toMap());
        updates.put("/user-items/" + getUid() + "/" + key, item.toMap());
        mDatabase.updateChildren(updates);
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
