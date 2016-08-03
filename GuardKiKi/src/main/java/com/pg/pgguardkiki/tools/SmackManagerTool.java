package com.pg.pgguardkiki.tools;

import android.util.Log;
import com.pg.pgguardkiki.data.SystemData;
import com.pg.pgguardkiki.service.ConnectService;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;

/**
 * Created by zzj on 16-7-29.
 */
public class SmackManagerTool{
    private static final String ClassName = "SmackManagerTool";
    private XMPPConnection mConnection;
    private ConnectService mService;
    private static ConnectionConfiguration  mConnectionConfig;
    private Roster mRoster;
    private RosterListener mRosterListener;

    private static final int PACKET_TIMEOUT = 30000;

    public SmackManagerTool(ConnectService service) {

        registerSmackProviders();

        mService = service;
        mConnectionConfig = new ConnectionConfiguration(SystemData.HOST_IP,SystemData.PORT,SystemData.SERVER_NAME);
        mConnectionConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        mConnectionConfig.setSendPresence(true);
        mConnectionConfig.setDebuggerEnabled(true);
        mConnection = new XMPPConnection(mConnectionConfig);
    }

    public static void registerSmackProviders() {

    }

    public boolean login(String phone, String password){
        if (mConnection.isConnected()) {
            mConnection.disconnect();
        }
        try {
            SmackConfiguration.setPacketReplyTimeout(PACKET_TIMEOUT);
            SmackConfiguration.setKeepAliveInterval(-1);
            SmackConfiguration.setDefaultPingInterval(0);
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
        registerRosterChangeListener();// 监听联系人动态变化

        // 更改在綫狀態
        Presence presence = new Presence(Presence.Type.available);
        presence.setMode(Presence.Mode.available);
        mConnection.sendPacket(presence);

        return true;
    }

    private void registerRosterChangeListener() {
        mRoster = mConnection.getRoster();
        mRosterListener = new RosterListener() {
            @Override
            public void presenceChanged(Presence presence) {
                Log.d(ClassName, "presenceChanged 0= "+presence.getFrom());
            }

            @Override
            public void entriesUpdated(Collection<String> entries) {
                Log.d(ClassName, "entriesUpdated 1= "+entries);
            }

            @Override
            public void entriesDeleted(Collection<String> entries) {
                Log.d(ClassName, "entriesUpdated 2= "+entries);
            }

            @Override
            public void entriesAdded(Collection<String> entries) {
                Log.d(ClassName, "entriesAdded 2= "+entries);
            }
        };
        mRoster.addRosterListener(mRosterListener);
    }
}
