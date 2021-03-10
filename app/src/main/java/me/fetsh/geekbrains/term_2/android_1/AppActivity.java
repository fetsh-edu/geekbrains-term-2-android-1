package me.fetsh.geekbrains.term_2.android_1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AppActivity extends AppCompatActivity {

    public static final String PREF_KEY = "MainPref";
    public static final String MODE_NIGHT_KEY = "ModeNight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected int getSavedTheme(final int defaultTheme) {
        return getSharedPreferences(PREF_KEY, MODE_PRIVATE)
                .getInt(MODE_NIGHT_KEY, defaultTheme);
    }

    protected void applySavedTheme(final int defaultTheme) {
        AppCompatDelegate.setDefaultNightMode(
                getSavedTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        );
    }
}
