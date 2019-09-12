package com.alexredchets.mobile_take_home;

public interface LoadListener {
    void onDataReceived(String data);
    void onError();
}
