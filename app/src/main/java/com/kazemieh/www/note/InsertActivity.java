package com.kazemieh.www.note;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class InsertActivity extends AppCompatActivity {

    DataBaseOpenHelper dataBaseOpenHelper;
    String stitle, sdes;
    String s;
    int id;
    int stari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        dataBaseOpenHelper = new DataBaseOpenHelper(this);

        final EditText et_des = findViewById(R.id.et_InsertActivity_des);
        final EditText et_title = findViewById(R.id.et_InsertActivity_title);
        final ImageView star = findViewById(R.id.iv_Insert_star);
        ImageView b_delete = findViewById(R.id.iv_Insert_delete);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            s = bundle.getString("key", "in");
            if (s.equals("up")) {
                id = bundle.getInt("id", -1);
                Cursor cursor = dataBaseOpenHelper.getWritableDatabase().rawQuery(" select * from " + dataBaseOpenHelper.tablename + " where " + dataBaseOpenHelper.idnote + " = " + id, null);
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.titlenote));
                    String des = cursor.getString(cursor.getColumnIndex(dataBaseOpenHelper.desnote));
                    stari = cursor.getInt(cursor.getColumnIndex(dataBaseOpenHelper.favnote));

                    String colortext="<font color='red' >"+MainActivity.searchtext+"</font>";
                    Spanned texttitle= Html.fromHtml(title.replace(MainActivity.searchtext,colortext));
                    Spanned textdes=Html.fromHtml(des.replace(MainActivity.searchtext,colortext));

                    et_title.setText(texttitle);
                    et_des.setText(textdes);
                    if (stari == 0) {
                        star.setImageResource(R.drawable.ic_round_star_border_24);
                    } else {
                        star.setImageResource(R.drawable.ic_round_star_24);
                    }

                }
                b_delete.setVisibility(View.VISIBLE);

       //         Toast.makeText(this, "         اینجا قرار است که آپدیت اتفاق بیوفتد         " + s, Toast.LENGTH_SHORT).show();
            } else {

           //     Toast.makeText(this, "         اینجا قرار است که اضافه کردن اتفاق بیوفتد         " + s, Toast.LENGTH_SHORT).show();
            }
        }


        ImageView b_save = findViewById(R.id.b_InsertActivity_save);


        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stitle = et_title.getText().toString();
                sdes = et_des.getText().toString();

                if (!stitle.equals("") && !sdes.equals("")) {

                    if (s.equals("in")) {
                        //  insertBySQL();
                        insertByJava();

                    } else {
                        //updateBySQL();
                        updateByJava();
                    }
                    finish();
                } else {
                    Toast.makeText(InsertActivity.this, "حتما باید عنوان و متن پر باشد", Toast.LENGTH_SHORT).show();
                }


            }
        });

        final ImageView b_cancel = findViewById(R.id.b_InsertActivity_cancel);
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stari == 0) {
                    stari = 1;
                    star.setImageResource(R.drawable.ic_round_star_24);
                } else {
                    stari = 0;
                    star.setImageResource(R.drawable.ic_round_star_border_24);
                }
            }
        });

        b_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivity.this);
                builder.setMessage("آیا می خواهید حذف کنید؟")
                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // dataBaseOpenHelper.getWritableDatabase().execSQL(" delete from "+dataBaseOpenHelper.tablename+" where "+dataBaseOpenHelper.idnote+" = "+ id);
                                dataBaseOpenHelper.getWritableDatabase().delete(dataBaseOpenHelper.tablename, dataBaseOpenHelper.idnote + " = " + id, null);
                                finish();
                            }
                        })
                        .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });


    }


    //    اضافه کردن به دیتابیس به وسیله sql
    public void insertBySQL() {
        dataBaseOpenHelper.getWritableDatabase().execSQL(" insert into " +
                dataBaseOpenHelper.tablename +
                " ( " +
                dataBaseOpenHelper.titlenote +
                " , " +
                dataBaseOpenHelper.desnote +
                " , " +
                dataBaseOpenHelper.favnote +
                " ) values ( '" +
                stitle +
                "' , '" +
                sdes +
                "' , '" +
                stari +
                "' ) ");
        //dataBaseOpenHelper.getWritableDatabase().execSQL(" insert into tblnote ( titlenote , desnote ) values ( '"+stitle+"' , '"+stitle+"' ) ");
    }

    //اضافه کردن به دیتابیس به وسیله جاوا
    public void insertByJava() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(dataBaseOpenHelper.titlenote, stitle);
        contentValues.put(dataBaseOpenHelper.desnote, sdes);
        contentValues.put(dataBaseOpenHelper.favnote, stari);

        dataBaseOpenHelper.getWritableDatabase().insert(dataBaseOpenHelper.tablename, null, contentValues);
    }

    public void updateBySQL() {
        dataBaseOpenHelper.getWritableDatabase().execSQL(" update " + dataBaseOpenHelper.tablename + " set "
                + dataBaseOpenHelper.titlenote + " = '" + stitle + "' , " + dataBaseOpenHelper.desnote + " = '" + sdes + "' , "
                + dataBaseOpenHelper.favnote + " =  " + stari + "  where " + dataBaseOpenHelper.idnote + " = '" + id + "' ");
    }

    public void updateByJava() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dataBaseOpenHelper.titlenote, stitle);
        contentValues.put(dataBaseOpenHelper.desnote, sdes);
        contentValues.put(dataBaseOpenHelper.favnote, stari);
        String[] ss = {id + ""};
        dataBaseOpenHelper.getWritableDatabase().update(dataBaseOpenHelper.tablename, contentValues, dataBaseOpenHelper.idnote + " =? ", ss);
    }

}