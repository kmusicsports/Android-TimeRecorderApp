package com.test.time_recorder_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        TextView text = findViewById(R.id.cp_a_text_terms);
        text.setOnClickListener(view -> {
            DialogFragment dialog = new DialogTermsOfUseFragment();
            dialog.show(getSupportFragmentManager(), "dialog_terms_of_use");
        });

        Button button = findViewById(R.id.cp_a_button);
        button.setOnClickListener(view -> {
            clickStart();
        });
    }

    private void clickStart() {
        EditText editTextPassword = findViewById(R.id.cp_a_et_password);
        EditText editTextPasswordAgain = findViewById(R.id.cp_a_et_password_again);
        String password = editTextPassword.getText().toString();
        String passwordAgain = editTextPasswordAgain.getText().toString();

        if(!((CheckBox) findViewById(R.id.cp_a_check_box)).isChecked()) {
            // CheckBox is not checked
            Toast.makeText(CreatePasswordActivity.this, getString(R.string.cp_a_label_not_checked), Toast.LENGTH_SHORT).show();
        } else if(password.equals("") || passwordAgain.equals("")) {
            // there is no password
            Toast.makeText(CreatePasswordActivity.this, getString(R.string.label_no_password), Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(passwordAgain)) {
                // save the password
                SharedPreferences sharedPreferences = getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password", password);
                editor.apply();

                // enter the app
                Intent intentMain = new Intent(CreatePasswordActivity.this, MainActivity.class);
                startActivity(intentMain);
            } else {
                // there is no match on the passwords
                Toast.makeText(CreatePasswordActivity.this, getString(R.string.label_no_match), Toast.LENGTH_SHORT).show();
            }
        }
    }
}