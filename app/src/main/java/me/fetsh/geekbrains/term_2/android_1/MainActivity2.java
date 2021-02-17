package me.fetsh.geekbrains.term_2.android_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    private Intent intentFirstActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        intentFirstActivity = new Intent(this, MainActivity.class);

        Button button = findViewById(R.id.activity_button);
        button.setOnClickListener((v) -> startActivity(intentFirstActivity));


    }
}