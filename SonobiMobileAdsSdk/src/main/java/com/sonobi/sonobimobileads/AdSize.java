package com.sonobi.sonobimobileads;

import android.support.annotation.Keep;

/**
 * Created by jgo on 10/11/17.
 */
@Keep
public class AdSize {

    public int width;
    public int height;

    public AdSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String getSize() {
        return this.width + "x" + this.height;
    }

}
