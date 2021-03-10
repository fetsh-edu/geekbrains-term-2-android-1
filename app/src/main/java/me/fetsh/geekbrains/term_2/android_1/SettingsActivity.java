package me.fetsh.geekbrains.term_2.android_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class SettingsActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.settings);

        TextView selectedTheme = findViewById(R.id.theme_selected);
        selectedTheme.setText(nightModeToString(getSavedTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)));

        findViewById(R.id.theme_selector).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.setOnMenuItemClickListener(this::nightModeSelected);
            popup.inflate(R.menu.theme_menu);
            popup.getMenu().findItem(nightModeToItemId(getSavedTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM))).setChecked(true);
            popup.show();
        });
    }

    private boolean nightModeSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dark_theme:
                setAndSaveNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                item.setChecked(!item.isChecked());
                return true;
            case R.id.light_theme:
                setAndSaveNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                item.setChecked(!item.isChecked());
                return true;
            case R.id.system_theme:
                setAndSaveNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                item.setChecked(!item.isChecked());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAndSaveNightMode(int modeNight) {
        ((TextView) findViewById(R.id.theme_selected)).setText(nightModeToString(modeNight));
        AppCompatDelegate.setDefaultNightMode(modeNight);
        SharedPreferences sharedPref = getSharedPreferences(AppActivity.PREF_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(AppActivity.MODE_NIGHT_KEY, modeNight);
        editor.apply();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String nightModeToString(final int nightMode) {
        String result;
        switch (nightMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                result = getResources().getString(R.string.light_theme);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                result = getResources().getString(R.string.dark_theme);
                break;
            default:
                result = getResources().getString(R.string.system_theme);
        };
        return result;
    }
    private int nightModeToItemId(final int nightMode) {
        int result;
        switch (nightMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                result = R.id.light_theme;
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                result = R.id.dark_theme;
                break;
            default:
                result = R.id.system_theme;
        };
        return result;
    }
}