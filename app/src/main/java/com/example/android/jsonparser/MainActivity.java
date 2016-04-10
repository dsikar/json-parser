package com.example.android.jsonparser;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private Button mJsonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            JsonFragment fragment = new JsonFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

    }



}
