package com.test.time_recorder_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class DialogTermsOfUseFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_terms_of_use, null);

        WebView web = view.findViewById(R.id.terms_d_web);
        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.JAPAN)) {
            // 日本語環境
            web.loadUrl("file:///android_asset/index_ja.html");
        } else {
            // その他の言語環境
            web.loadUrl("file:///android_asset/index.html");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setView(view)
                .setNegativeButton(getString(R.string.label_close), null)
                .create();
    }
}
