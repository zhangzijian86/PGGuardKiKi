package com.pg.pgguardkiki.tools;

import android.util.Log;
import com.pg.pgguardkiki.data.SystemData;
import com.pg.pgguardkiki.service.ConnectService;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

/**
 * Created by zzj on 16-7-29.
 */
public class SmackManagerTool{
    private static final String ClassName = "SmackManagerTool";
    private XMPPTCPConnection mConnection;
    private ConnectService mService;
    private static XMPPTCPConnectionConfiguration.Builder  mConnectionConfig;

    public SmackManagerTool(ConnectService service) {

        registerSmackProviders();

        mService = service;
        mConnectionConfig = XMPPTCPConnectionConfiguration.builder();
        //设置openfire主机IP
        mConnectionConfig.setHost(SystemData.HOST_IP);
        //设置openfire服务器名称
        mConnectionConfig.setServiceName(SystemData.SERVER_NAME);
        //设置端口号：默认5222
        mConnectionConfig.setPort(SystemData.PORT);
        mConnectionConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        mConnectionConfig.setSendPresence(true);
        mConnectionConfig.setDebuggerEnabled(true);
        mConnection = new XMPPTCPConnection(mConnectionConfig.build());
    }

    public boolean login(String phone, String password){
        if (mConnection.isConnected()) {
            mConnection.disconnect();
        }
        try {
            mConnection.connect();
        } catch (Exception e) {
            Log.d(ClassName, "Connect Error 0");
            return  false;
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
        return true;
    }

    public static void registerSmackProviders() {

    }

}
