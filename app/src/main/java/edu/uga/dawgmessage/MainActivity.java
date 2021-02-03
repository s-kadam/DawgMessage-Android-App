package edu.uga.dawgmessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
/*
This program created by Preston Lowry and Shubham Kadam
for Kochut's Mobile App Development Class
May 2nd, 2019
 */
public class MainActivity extends AppCompatActivity {
    Button logButton, createButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logButton = findViewById(R.id.logButton);
        createButton = findViewById(R.id.createButton);

        //Makes the buttons work

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

    }
}
