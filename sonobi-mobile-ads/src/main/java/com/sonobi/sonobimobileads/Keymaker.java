package com.sonobi.sonobimobileads;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by jgo on 10/5/17.
 */

class Keymaker {

    //Constant query params
    private final static String BASE_TRINITY_URL = "https://matrix-local.connollygroup.com/trinity?key_maker=";
    private final static String CV = "sbi";
    private final static String VP = "mobile";
    private final static String S = UUID.randomUUID().toString();
    private final static String PV = UUID.randomUUID().toString();
    private final static String VC = "0";

    String slotKey;
    private String url;

    Keymaker(Integer id, String adUnitId, String sizesCsv, ExtraTrinityParams extraTrinityParamManager) {

        this.slotKey = adUnitId + "|" + id;

        String url = BASE_TRINITY_URL;
        url += "{\"" + this.slotKey + "\":\"" + sizesCsv + "\"}";
//      url += "{\"mobile-test\":\"f7d3088e7e7e9a2b1126\"}";
        url += "&cv=" + CV;

        url += "&vp=" + VP;

        //cache buster
        url += "&s=" + S;

        //page view id
        url += "&pv=" + PV;

        url += "&vc=" + VC;

        String hfa = extraTrinityParamManager.getHfa(),
                cdf = extraTrinityParamManager.getCdf(),
                ant = extraTrinityParamManager.getAnt(),
                gmgt = extraTrinityParamManager.getGmgt();

        if (hfa.length() > 0) {
            url += "&hfa=" + hfa;
        }

        if (cdf.length() > 0) {
            url += "&cdf=" + cdf;
        }

        if (ant.length() > 0) {
            url += "&ant=" + ant;
        }

        if (gmgt.length() > 0) {
            url += "&gmgt=" + gmgt;
        }

        this.url = url;
    }

    JSONObject executeRequest(Integer timeout, Boolean testMode) {

        HttpsRequest httpsRequest = new HttpsRequest("GET", timeout, testMode);
        Object rawResponse;
        JSONObject parsedResponse = new JSONObject();
        try {
            try {
                rawResponse = httpsRequest.execute(this.url).get().toString();
                parsedResponse = this.parseResponse(rawResponse);

            } catch (ExecutionException e) {
                return parsedResponse;
            }
        } catch (InterruptedException e) {
            return parsedResponse;
        }

        return parsedResponse;

    }

    private JSONObject parseResponse(Object rawResponse) {

        JSONObject parsedResponse;
        String stringedResponse = rawResponse.toString();

        stringedResponse = stringedResponse.replace("sbi(", "");
        stringedResponse = stringedResponse.replace(");", "");

        try {
            parsedResponse = new JSONObject(stringedResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            parsedResponse = new JSONObject();
        }

        return parsedResponse;

    }


}
