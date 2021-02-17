package me.fetsh.geekbrains.term_2.android_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Intent intentSecondActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentSecondActivity = new Intent(this, MainActivity2.class);

        Button button = findViewById(R.id.activity_button);
        button.setOnClickListener((v) -> startActivity(intentSecondActivity));

    }
}