package com.freeautoproxy.core;

import java.util.Comparator;

public class ProxyItemComparator implements Comparator<ProxyItem> {

    @Override
    public int compare(ProxyItem o1, ProxyItem o2) {
        return ((Long) o1.RespondMilliSeconds).compareTo((Long) o2.RespondMilliSeconds);
    }

}
