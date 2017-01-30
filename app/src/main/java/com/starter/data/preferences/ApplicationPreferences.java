package com.starter.data.preferences;

import android.content.SharedPreferences;

public class ApplicationPreferences {
    private static final String KEY_SENT_GCM_TOKEN_TO_SERVER = "KEY_SENT_GCM_TOKEN_TO_SERVER";

    SharedPreferences sharedPreferences = null;

    public ApplicationPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
}