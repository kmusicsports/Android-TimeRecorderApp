package com.test.time_recorder_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class DialogChangePasswordFragment extends DialogFragment {

    private EditText editTextPassword;
    private EditText editTextPasswordAgain;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        editTextPassword = view.findViewById(R.id.cp_edit_text_password);
        editTextPasswordAgain = view.findViewById(R.id.cp_edit_text_password_again);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        editTextPasswordAgain.setOnEditorActionListener((text, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                changePassword();
                return true;
            }
            return false;
        });

        return builder
                .setTitle(getString(R.string.title_change_password))
                .setView(view)
                .setNegativeButton(getString(R.string.label_cancel), null)
                .setPositiveButton(getString(R.string.label_change),
                        (dialog, which) -> changePassword()
                )
                .create();

    }

    public void changePassword () {
        String password = editTextPassword.getText().toString();
        String passwordAgain = editTextPasswordAgain.getText().toString();
        if(password.equals("") || passwordAgain.equals("")) {
            // there is no password
            Toast.makeText(getActivity(), getString(R.string.label_no_password), Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(passwordAgain)) {
                // save the password
                SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("PREFS", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password", password);
                editor.apply();

                Toast.makeText(getActivity(), getString(R.string.label_changed), Toast.LENGTH_SHORT).show();
                Intent intentManager = new Intent(getActivity(), ManagerActivity.class);
                startActivity(intentManager);
            } else {
                // there is no match on the passwords
                Toast.makeText(getActivity(), getString(R.string.label_no_match), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
