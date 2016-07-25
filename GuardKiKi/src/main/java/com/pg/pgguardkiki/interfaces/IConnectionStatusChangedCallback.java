package com.pg.pgguardkiki.interfaces;

/**
 * Created by zzj on 16-7-25.
 */
public interface IConnectionStatusChangedCallback {
    public void connectionStatusChanged(int connectedState, String reason);
}
