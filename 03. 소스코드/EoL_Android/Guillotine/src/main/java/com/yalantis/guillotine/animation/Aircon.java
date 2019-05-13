package com.yalantis.guillotine.animation;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yalantis.guillotine.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by csp on 2016-05-19.
 */
public class Aircon extends AppCompatActivity {
    ToggleButton onoffbtn;
    ImageButton tplus;
    ImageButton tminus;
    ImageButton pplus;
    ImageButton pminus;


    String myJSON;
    String id ="123";

    private static final String TAG_RESULTS="result";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aircon);

        //온오프 버튼 이벤트
        onoffbtn = (ToggleButton) this.findViewById(R.id.aircononbtn);
        onoffbtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="99999";
                if (onoffbtn.isChecked()) {
                    onoffbtn.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.poweron));
                    order = "00110";
                } else {
                    onoffbtn.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.poweroff));
                    order = "00110";
                }
                updateorder(order);
            }
        });


        tplus = (ImageButton) this.findViewById(R.id.tempplus);
        tplus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="00111";
                Toast toast = Toast.makeText(v.getContext(), "온도를 올립니다!",
                        Toast.LENGTH_SHORT);
                toast.show();
                updateorder(order);
            }
        });

        tminus = (ImageButton) this.findViewById(R.id.tempminus);
        tminus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="00112";
                Toast toast = Toast.makeText(v.getContext(), "온도를 내립니다!",
                        Toast.LENGTH_SHORT);
                toast.show();
                updateorder(order);
            }
        });


        pplus = (ImageButton) this.findViewById(R.id.powerplus);
        pplus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="00113";
                Toast toast = Toast.makeText(v.getContext(), "풍속을 올립니다!",
                        Toast.LENGTH_SHORT);
                toast.show();
                updateorder(order);
            }
        });

        pminus = (ImageButton) this.findViewById(R.id.powerminus);
        pminus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="00114";
                Toast toast = Toast.makeText(v.getContext(), "풍속을 내립니다!",
                        Toast.LENGTH_SHORT);
                toast.show();
                updateorder(order);
            }
        });


    }


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
                loading = ProgressDialog.show(Aircon.this, "Please Wait", null, true, true);
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

