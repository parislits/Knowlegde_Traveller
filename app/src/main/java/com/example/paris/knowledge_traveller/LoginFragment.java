package com.example.paris.knowledge_traveller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import static android.app.Activity.RESULT_OK;


public class LoginFragment extends Fragment {

    private CallbackManager mCallbackManager;


    public LoginFragment() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mCallbackManager = CallbackManager.Factory.create();

        final LoginButton mLoginButton = view.findViewById(R.id.login_button);

        // Set the initial permissions to request from the user while logging in
        //αλλα δεν το χρησιμοποιουμαι καπου
       // mLoginButton.setReadPermissions(Arrays.asList(EMAIL));

        mLoginButton.setFragment(this); //Χρειαζεται για να βαλουμε το κουμπί της συνδεσης σε fragment

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(!isLoggedIn){
            mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // Ο χρηστης εχει συνδεθει επιτυχως
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);



                }

                @Override
                public void onCancel() {
                    // Ο χρηστης ακυρωσε την εισοδο του
                }

                @Override
                public void onError(FacebookException exception) {
                    // Παρουσιαστηκε καποιο σφαλμα
                }
            });
        }
        else{
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

}
