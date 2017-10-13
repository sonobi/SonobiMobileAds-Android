package com.sonobi.sonobimobileads;

import android.support.annotation.Keep;
import android.view.View;

/**
 * Created by jgo on 10/11/17.
 */

@Keep
public interface AdListener {
    void onError(String errorMessage);
    void onAdLoaded(View view);
}
