package com.sonobi.sonobimobileads;

import android.support.annotation.Keep;

/**
 * Created by jgo on 10/13/17.
 */

/**
 * Configurations for the triniry request
 * Available Options:
 * timeout - time in milliseconds willing to wait for the trinity request to respond
 * testMode - enable or disable the test cookie
 */
public class SonobiConfig {

    private boolean testMode = false;
    private int timeout = 5000;

    @Keep
    public int getTimeout() {
        return timeout;
    }

    @Keep
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Keep
    public boolean isTestMode() {
        return testMode;
    }

    @Keep
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }
}
