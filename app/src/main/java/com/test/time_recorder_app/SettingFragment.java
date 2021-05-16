package com.test.time_recorder_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class SettingFragment extends Fragment {

    private View view;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.title_setting));

        createList();

        return view;
    }

    private void createList() {
        // ListViewに表示するリスト項目をArrayListで準備する
        ArrayList arrayItems = new ArrayList<>();
        arrayItems.add(getString(R.string.title_manager_menu));
        arrayItems.add(getString(R.string.title_help));
        arrayItems.add(getString(R.string.title_inquiry));
        arrayItems.add(getString(R.string.title_app_information));
        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        final ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, arrayItems);
        // ListViewにArrayAdapterを設定する
        ListView list = view.findViewById(R.id.setting_f_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, v, position, id) -> {
            switch (position) {
                case 0:
                    // 管理者メニュー
                    createCheckPasswordDialog();
                    break;
                case 1:
                    // ヘルプ
                    Intent intentHelp = new Intent(getActivity(), HelpActivity.class);
                    startActivity(intentHelp);
                    break;
                case 2:
                    // お問い合わせ
                    activateMailer();
                    break;
                case 3:
                    // アプリについて
                    Intent intentAppInformation = new Intent(getActivity(), AppInformationActivity.class);
                    startActivity(intentAppInformation);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        });
    }

    public void createCheckPasswordDialog() {
        editText = new EditText(getActivity());
        editText.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setHint(getString(R.string.hint_password));
        editText.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkPassword();
                return true;
            }
            return false;
        });
        editText.requestFocus();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setIcon(android.R.drawable.ic_lock_lock)
            .setTitle(getString(R.string.label_access_permission))
            .setMessage(getString(R.string.label_input_password))
            .setView(editText)
            .setNegativeButton(getString(R.string.label_cancel),null)
            .setPositiveButton(getString(R.string.label_ok),
                    (dialog, which) -> checkPassword()
            ).show();
    }

    public void checkPassword() {
        String pwdTxt = editText.getText().toString();

        // load the password
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("PREFS", 0);
        String password = sharedPreferences.getString("password", "");
        if (pwdTxt.equals(password)){
            //管理者メニューへ
            Intent intentManager = new Intent(getActivity(), ManagerActivity.class);
            startActivity(intentManager);
        } else {
            Toast.makeText(getActivity(), getString(R.string.label_wrong_password) , Toast.LENGTH_SHORT).show();
        }
    }

    private void activateMailer() {
        Intent intentMailer = new Intent();
        intentMailer.setAction(Intent.ACTION_SEND);
        intentMailer.setType("message/rfc822");
        intentMailer.putExtra(Intent.EXTRA_EMAIL, new String[]{"javamailer667@gmail.com"});
        intentMailer.putExtra(Intent.EXTRA_SUBJECT,"SimpleTimeCard：" + getString(R.string.title_inquiry));
        intentMailer.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.label_name) +"\n\n" + getString(R.string.setting_f_label_information_here) + "\n\n\n\n"
                        + "Device : " + Build.BRAND+" "+ Build.MODEL
                        + "\nOS version : " + Build.VERSION.RELEASE
                        + "\nApp : SimpleTimeCard"
                        + "\nVersion : " + BuildConfig.VERSION_NAME);

        startActivity(intentMailer);
    }
}