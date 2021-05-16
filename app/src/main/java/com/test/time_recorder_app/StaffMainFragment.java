package com.test.time_recorder_app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class StaffMainFragment extends Fragment {

    private ListView list;
    private SimpleOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_staff_main, container, false);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.title_staff_main));

        list = view.findViewById(R.id.sm_f_list);

        createStaffList();

        SearchView search = view.findViewById(R.id.sm_f_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                if (queryText == null || queryText.equals("")) {
                    list.clearTextFilter();
                } else {
                    list.setFilterText(queryText);
                }
                return false;
            }
        });

        return view;
    }

    public void createStaffList(){
        if(helper == null){
            helper = new SimpleOpenHelper(getActivity());
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");
        Cursor cursor = db.query(
                "stafftable",
                null,
                null, null,
                null,
                null,
                null,
                null
        );
        ArrayList<HashMap<String, String>> arrayItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
//            data.clear();
            for(int i = 0; i < cursor.getCount(); i++){
                HashMap<String, String> MapItems = new HashMap<>();
                MapItems.put("Name", cursor.getString(1));
                MapItems.put("Wage", getString(R.string.sm_f_label_hourly_wage) + cursor.getInt(2));
                arrayItems.add(MapItems);
                cursor.moveToNext();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.label_no_staff), Toast.LENGTH_SHORT).show();
        }
        // 忘れずに！
        cursor.close();

        final SimpleAdapter adapter = new SimpleAdapter(getActivity(), arrayItems, R.layout.list_item, new String[] {"Name", "Wage"}, new int[] {R.id.text_title, R.id.text_comment});
        list.setAdapter(adapter);
        list.setTextFilterEnabled(true);
        list.setOnItemClickListener((parent, view, position, id) -> {
            DialogFragment dialog = new DialogEditStaffFragment();
            final Bundle args = new Bundle();
            args.putString("Name", (String) ((TextView) view.findViewById(R.id.text_title)).getText());
            dialog.setArguments(args);
            dialog.show(Objects.requireNonNull(getFragmentManager()), "dialog_edit_staff");
        });
    }
}