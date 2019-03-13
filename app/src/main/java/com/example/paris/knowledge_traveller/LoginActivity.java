package com.example.paris.knowledge_traveller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.AccessToken;

public class LoginActivity extends AppCompatActivity {
        private Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Ελεγχος για να δουμε αν ειναι συνδεδεμενος ηδη ο χρηστης απο προηγουμενη εισοδο στην εφαρμογη
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Intent intent = new Intent(this,MainActivity.class);
        //Αν ειναι ,τοτε να συνδεθει απευθειας στην mainActivity

            startActivity(intent);


    }

    @Override
    public void onResume()
    {
        super.onResume();

        //Ελεγχος για να δουμε αν ειναι συνδεδεμενος ηδη ο χρηστης απο προηγουμενη εισοδο στην εφαρμογη
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Intent intent = new Intent(this,MainActivity.class);
        //Αν ειναι ,τοτε να συνδεθει απευθειας στην mainActivity

            startActivity(intent);


    }

}
