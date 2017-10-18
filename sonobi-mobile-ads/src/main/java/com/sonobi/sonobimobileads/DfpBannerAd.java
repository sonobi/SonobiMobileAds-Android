package com.sonobi.sonobimobileads;

import android.support.annotation.Keep;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by jgo on 10/4/17.
 */

/**
 * Class to request bids from sonobi and set targeting on the ad request
 */
public class DfpBannerAd extends SonobiConfig {

    private PublisherAdView bannerAdView;
    private ExtraTrinityParams extraTrinityParams;

    /**
     * Constructor
     * @param bannerAdView {PublisherAdView} The Ad view to grab the adunit, sizes, and id from
     * @param extraTrinityParams {ExtraTrinityParams} extra targeting parameters to pass to the trinity request
     */
    @Keep
    public DfpBannerAd(PublisherAdView bannerAdView, ExtraTrinityParams extraTrinityParams) {
        super();
        this.bannerAdView = bannerAdView;
        this.extraTrinityParams = extraTrinityParams;
    }

    /**
     * Method to request a bid from trinity
     *
     * @param adRequest {PublisherAdRequest.Builder} The ad request builder to add sbi_* targeting to
     * @return PublisherAdRequest.Builder The instance of the PublisherAdRequest.Builder that was initially passed in
     */
    @Keep
    public PublisherAdRequest.Builder requestBid(PublisherAdRequest.Builder adRequest) {
        String sizes = "";
        JSONObject keymakerResponse;
        JSONObject bidResponse;

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


        //Do the trinity request
        Keymaker sonobiKeymaker = new Keymaker(bannerAdView.getId(), bannerAdView.getAdUnitId(), sizes, this.extraTrinityParams, "POST");
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
            bidResponse = keymakerResponse.getJSONObject("slots").getJSONObject(sonobiKeymaker.slotKey);
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
