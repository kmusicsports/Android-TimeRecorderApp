package com.test.time_recorder_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class DialogClothingDateFragment extends DialogFragment {

    private SharedPreferences sharedPreferences;
    private Spinner spinnerDay;
    private String clothingDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_clothing_date, null);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("PREFS", 0);
        clothingDate = sharedPreferences.getString("deadline", "5");

        TextView textClothingDate = view.findViewById(R.id.cd_d_text_clothing_date);
        textClothingDate.setText(clothingDate);

        spinnerDay = view.findViewById(R.id.cd_d_spinner);
        getDaySpinnerText();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(getString(R.string.title_closing_date))
                .setView(view)
                .setNegativeButton(getString(R.string.label_cancel), null)
                .setPositiveButton(getString(R.string.label_change),
                        (dialog, which) -> changeClothingDate()
                )
                .create();
    }

    public void getDaySpinnerText() {
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                clothingDate = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void changeClothingDate() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deadline", clothingDate);
        editor.apply();

        Toast.makeText(getActivity(), getString(R.string.label_changed), Toast.LENGTH_SHORT).show();
        Intent intentSalaryCalculation = new Intent(getActivity(), SalaryCalculationActivity.class);
        startActivity(intentSalaryCalculation);
    }

}
