package com.yhsh.flowstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.yhsh.flowstudy.view.QuickIndexBar;

public class QuickSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search);
        QuickIndexBar quickIndexBar = findViewById(R.id.quick_index_bar);
        quickIndexBar.setOnLetterChangeListener(letter -> {
            Toast.makeText(getApplicationContext(), letter, Toast.LENGTH_SHORT).show();
        });
    }
}