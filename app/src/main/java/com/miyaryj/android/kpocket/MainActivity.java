package com.miyaryj.android.kpocket;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final int RC_AUTH = 10;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                String message = getString(R.string.message_signed_in, user.getDisplayName());
                Snackbar.make(findViewById(R.id.activity_main), message, Snackbar.LENGTH_LONG).show();
                showItemsFragment();

                Intent intent = getIntent();
                if (intent != null && Intent.ACTION_SEND.equals(intent.getAction())) {
                    showNewItemFragment(intent);
                    setIntent(null);
                }
            } else {
                // User is signed out
                Snackbar.make(findViewById(R.id.activity_main), R.string.message_signed_out, Snackbar.LENGTH_LONG).show();
                startActivityForResult(new Intent(MainActivity.this, AuthActivity.class), RC_AUTH);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_AUTH) {
            if (resultCode != Activity.RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_signout:
                mAuth.signOut();
                return true;
        }
        return false;
    }

    private void showItemsFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(ItemsFragment.TAG) == null) {
            Fragment f = ItemsFragment.newInstance();
            fm.beginTransaction().replace(R.id.fragment_container, f, ItemsFragment.TAG).commit();
        }
    }

    private void showNewItemFragment(Intent intent) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(NewItemFragment.TAG) == null) {
            Fragment f = NewItemFragment.newInstance(intent);
            fm.beginTransaction().add(f, NewItemFragment.TAG).commit();
        }
    }
}
