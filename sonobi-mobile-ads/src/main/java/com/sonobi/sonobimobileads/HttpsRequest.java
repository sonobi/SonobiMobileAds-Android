package com.sonobi.sonobimobileads;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jgo on 10/4/17.
 */

class HttpsRequest extends AsyncTask<Object, Void, Object> {

    private String methodType;
    private Integer timeout;
    private Boolean testMode = false;

    HttpsRequest(String methodType, Integer timeout, Boolean testMode) {
        this.methodType = methodType;
        this.timeout = timeout;
        this.testMode = testMode;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String url = objects[0].toString();
        Object result;
        String inputLine;

        try {
            URL myUrl = new URL(url);

            HttpsURLConnection connection = (HttpsURLConnection) myUrl.openConnection();

            connection.setRequestMethod(this.methodType);
            connection.setReadTimeout(this.timeout);
            connection.setConnectTimeout(this.timeout);

            if (this.testMode) {
                connection.setRequestProperty("Cookie", "sbi_test={\"sbi_apoc\":\"guarantee\",\"sbi_mouse\":20};");
            }

            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();


        } catch (IOException e) {
            e.printStackTrace();
            result = "";
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
    }
}
