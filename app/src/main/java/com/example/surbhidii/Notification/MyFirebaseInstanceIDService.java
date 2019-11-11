package com.example.surbhidii.Notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
private String refreshedToken;
Context mContext;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                refreshedToken = instanceIdResult.getToken();
                sendRegistrationToServer(refreshedToken);
                storeRegIdInPref(refreshedToken);

                // sending reg id to your server

                // Notify UI that registration has completed, so the progress indicator can be hidden.

            }
        });


    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        //add code here
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = mContext.getSharedPreferences(config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("rohit" +
                "kumar", token);
        editor.apply();
    }


}
