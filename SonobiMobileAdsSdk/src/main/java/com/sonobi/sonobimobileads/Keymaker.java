package com.sonobi.sonobimobileads;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by jgo on 10/5/17.
 */

public class Keymaker {

    //Constant query params
    public final String BASE_TRINITY_URL = "https://matrix-local.connollygroup.com/trinity?key_maker=";
    public final String CV = "sbi";
    public final String VP = "mobile";
    public final String S = UUID.randomUUID().toString();
    public final String PV = UUID.randomUUID().toString();
    public final String VC = "0";

    public String adUnitId;
    public String sizesCsv;
    public String slotKey;
    public Integer id;
    public String url;
    public Object rawResponse;
    public JSONObject parsedResponse;
    public ExtraTrinityParams extraTrinityParamManager;

    public Keymaker(Integer id, String adUnitId, String sizesCsv, ExtraTrinityParams extraTrinityParamManager) {

        this.adUnitId = adUnitId;
        this.sizesCsv = sizesCsv;
        this.slotKey = adUnitId + "|" + id;
        this.id = id;
        this.extraTrinityParamManager = extraTrinityParamManager;

        String url = BASE_TRINITY_URL;
        url+= "{\"" + this.adUnitId + "|" + id + "\":\"" + this.sizesCsv + "\"}";
//      url += "{\"mobile-test\":\"f7d3088e7e7e9a2b1126\"}";
        url += "&cv=" + this.CV;

        url += "&vp=" + this.VP;

        //cache buster
        url += "&s=" + this.S;

        //page view id
        url += "&pv=" + this.PV;

        url += "&vc=" + this.VC;

        String hfa = this.extraTrinityParamManager.getHfa(),
                cdf = this.extraTrinityParamManager.getCdf(),
                ant = this.extraTrinityParamManager.getAnt(),
                gmgt = this.extraTrinityParamManager.getGmgt();

        if(hfa.length() > 0) {
            url += "&hfa=" + hfa;
        }

        if(cdf.length() > 0) {
            url += "&cdf=" + cdf;
        }

        if(ant.length() > 0) {
            url += "&ant=" + ant;
        }

        if(gmgt.length() > 0) {
            url += "&gmgt=" + gmgt;
        }

        this.url = url;
    }

    public JSONObject executeRequest(Integer timeout, Boolean testMode) {

        HttpsRequest httpsRequest = new HttpsRequest("GET", timeout, testMode);

        try {
            try {
                this.rawResponse = httpsRequest.execute(this.url).get().toString();
                this.parsedResponse = this.parseResponse(this.rawResponse);

            } catch (ExecutionException e) {
                return this.parsedResponse;
            }
        } catch (InterruptedException e) {
            return this.parsedResponse;
        }

        return this.parsedResponse;

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
