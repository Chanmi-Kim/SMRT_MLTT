package com.yalantis.guillotine.animation;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.yalantis.guillotine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;



public class Sensor extends AppCompatActivity {

    String myJSON;
    String id ="123";
    String pass ="456";
    JSONArray peoples = null;
    private static final String TAG_RESULTS="result";
    private static final String TAG_SWITCH1 = "switch1";
    private static final String TAG_TEMPERATURE = "switch2";
    private static final String TAG_LX ="updata";
    ArrayList<HashMap<String, String>> personList;
    TextView textview1;
    TextView textview2;
    TextView textview3;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor);
        textview1 = (TextView)findViewById(R.id.textView2);
        textview2 = (TextView)findViewById(R.id.textView5);
        textview3 = (TextView)findViewById(R.id.textView8);
        findViewById(R.id.button).setOnClickListener(mClickListener);

    }
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            getData("http://119.205.68.167:8081/data.php");
            }
        };


    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri ="http://119.205.68.167:8081/data.php";

                BufferedReader bufferedReader = null;
                try {

                    String data  = URLEncoder.encode("id", "UTF-8") + "=" +
                            URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" +
                            URLEncoder.encode(pass, "UTF-8");

                    URL url = new URL(uri);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter
                            (conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line+"\n");
                        break;
                    }
                    return sb.toString();

                }catch(Exception e){
                    return null;
                }



            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);

                    for(int i=0;i<peoples.length();i++){
                        JSONObject c = peoples.getJSONObject(i);
                        String switch1 = c.getString(TAG_SWITCH1);
                        String switch2 = c.getString(TAG_TEMPERATURE);
                        String updata = c.getString(TAG_LX);
                        HashMap<String,String> persons = new HashMap<String,String>();

                        persons.put(TAG_SWITCH1,switch1);
                        persons.put(TAG_TEMPERATURE,switch2);
                        persons.put(TAG_LX,updata);
                        textview1.setText(persons.put(TAG_SWITCH1, switch1));
                        textview2.setText(persons.put(TAG_TEMPERATURE,switch2));
                        textview3.setText( persons.put(TAG_LX,updata));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}

