package com.sonobi.sonobimobileads;

import android.support.annotation.Keep;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by jgo on 10/4/17.
 */

public class DfpInterstitialAd extends SonobiConfig {

    private PublisherInterstitialAd bannerAdView;
    private ExtraTrinityParams extraTrinityParams;

    @Keep
    public DfpInterstitialAd(PublisherInterstitialAd bannerAdView, ExtraTrinityParams extraTrinityParams) {
        super();
        this.bannerAdView = bannerAdView;
        this.extraTrinityParams = extraTrinityParams;
    }

    @Keep
    public PublisherAdRequest.Builder requestBid(PublisherAdRequest.Builder adRequest) {
        String sizes = "interstitial";
        JSONObject keymakerResponse;
        JSONObject bidResponse;

        //Targeting variables;
        String sbiDc;
        Double sbiPrice;
        String sbiAid;
        String sbiDozer;

        //Do the trinity request
        Keymaker sonobiKeymaker = new Keymaker(1, bannerAdView.getAdUnitId(), sizes, this.extraTrinityParams);
        keymakerResponse = sonobiKeymaker.executeRequest(this.getTimeout(), this.isTestMode());

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
            bidResponse = keymakerResponse.getJSONObject("slots").getJSONObject(bannerAdView.getAdUnitId() + "|" + 1);
            //bidResponse = keymakerResponse.getJSONObject("slots").getJSONObject("mobile-test");
        } catch (JSONException e) { //if it errors, return the adRequest
            e.printStackTrace();
            return adRequest;
        }

        try { //try to get the price
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
