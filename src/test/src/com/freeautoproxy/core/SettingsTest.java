package com.freeautoproxy.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    @Test
    @DisplayName("Check filling Settings data")
    void test() {
        assertFalse(Settings.getAppVersion().isEmpty());
        assertNotEquals(0, Settings.getAmountThreads());
        assertNotEquals(0, Settings.getAwaitTestProxy());
        assertNotEquals(0, Settings.getAwaitGetProxy());
        assertNotEquals(0, Settings.getCapacityProxiesQueue());
        assertFalse(Settings.getProxyUrls().isEmpty());
        assertFalse(Settings.getTestByUrls().isEmpty());
        assertFalse(Settings.getUserAgent().isEmpty());
        assertNotEquals(0, Settings.getUrlConnectionTimeOut());
        assertNotNull(Settings.getTimeZone());

    }
}