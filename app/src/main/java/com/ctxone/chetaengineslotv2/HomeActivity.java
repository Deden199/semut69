package com.ctxone.chetaengineslotv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String SELECTED_OPTION = "selectedOption";
    private SharedPreferences sharedPreferences;
    private RadioGroup radioGroup;
    private Button daftarButton;
    private ImageView logodalem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        getSupportActionBar().hide();
        logodalem = findViewById(R.id.logobull);
        Button button = findViewById(R.id.aktifk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedOption = sharedPreferences.getString(SELECTED_OPTION, "");
                Intent intent = new Intent(HomeActivity.this, AutoSpin.class);
                intent.putExtra(SELECTED_OPTION, selectedOption);
                startActivity(intent);
                finish();            }
        });


        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        radioGroup = findViewById(R.id.radioGroup);
        daftarButton = findViewById(R.id.aktifk);
        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedOption = sharedPreferences.getString(SELECTED_OPTION, "");
                Intent intent = new Intent(HomeActivity.this, AutoSpin.class);
                intent.putExtra(SELECTED_OPTION, selectedOption);
                startActivity(intent);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    String selectedOption = selectedRadioButton.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SELECTED_OPTION, selectedOption);
                    editor.apply();
                }
            }
        });

        String savedOption = sharedPreferences.getString(SELECTED_OPTION, "");
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View view = radioGroup.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.getText().toString().equals(savedOption)) {
                    radioButton.setChecked(true);
                    break;
                }
            }
        }
    }


}
