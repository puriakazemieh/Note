package com.kazemieh.www.note;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DataBaseOpenHelper dataBaseOpenHelper;
    boolean updateDataBase = false;
    List<DataModel> dataModelAll;
    Adapter adapter;
    SharedPreferences sharedPreferences;

    boolean bstate = true;
    boolean searchstate = true;
    ImageView iv_search;
    EditText et_serach;
    ImageView iv_state;
    ImageView iv_sort;
    public static String searchtext="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("note", 0);

        updateDataBase = true;

        dataBaseOpenHelper = new DataBaseOpenHelper(this);
        dataBaseOpenHelper.onUpgrade(dataBaseOpenHelper.getWritableDatabase(), 1, 2);

        final RecyclerView recyclerView = findViewById(R.id.rv_MainActivity_showlist);


        iv_state = findViewById(R.id.iv_MainActivity_state);
        iv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bstate) {
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false));
                    iv_state.setImageResource(R.drawable.ic_round_grid_on_24);
                    bstate = false;

                } else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    iv_state.setImageResource(R.drawable.ic_round_view_headline_24);
                    bstate = true;

                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        dataModelAll = showListSearchByJava("");
        adapter = new Adapter(this, dataModelAll, dataBaseOpenHelper);
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                intent.putExtra("key", "in");
                startActivity(intent);
            }
        });

        iv_sort = findViewById(R.id.iv_MainActivity_sort);
        iv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sort = sharedPreferences.getString("sortsh", "asc");
                if (sort.equals("asc")) {
                    sort = "desc";
                } else {
                    sort = "asc";
                }

                sharedPreferences.edit().putString("sortsh", sort).apply();
                dataModelAll.clear();
                dataModelAll.addAll(showListSearchByJava(""));
                adapter.notifyDataSetChanged();

            }
        });

        iv_search = findViewById(R.id.iv_MainActivity_serch);
        et_serach = findViewById(R.id.et_MainActivity_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (searchstate) {
                    iv_sort.setVisibility(View.GONE);
                    iv_state.setVisibility(View.GONE);
                    et_serach.setVisibility(View.VISIBLE);
                    iv_search.setImageResource(R.drawable.ic_round_clear_24);

                    et_serach.requestFocus();

                    inputMethodManager.showSoftInput(et_serach, InputMethodManager.SHOW_IMPLICIT);

                    searchstate = false;

                } else {

                    iv_sort.setVisibility(View.VISIBLE);
                    iv_state.setVisibility(View.VISIBLE);
                    et_serach.setVisibility(View.GONE);

                    iv_search.setImageResource(R.drawable.ic_round_search_24);

                    inputMethodManager.hideSoftInputFromWindow(et_serach.getWindowToken(), 0);

                    dataModelAll.clear();
                    dataModelAll.addAll(showListSearchByJava(""));
                    adapter.notifyDataSetChanged();

                    et_serach.setText("");

                    searchstate = true;
                }


            }
        });
        et_serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataModelAll.clear();
                dataModelAll.addAll(showListSearchByJava(s.toString()));
                adapter.notifyDataSetChanged();
                searchtext = et_serach.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //  نمایش اطلاعات داخل دیتابیس به وسیله ی sql
    public List<DataModel> showlistBySQL() {
        List<DataModel> dataModelList = new ArrayList<>();
        //  Cursor cursor = dataBaseOpenHelper.getWritableDatabase().rawQuery(" select * from " + dataBaseOpenHelper.tablename, null);
        Cursor cursor = dataBaseOpenHelper.getWritableDatabase().rawQuery(" select * from " + dataBaseOpenHelper.tablename + " order by " + dataBaseOpenHelper.idnote + " " + sharedPreferences.getString("sortsh", "asc"), null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.titlenote));
            String des = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.desnote));
            int id = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.idnote));
            int fav = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.favnote));
            dataModelList.add(new DataModel(title, des, id, fav));
        }
        return dataModelList;
    }

    //  نمایش اطلاعات داخل دیتابیس به وسیله ی جاوا
    public List<DataModel> showListByJava() {
        List<DataModel> dataModelList = new ArrayList<>();
        //   Cursor cursor = dataBaseOpenHelper.getWritableDatabase().query(dataBaseOpenHelper.tablename, null, null, null, null, null, null);

        Cursor cursor = dataBaseOpenHelper.getWritableDatabase().query(dataBaseOpenHelper.tablename, null, null, null, null, null, dataBaseOpenHelper.idnote + " " + sharedPreferences.getString("sortsh", "asc"));
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.titlenote));
            String des = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.desnote));
            int id = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.idnote));
            int fav = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.favnote));
            dataModelList.add(new DataModel(title, des, id, fav));
        }
        return dataModelList;
    }

    public List<DataModel> showlistSearchBySQL(String search) {
        List<DataModel> dataModelList = new ArrayList<>();

        //  Cursor cursor = dataBaseOpenHelper.getWritableDatabase().rawQuery(" select * from " + dataBaseOpenHelper.tablename, null);
        Cursor cursor = dataBaseOpenHelper.getWritableDatabase().rawQuery(" select * from " + dataBaseOpenHelper.tablename +
                " where " + dataBaseOpenHelper.titlenote + " like '%" + search + "%' or " + dataBaseOpenHelper.desnote + " like '%" + search + "%' " +
                " order by " + dataBaseOpenHelper.idnote + " " + sharedPreferences.getString("sortsh", "asc"), null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.titlenote));
            String des = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.desnote));
            int id = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.idnote));
            int fav = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.favnote));
            dataModelList.add(new DataModel(title, des, id, fav));
        }
        return dataModelList;
    }


    public List<DataModel> showListSearchByJava(String search) {
        List<DataModel> dataModelList = new ArrayList<>();
        //   Cursor cursor = dataBaseOpenHelper.getWritableDatabase().query(dataBaseOpenHelper.tablename, null, null, null, null, null, null);

        Cursor cursor = dataBaseOpenHelper.getWritableDatabase().query(dataBaseOpenHelper.tablename, null,
                dataBaseOpenHelper.titlenote + " like ? or " + dataBaseOpenHelper.desnote + " like ? "
                , new String[]{"%" + search + "%", "%" + search + "%"}, null, null, dataBaseOpenHelper.idnote + " " + sharedPreferences.getString("sortsh", "asc"));
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.titlenote));
            String des = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.desnote));
            int id = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.idnote));
            int fav = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.favnote));
            dataModelList.add(new DataModel(title, des, id, fav));
        }
        return dataModelList;
    }

    @Override
    public void onBackPressed() {
        if (!searchstate) {

            iv_sort.setVisibility(View.VISIBLE);
            iv_state.setVisibility(View.VISIBLE);
            et_serach.setVisibility(View.GONE);

            iv_search.setImageResource(R.drawable.ic_round_search_24);

            dataModelAll.clear();
            dataModelAll.addAll(showListSearchByJava(""));
            adapter.notifyDataSetChanged();

            et_serach.setText("");

            searchstate = true;
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        if (updateDataBase) {
            dataModelAll.clear();
            if (!searchstate) {
                dataModelAll.addAll(showListSearchByJava(et_serach.getText().toString()));
            } else {
                dataModelAll.addAll(showListSearchByJava(""));
            }

            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}