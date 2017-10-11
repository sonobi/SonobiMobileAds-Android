package com.sonobi.sonobimobileads;

import android.view.View;

/**
 * Created by jgo on 10/11/17.
 */

public interface AdListener {
    void onError(String errorMessage);
    void onAdLoaded(View view);
}
