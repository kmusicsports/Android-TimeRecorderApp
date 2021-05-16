package com.test.time_recorder_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class DialogSortFragment extends DialogFragment {

    private View view;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_sort, null);

        clickRadio();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
            .setView(view)
            .create();
    }

    @SuppressLint("NonConstantResourceId")
    private void clickRadio() {
        RadioGroup radioGroup = view.findViewById(R.id.sort_d_radio_group);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String order = "date ASC";
            switch(checkedId) {
                case R.id.sort_d_radio_button_asc:
                    order = "date ASC";
                    break;
                case R.id.sort_d_radio_button_desc:
                    order = "date DESC";
                    break;
                default:
                    Toast.makeText(getActivity(), "Error(sort_d)", Toast.LENGTH_SHORT).show();
            }
            Bundle args = new Bundle();
            args.putString("name", null);
            args.putString("startDate", null);
            args.putString("endDate", null);
            args.putString("order", order);
            DisplayRecordsFragment fragment = new DisplayRecordsFragment();
            fragment.setArguments(args);
            setFragment(fragment);
        });
    }

    public void setFragment(Fragment fragment){
        FragmentManager manager = Objects.requireNonNull(getFragmentManager());
        manager.beginTransaction()
            .replace(R.id.main_a_layout, fragment)
            .commit();
    }
}
