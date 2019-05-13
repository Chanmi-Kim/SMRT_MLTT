package com.yalantis.guillotine.animation;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Tv extends AppCompatActivity {
    ToggleButton onoffbtn;
    ToggleButton soundonoff;
    ImageButton tplus;
    ImageButton tminus;
    ImageButton pplus;
    ImageButton pminus;


    String myJSON;
    String id ="123";

    private static final String TAG_RESULTS="result";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);

        //온오프 버튼 이벤트
        soundonoff = (ToggleButton) this.findViewById(R.id.soundonoff);
        soundonoff.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order = "99999";
                if (soundonoff.isChecked()) {
                    soundonoff.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.soundon));
                    order = "00010";
                } else {
                    soundonoff.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.soundoff));
                    order = "00010";
                }
                updateorder(order);
            }
        });

        onoffbtn = (ToggleButton) this.findViewById(R.id.tvbtn);
        onoffbtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="99999";
                if (onoffbtn.isChecked()) {
                    onoffbtn.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.poweron));
                    order = "00010";
                } else {
                    onoffbtn.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.poweroff));
                    order = "00010";
                }
                updateorder(order);
            }
        });


        tplus = (ImageButton) this.findViewById(R.id.soundup);
        tplus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="00013";
                Toast toast = Toast.makeText(v.getContext(), "소리를 올립니다!",
                        Toast.LENGTH_SHORT);
                toast.show();
                updateorder(order);
            }
        });

        tminus = (ImageButton) this.findViewById(R.id.sounddown);
        tminus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="00014";
                Toast toast = Toast.makeText(v.getContext(), "소리를 내립니다!",
                        Toast.LENGTH_SHORT);
                toast.show();
                updateorder(order);
            }
        });


        pplus = (ImageButton) this.findViewById(R.id.channelup);
        pplus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="00011";
                Toast toast = Toast.makeText(v.getContext(), "채널을 올립니다!",
                        Toast.LENGTH_SHORT);
                toast.show();
                updateorder(order);
            }
        });

        pminus = (ImageButton) this.findViewById(R.id.channeldown);
        pminus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String order="00012";
                Toast toast = Toast.makeText(v.getContext(), "채널을 내립니다!",
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
                loading = ProgressDialog.show(Tv.this, "Please Wait", null, true, true);
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