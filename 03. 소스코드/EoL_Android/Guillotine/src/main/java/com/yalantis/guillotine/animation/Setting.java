package com.yalantis.guillotine.animation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.guillotine.R;

import java.lang.reflect.Array;

/**
 * Created by csp on 2016-05-23.
 */
public class Setting extends AppCompatActivity {
    Button save;
    dbHelper helper;
    SQLiteDatabase db;
    String selTV;
    String selAir;
    String selSet;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        helper = new dbHelper(this);

        try {
            db = helper.getWritableDatabase();
            //데이터베이스 객체를 얻기 위하여 getWritableDatabse()를 호출

        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }



        save = (Button) this.findViewById(R.id.btnsave);
        save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                Toast.makeText(v.getContext(), "내용을 저장합니다",Toast.LENGTH_SHORT).show();




                //if(temp == null)
                   // db.execSQL("INSERT INTO contact VALUES('"+selTV+"','"+selAir+"','"+selSet+"');");

                //else
                   db.execSQL("update contact set tv='"+selTV+"',air='"+selAir+"',settop='"+selSet+"';");


               // cursor.moveToFirst();


                Cursor cursor;
                cursor = db.rawQuery("SELECT * FROM contact;", null);

                while(cursor.moveToNext()){
                    String tv = cursor.getString(0);
                   String air = cursor.getString(1);
                   String settop = cursor.getString(2);
                    Toast.makeText(v.getContext(), tv+ " "+air+" "+settop ,Toast.LENGTH_SHORT).show();
               }

                cursor.close();

            }

        });





//선택 화면 1
        Spinner device = (Spinner)findViewById(R.id.device);
        device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
               // Toast.makeText(getApplicationContext(), "data=" + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                selTV = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

// 선택 화면 2
        Spinner coop = (Spinner)findViewById(R.id.cooper);
        coop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
              //  Toast.makeText(getApplicationContext(), "data=" + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                selAir = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 선택 화면 3
        Spinner settp= (Spinner)findViewById(R.id.settop);
        settp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //Toast.makeText(getApplicationContext(), "data=" + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                selSet = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }
}
class dbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "mysetting.db";
    private static final int DATABASE_VERSION =1;


    /*
     *먼저 SQLiteOpenHelper클래스를 상속받은 dbHelper클래스가 정의 되어 있다. 데이터베이스 파일 이름은 "mycontacts.db"가되고,
     *데이터베이스 버전은 1로 되어있다. 만약 데이터베이스가 요청되었는데 데이터베이스가 없으면 onCreate()를 호출하여 데이터베이스
     *파일을 생성해준다.
     */

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contact (tv TEXT,air TEXT,settop TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE contact");
        onCreate(db);
    }



}
