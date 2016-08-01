package com.pg.pgguardkiki.tools;

import android.util.Log;
import com.pg.pgguardkiki.data.SystemData;
import com.pg.pgguardkiki.service.ConnectService;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

/**
 * Created by zzj on 16-7-29.
 */
public class SmackManagerTool{
    private static final String ClassName = "SmackManagerTool";
    private XMPPConnection mConnection;
    private ConnectService mService;
    private static ConnectionConfiguration  mConnectionConfig;

    public SmackManagerTool(ConnectService service) {

        registerSmackProviders();

        mService = service;
        mConnectionConfig = new ConnectionConfiguration(SystemData.HOST_IP,SystemData.PORT,SystemData.SERVER_NAME);
        mConnectionConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        mConnectionConfig.setSendPresence(true);
        mConnectionConfig.setDebuggerEnabled(true);
        mConnection = new XMPPConnection(mConnectionConfig);
    }

    public boolean login(String phone, String password){
        if (mConnection.isConnected()) {
            mConnection.disconnect();
        }
        try {
            mConnection.connect();
        } catch (XMPPException e) {
            Log.d(ClassName, "Connect Error 0");
            e.printStackTrace();
        }
        if (!mConnection.isConnected()) {
            Log.d(ClassName, "Connect Error 1");
            return  false;
        }
        try {
            mConnection.login(phone,password);
        } catch (Exception e) {
            Log.d(ClassName, "Login Error");
            return  false;
        }
        // 更改在綫狀態
        Presence presence = new Presence(Presence.Type.available);
        mConnection.sendPacket(presence);

        return true;
    }

    public static void registerSmackProviders() {

    }

}
