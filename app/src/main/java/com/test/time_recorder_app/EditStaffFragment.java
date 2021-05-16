package com.test.time_recorder_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EditStaffFragment extends Fragment {

    private View view;
    private SimpleOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_staff, container, false);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.title_delete_multiple));

        createStaffList();

        clickDelete();

        return view;
    }

    public void createStaffList(){
        ArrayList<HashMap<String, String>> arrayItems = new ArrayList<>();
        if (helper == null) {
            helper = new SimpleOpenHelper(getActivity());
        }
        if (db == null) {
            db = helper.getReadableDatabase();
        }
        Log.d("debug", "**********Cursor");
        Cursor cursor = db.query(
                "stafftable",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                HashMap<String, String> mapItems = new HashMap<>();
                mapItems.put("staffName", cursor.getString(1));
                arrayItems.add(mapItems);
                cursor.moveToNext();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.label_no_staff), Toast.LENGTH_SHORT).show();
        }
        // 忘れずに！
        cursor.close();
        final SimpleAdapter adapter = new SimpleAdapter(getActivity(), arrayItems, R.layout.checkbox_list_item, new String[] {"staffName"}, new int[] {R.id.checkBox});
        ListView list = view.findViewById(R.id.es_f_list);
        list.setAdapter(adapter);
    }

    public void clickDelete(){
        Button button = view.findViewById(R.id.es_f_button);
        button.setOnClickListener(v -> {
            ArrayList<String> array = StaffActivity.stock;
            while(!array.isEmpty()) {
                String[] params = {array.remove(0)};
                deleteData(params);
            }
            EditStaffFragment fragmentEditStaff = new EditStaffFragment();
            setFragment(fragmentEditStaff);
        });
    }

    public void deleteData(String[] data) {
        if (helper == null) {
            helper = new SimpleOpenHelper(getActivity());
        }

        if (db == null) {
            db = helper.getWritableDatabase();
        }

        db.delete("stafftable", "staff = ?", data);
    }

    public void setFragment(Fragment fragment){
        FragmentManager manager = Objects.requireNonNull(getFragmentManager());
        manager.beginTransaction()
                .replace(R.id.staff_layout, fragment)
                .commit();
    }

}