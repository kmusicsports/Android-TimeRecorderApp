package com.test.time_recorder_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ajts.androidmads.library.SQLiteToExcel;

import java.util.ArrayList;
import java.util.Date;

public class DialogExportFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_export, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setTitle(getString(R.string.title_export))
                .setView(view)
                .setNegativeButton(getString(R.string.label_cancel), null)
                .setPositiveButton(getString(R.string.label_send),
                        (dialog, which) -> {
                            String mailAddress = ((EditText) view.findViewById(R.id.export_d_edit_text)).getText().toString();

                            convertSQLiteToExcel();

                            AsyncTask at = new AsyncTask(mailAddress);
                    //        at.execute("Gmailのアカウント名（@gmail.comの前まで）",
                    //                "アプリパスワード","テストタイトル","送信完了\n本文をここに記述する") ;
                            at.execute("javamailer667", "jyzyztppywqvbcmf", "SimpleTimeCard", "Message");
                            Toast.makeText(getActivity(), getString(R.string.export_d_label_sending_completed),Toast.LENGTH_LONG).show();
                        })
                .create();
    }

    private void convertSQLiteToExcel() {
        @SuppressLint("SdCardPath") String filesDirectoryPath = "/data/data/com.test.time_recorder_app/";
        ArrayList<String> columnsToExclude = new ArrayList<>();
        columnsToExclude.add("_id");
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getActivity(), "Database.db", filesDirectoryPath);
        sqliteToExcel.setExcludeColumns(columnsToExclude);
        sqliteToExcel.exportSingleTable("recordtable", "table1.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onCompleted(String filePath) {
//                                        Toast.makeText(getActivity(), "Successfully Exported.", Toast.LENGTH_LONG).show();
                Log.d("Successfully Exported.", new Date().toString());

            }
            @Override
            public void onError(Exception e) {
//                                        Toast.makeText(getActivity(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Error(export_d) : " + e.getMessage(), new Date().toString());
            }
        });
    }

//    private class asyncTask extends android.os.AsyncTask{
//        protected String account;
//        protected String password;
//        protected String title;
//        protected String text;
//
//        @Override
//        protected Object doInBackground(Object... obj){
//            account=(String)obj[0];
//            password=(String)obj[1];
//            title=(String)obj[2];
//            text=(String)obj[3];
//
//            java.util.Properties properties = new java.util.Properties();
//            properties.put("mail.smtp.host", "smtp.gmail.com");
//            properties.put("mail.smtp.auth", "true");
//            properties.put("mail.smtp.port", "465");
//            properties.put("mail.smtp.socketFactory.post", "465");
//            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//
//            final javax.mail.Message msg = new javax.mail.internet.MimeMessage(javax.mail.Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
//                @Override
//                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//                    return new javax.mail.PasswordAuthentication(account,password);
//                }
//            }));
//
//            try {
//                msg.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
//                msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(account + "@gmail.com")); //自分自身にメールを送信
////                msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(address)); // 指定アドレスにメールを送信
//                msg.setSubject(title);
//                msg.setText(text);
//
//                MimeBodyPart mbp = new MimeBodyPart();
//                @SuppressLint("SdCardPath") File attachmentFile = new File("/data/data/com.test.time_recorder_app/files/table1.xls");
//                FileDataSource fds = new FileDataSource(attachmentFile);
//                mbp.setDataHandler(new DataHandler(fds));
//                mbp.setFileName(MimeUtility.encodeWord(fds.getName()));
//
//                Multipart mp = new MimeMultipart();
//                mp.addBodyPart(mbp);
//
//                msg.setContent(mp);
//
//                javax.mail.Transport.send(msg);
//
//            } catch (Exception e) {
//                return (Object)e.toString();
//            }
//
//            return (Object)"送信が完了しました";
//
//        }
//        @Override
//        protected void onPostExecute(Object obj) {
//            //画面にメッセージを表示する
//            Toast.makeText(getActivity(),(String)obj,Toast.LENGTH_LONG).show();
//        }
//    }
}
