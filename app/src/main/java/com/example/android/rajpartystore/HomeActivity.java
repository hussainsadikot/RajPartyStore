package com.example.android.rajpartystore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //We will Implement LogOut Button at that time we also remove the user credential stored in Paper library
        //Commands to Remove credential from Paper is "Paper.book().destroy();" this will remove from phone memory
    }
}
