package com.yalantis.guillotine.animation;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yalantis.guillotine.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.widget.Toast;
import android.widget.ToggleButton;



public class multitap extends AppCompatActivity {

    String myJSON;
    String id ="123";

    private static final String TAG_RESULTS="result";
    ToggleButton Button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multitap);
        Button = (ToggleButton) this.findViewById(R.id.button02);
        Button.setOnClickListener(mClickListener);


    }
    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String order="00000";
            if(Button.isChecked()){
                Button.setBackgroundDrawable(
                        getResources().getDrawable(R.drawable.poweron));
                order = "11000";
            }else{
                Button.setBackgroundDrawable(
                        getResources().getDrawable(R.drawable.poweroff));
                order = "10000";
            }
            updateorder(order);
        }
    };

    public void updateorder(final String order){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected String doInBackground(String... params) {

                String uri ="http://119.205.68.167:8081/order.php";

                BufferedReader bufferedReader = null;
                try {

                    String data  = URLEncoder.encode("order", "UTF-8") + "=" +
                            URLEncoder.encode(order, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" +
                            URLEncoder.encode(id, "UTF-8");

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
                    return new String("Exception: " + e.getMessage());
                }



            }
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(multitap.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


}

