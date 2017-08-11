package com.gepherrini.android.babysitter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = (TextView) findViewById(R.id.main);

        new Download().execute();
    }



    public class Download extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            URL url;
            HttpsURLConnection connection = null;
            BufferedReader reader = null;
            String code = "";

            try {

                url = new URL("https://script.googleusercontent.com/macros/echo?user_content_key=D9-e5BtUWsT6VMTgu_JCjP_KHOim2pGNgvUYyNnyshcwX0S76_qO3HTA2O1fyzo-rkdOTMjg-fSDPSWwQTukb-zbv0CaDz4KOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa1GhPSVukpSQTydEwAEXFXgt_wltjJcH3XHUaaPC1fv5o9XyvOto09QuWI89K6KjOu0SP2F-BdwUlVc4aLcZqWafX_uJdZVXfiPhRs0qlubIcyFTBABm9yQK8-vgYlC8d7ucxA2S5GjS5y7FLqOV0Tk27B8Rh4QJTQ&lib=MnrE7b2I2PjfH799VodkCPiQjIVyBAxva");
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = "";
                while ((line = reader.readLine()) != null) {
                    code += line;
                }

                return new JSONObject(code);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            String text = "";
            try {
                JSONArray array = s.getJSONArray("Sheet1");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject info = array.getJSONObject(i);
                    text += info.getString("Name");
                    text += ", ";
                    text += info.getString("Surname");
                    text += ", ";
                    text += info.getString("Adress");
                    text += ".\n";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            main.setText(text);
        }
    }
}
