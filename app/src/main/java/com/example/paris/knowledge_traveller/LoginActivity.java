package com.example.paris.knowledge_traveller;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
        if(isLoggedIn){
            startActivity(intent);
        }

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
        if(isLoggedIn){
            startActivity(intent);
        }

    }

}
