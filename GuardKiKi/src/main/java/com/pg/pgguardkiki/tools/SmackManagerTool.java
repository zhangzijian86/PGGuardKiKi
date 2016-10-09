package com.pg.pgguardkiki.tools;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;
import com.pg.pgguardkiki.data.SystemData;
import com.pg.pgguardkiki.service.ConnectService;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import com.pg.pgguardkiki.tools.ChatProvider;
import com.pg.pgguardkiki.tools.ChatProvider.ChatConstants;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.carbons.Carbon;
import org.jivesoftware.smackx.carbons.CarbonManager;
import org.jivesoftware.smackx.packet.DelayInfo;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;

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
    private final ContentResolver mContentResolver;
    private PacketListener mPacketListener;

    private static final int PACKET_TIMEOUT = 30000;

    public SmackManagerTool(ConnectService service) {
        registerSmackProviders();
        mService = service;
        mConnectionConfig = new ConnectionConfiguration(SystemData.HOST_IP,SystemData.PORT,SystemData.SERVER_NAME);
        mConnectionConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        mConnectionConfig.setSendPresence(true);
        mConnectionConfig.setDebuggerEnabled(true);
        //mConnectionConfig.setSASLAuthenticationEnabled(false);
        mConnection = new XMPPConnection(mConnectionConfig);
        this.mContentResolver = service.getContentResolver();
    }

    public static void registerSmackProviders() {

    }

    public boolean login(String phone, String password){
        Log.d(ClassName, "==11==");
        if (mConnection.isConnected()) {
            mConnection.disconnect();
        }
        Log.d(ClassName, "==22==");
        try {
            SmackConfiguration.setPacketReplyTimeout(PACKET_TIMEOUT);
            SmackConfiguration.setKeepAliveInterval(-1);
            SmackConfiguration.setDefaultPingInterval(0);
            mConnection.connect();
        } catch (XMPPException e) {
            Log.d(ClassName, "Connect Error 0");
            e.printStackTrace();
        }
        Log.d(ClassName, "==33==");
        if (!mConnection.isConnected()) {
            Log.d(ClassName, "Connect Error 1");
            return  false;
        }
        Log.d(ClassName, "==33==");
        try {
            mConnection.login(phone,password);
        } catch (Exception e) {
            Log.d(ClassName, "Login Error");
            return  false;
        }
        Log.d(ClassName, "==44==");
        registerRosterChangeListener();// 监听联系人动态变化
        Log.d(ClassName, "==55==");
        registerNewsChangeListener();
        Log.d(ClassName, "==66==");

        mConnection.addConnectionListener(new ConnectionListener() {
            public void connectionClosedOnError(Exception e) {
                mService.postConnectionFailed(e.getMessage());
            }

            public void connectionClosed() {
            }

            public void reconnectingIn(int seconds) {
            }

            public void reconnectionFailed(Exception e) {
            }

            public void reconnectionSuccessful() {
            }
        });

        if(mConnection.isConnected()&&mConnection.isAuthenticated()){
            registerMessageListener();
        }

        // 更改在綫狀態
        Presence presence = new Presence(Presence.Type.available);
        presence.setMode(Presence.Mode.available);
        mConnection.sendPacket(presence);
        Log.d(ClassName, "==77==");
        return true;
    }

	public void register(String name, String password) {
        Log.d(ClassName, "==register=b=000=");
        if (mConnection.isConnected()) {
            mConnection.disconnect();
        }
        Log.d(ClassName, "==register=b=111=");
        try {
            SmackConfiguration.setPacketReplyTimeout(PACKET_TIMEOUT);
            SmackConfiguration.setKeepAliveInterval(-1);
            SmackConfiguration.setDefaultPingInterval(0);
            mConnection.connect();
        } catch (XMPPException e) {
            Log.d(ClassName, "register Connect Error 0");
            e.printStackTrace();
            
        }
        Log.d(ClassName, "==register=b=222=");
        if (!mConnection.isConnected()) {
            Log.d(ClassName, "register Connect Error 1");
        }
        Registration reg = new Registration();
        reg.setType(IQ.Type.SET);
        reg.setTo(mConnection.getServiceName());
        reg.setUsername(name);// 注意这里createAccount注册时，参数是username，不是jid，是“@”前面的部分。
        reg.setPassword(password);
        reg.addAttribute("android", "geolo_createUser_android");
        PacketFilter filter = new AndFilter(new PacketIDFilter(
                reg.getPacketID()), new PacketTypeFilter(IQ.class));
        PacketCollector collector = mConnection.createPacketCollector(filter);
        mConnection.sendPacket(reg);
        IQ result = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            Log.d(ClassName, "==register=b=444=");
        } else if (result.getType() == IQ.Type.RESULT) {
        } else {
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                Log.d(ClassName, "==register=b=555=IQ.Type.ERROR: "
                        + result.getError().toString());
            } else {
                Log.d(ClassName, "==register=b=666=IQ.Type.ERROR: " + result.getError().toString());
            }
        }
    }


//    public void sendMessage(String toJID, String message) {
//        Log.d(ClassName, "==aa==");
//        //final Message newMessage = new Message("zzj0@zzj/Spark 2.7.0", Message.Type.chat);
//        final Message newMessage = new Message("zzj0@zzj/Spark", Message.Type.chat);
//        newMessage.setBody(message);
//        Log.d(ClassName, "==bb==");
//        newMessage.addExtension(new DeliveryReceiptRequest());
//        if (mConnection.isConnected()&&mConnection.isAuthenticated()) {
//            Log.d(ClassName, "==cc=="+newMessage.getPacketID());
////            addChatMessageToDB(ChatConstants.OUTGOING, "zzj0@zzj/Spark 2.7.0", message,
////                    ChatConstants.DS_SENT_OR_READ, System.currentTimeMillis(),
////                    newMessage.getPacketID());
//            addChatMessageToDB(ChatConstants.OUTGOING, "zzj0@zzj/Spark", message,
//                    ChatConstants.DS_SENT_OR_READ, System.currentTimeMillis(),
//                    newMessage.getPacketID());
//            Log.d(ClassName, "==dd==");
//            mConnection.sendPacket(newMessage);
//            Log.d(ClassName, "==ee==");
//        } else {
//            // send offline -> store to DB
//            Log.d(ClassName, "==ff==");
//            addChatMessageToDB(ChatConstants.OUTGOING, toJID, message,
//                    ChatConstants.DS_NEW, System.currentTimeMillis(),
//                    newMessage.getPacketID());
//        }
//    }

    public void yazhengma(String toJID, String message) {
        Log.d(ClassName, "==aa==");

        Log.d(ClassName, "==sendMessage=b=000=");
        if (mConnection.isConnected()) {
            mConnection.disconnect();
        }
        Log.d(ClassName, "==sendMessage=b=111=");
        try {
            SmackConfiguration.setPacketReplyTimeout(PACKET_TIMEOUT);
            SmackConfiguration.setKeepAliveInterval(-1);
            SmackConfiguration.setDefaultPingInterval(0);
            mConnection.connect();
            mConnection.loginAnonymously();//匿名登陆
            registerMessageListener();
        } catch (XMPPException e) {
            Log.d(ClassName, "sendMessage Connect Error 0");
            e.printStackTrace();

        }
        Log.d(ClassName, "==sendMessage=b=222=");

        if (!mConnection.isConnected()) {
            Log.d(ClassName, "sendMessage Connect Error 1");
        }

        final Message newMessage = new Message("admin@zzj", Message.Type.chat);
        newMessage.setBody("==yazhengma==给服务端的数据==yazhengma==");
        newMessage.addExtension(new DeliveryReceiptRequest());
        mConnection.sendPacket(newMessage);
        //mConnection.disconnect();
    }

    public void sendMessage(String toJID, String message) {
        Log.d(ClassName, "==aa==");
        //final Message newMessage = new Message("zzj0@zzj/Spark 2.7.0", Message.Type.chat);
        //final Message newMessage = new Message("zzj0@zzj/Spark", Message.Type.chat);

//        final Message newMessage = new Message("zzj", Message.Type.chat);
//        newMessage.setBody("==给服务端的数据==");

        final Message newMessage = new Message("specialfrienduser@zzj/Spark", Message.Type.chat);
        //final Message newMessage = new Message("specialfrienduser", Message.Type.chat);
        newMessage.setBody("==给服务端的数据==");

//        final Message newMessage = new Message("zzj0@zzj/Spark", Message.Type.chat);
//        newMessage.setBody("给服务端的数据");

        Log.d(ClassName, "==bb==");
        newMessage.addExtension(new DeliveryReceiptRequest());
        if (mConnection.isConnected()&&mConnection.isAuthenticated()) {
            Log.d(ClassName, "==cc=="+newMessage.getPacketID());
//            addChatMessageToDB(ChatConstants.OUTGOING, "zzj0@zzj/Spark 2.7.0", message,
//                    ChatConstants.DS_SENT_OR_READ, System.currentTimeMillis(),
//                    newMessage.getPacketID());
            addChatMessageToDB(ChatConstants.OUTGOING, "zzj0@zzj/Spark", message,
                    ChatConstants.DS_SENT_OR_READ, System.currentTimeMillis(),
                    newMessage.getPacketID());
            Log.d(ClassName, "==dd==");
            mConnection.sendPacket(newMessage);
            Log.d(ClassName, "==ee==");
        } else {
            // send offline -> store to DB
            Log.d(ClassName, "==ff==");
            addChatMessageToDB(ChatConstants.OUTGOING, toJID, message,
                    ChatConstants.DS_NEW, System.currentTimeMillis(),
                    newMessage.getPacketID());
        }
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

    private void registerNewsChangeListener() {
        // register connection features
        ServiceDiscoveryManager sdm = ServiceDiscoveryManager
                .getInstanceFor(mConnection);
        if (sdm == null)
            sdm = new ServiceDiscoveryManager(mConnection);

        sdm.addFeature("http://jabber.org/protocol/disco#info");

        // reference PingManager, set ping flood protection to 10s
        PingManager.getInstanceFor(mConnection).setPingMinimumInterval(
                10 * 1000);
        // reference DeliveryReceiptManager, add listener

        DeliveryReceiptManager dm = DeliveryReceiptManager
                .getInstanceFor(mConnection);
        dm.enableAutoReceipts();
        dm.registerReceiptReceivedListener(new DeliveryReceiptManager.ReceiptReceivedListener() {
            public void onReceiptReceived(String fromJid, String toJid,
                                          String receiptId) {
                Log.d(ClassName,  "got delivery receipt for " + receiptId);
                changeMessageDeliveryStatus(receiptId, ChatConstants.DS_ACKED);
            }
        });
    }

    public void changeMessageDeliveryStatus(String packetID, int new_status) {
        ContentValues cv = new ContentValues();
        cv.put(ChatConstants.DELIVERY_STATUS, new_status);
        Uri rowuri = Uri.parse("content://" + ChatProvider.AUTHORITY + "/"
                + ChatProvider.TABLE_NAME);
        mContentResolver.update(rowuri, cv, ChatConstants.PACKET_ID
                + " = ? AND " + ChatConstants.DIRECTION + " = "
                + ChatConstants.OUTGOING, new String[] { packetID });
    }

    /************ start 新消息处理 ********************/
    private void registerMessageListener() {
        // do not register multiple packet listeners
        if (mPacketListener != null)
            mConnection.removePacketListener(mPacketListener);

        PacketTypeFilter filter = new PacketTypeFilter(Message.class);

        mPacketListener = new PacketListener() {
            public void processPacket(Packet packet) {
                try {
                    if (packet instanceof Message) {
                        Message msg = (Message) packet;
                        String chatMessage = msg.getBody();

                        // try to extract a carbon
                        Carbon cc = CarbonManager.getCarbon(msg);
                        if (cc != null
                                && cc.getDirection() == Carbon.Direction.received) {
                            Log.d(ClassName, "carbon: " + cc.toXML());
                            msg = (Message) cc.getForwarded()
                                    .getForwardedPacket();
                            chatMessage = msg.getBody();
                            // fall through
                        } else if (cc != null
                                && cc.getDirection() == Carbon.Direction.sent) {
                            Log.d(ClassName, "carbon: " + cc.toXML());
                            msg = (Message) cc.getForwarded()
                                    .getForwardedPacket();
                            chatMessage = msg.getBody();
                            if (chatMessage == null)
                                return;
                            String fromJID = getJabberID(msg.getTo());

                            addChatMessageToDB(ChatConstants.OUTGOING, fromJID,
                                    chatMessage, ChatConstants.DS_SENT_OR_READ,
                                    System.currentTimeMillis(),
                                    msg.getPacketID());
                            // always return after adding
                            return;
                        }

                        if (chatMessage == null) {
                            return;
                        }

                        if (msg.getType() == Message.Type.error) {
                            chatMessage = "<Error> " + chatMessage;
                        }

                        long ts;
                        DelayInfo timestamp = (DelayInfo) msg.getExtension(
                                "delay", "urn:xmpp:delay");
                        if (timestamp == null)
                            timestamp = (DelayInfo) msg.getExtension("x",
                                    "jabber:x:delay");
                        if (timestamp != null)
                            ts = timestamp.getStamp().getTime();
                        else
                            ts = System.currentTimeMillis();

                        String fromJID = getJabberID(msg.getFrom());

                        addChatMessageToDB(ChatConstants.INCOMING, fromJID,
                                chatMessage, ChatConstants.DS_NEW, ts,
                                msg.getPacketID());
                        Log.d(ClassName, "==mService.newMessage==" + fromJID+"==="+chatMessage);
                        mService.newMessage(fromJID, chatMessage);
//                        final Message newMessage = new Message("zzj0@zzj/Smack", Message.Type.chat);
//                        newMessage.setBody("返回的数据啊啊啊啊啊");
//                        newMessage.addExtension(new DeliveryReceiptRequest());
//                        if (mConnection.isConnected()&&mConnection.isAuthenticated()) {
//                            mConnection.sendPacket(newMessage);
//                        }
                    }
                } catch (Exception e) {
                    // SMACK silently discards exceptions dropped from
                    // processPacket :(
                    Log.d(ClassName, "failed to process packet:");
                    e.printStackTrace();
                }
            }
        };

        mConnection.addPacketListener(mPacketListener, filter);
    }

    private String getJabberID(String from) {
        String[] res = from.split("/");
        return res[0].toLowerCase();
    }

    private void addChatMessageToDB(int direction, String JID, String message,
                                    int delivery_status, long ts, String packetID) {
        ContentValues values = new ContentValues();

        values.put(ChatConstants.DIRECTION, direction);
        values.put(ChatConstants.JID, JID);
        values.put(ChatConstants.MESSAGE, message);
        values.put(ChatConstants.DELIVERY_STATUS, delivery_status);
        values.put(ChatConstants.DATE, ts);
        values.put(ChatConstants.PACKET_ID, packetID);

        mContentResolver.insert(ChatProvider.CONTENT_URI, values);
    }
}
