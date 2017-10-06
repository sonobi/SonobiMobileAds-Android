package com.sonobi.sonobimobileads;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by jgo on 10/4/17.
 */

public class SonobiMobileBannerAd {

    private PublisherAdView bannerAdView;
    private JSONObject config;
    private ExtraTrinityParamManager extraTrinityParamManager;

    public SonobiMobileBannerAd(PublisherAdView bannerAdView, String config, ExtraTrinityParamManager extraTrinityParamManager) {
        this.bannerAdView = bannerAdView;
        this.extraTrinityParamManager = extraTrinityParamManager;
        try {
            this.config = new JSONObject(config);
        } catch (JSONException e) {
            this.config = new JSONObject();
        }
    }

    public PublisherAdRequest.Builder requestBid(PublisherAdRequest.Builder adRequest) {
        Object timeout;
        String sizes = "";
        JSONObject keymakerResponse;
        JSONObject bidResponse;
        Boolean testMode;

        //Targeting variables;
        String sbiDc;
        Double sbiPrice;
        String sbiAid;
        String sbiDozer;

        //Form our CSV of sizes
        for(AdSize adSize : bannerAdView.getAdSizes()) {
            sizes += adSize.getWidth() + "x" + adSize.getHeight() + ",";
        }

        if(sizes.charAt(sizes.length() - 1) == ',') {
            sizes = sizes.substring(0, sizes.length() - 1);
        }

        //Get our timeout
        try {
             timeout = this.config.get("timeout");
        } catch (JSONException e) {
            timeout = "15000";
        }

        try {
            testMode = this.config.getBoolean("test");
        }catch (JSONException e) {
            testMode = false;
        }

        //Do the trinity request
        SonobiKeymaker sonobiKeymaker = new SonobiKeymaker(bannerAdView.getId(), bannerAdView.getAdUnitId(), sizes, this.extraTrinityParamManager);
        keymakerResponse = sonobiKeymaker.executeRequest(Integer.valueOf(timeout.toString()), testMode);

        //try to get sbi_dc from the response
        try {
            sbiDc = keymakerResponse.getString("sbi_dc");
            adRequest.addCustomTargeting("sbi_dc", sbiDc);
        } catch (JSONException e) { //if it errors, return the adRequest
            e.printStackTrace();
            return adRequest;
        }

        //try to get the bid response
        try {
            bidResponse = keymakerResponse.getJSONObject("slots").getJSONObject(bannerAdView.getAdUnitId() + "|" +bannerAdView.getId());
            //bidResponse = keymakerResponse.getJSONObject("slots").getJSONObject("mobile-test");
        } catch (JSONException e) { //if it errors, return the adRequest
            e.printStackTrace();
            return adRequest;
        }

        try { //try to get the cpm value
            sbiPrice = bidResponse.getDouble("sbi_mouse");
            adRequest.addCustomTargeting("sbi_price", sbiPrice.toString());
        } catch (JSONException e) { //if it errors, return the adRequest
            e.printStackTrace();
        }

        try { //try to get the aid
            sbiAid = bidResponse.getString("sbi_aid");
            adRequest.addCustomTargeting("sbi_aid", sbiAid);
        } catch (JSONException e) { //if it errors, return the adRequest
            e.printStackTrace();
        }

        try { //try to get the dozer
            sbiDozer = bidResponse.getString("sbi_dozer");
            adRequest.addCustomTargeting("sbi_dozer", sbiDozer);
        } catch (JSONException e) { //if it errors, return the adRequest
            e.printStackTrace();
        }

        return adRequest;

    }

}
