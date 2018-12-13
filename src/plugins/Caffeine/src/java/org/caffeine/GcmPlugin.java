//package org.caffeine;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//import java.util.TimerTask;
////
////import org.apache.http.client.ClientProtocolException;
////import org.apache.http.client.fluent.Form;
////import org.apache.http.client.fluent.Request;
////import org.apache.http.entity.ContentType;
//import org.jivesoftware.openfire.PresenceManager;
//import org.jivesoftware.openfire.XMPPServer;
//import org.jivesoftware.openfire.container.Plugin;
//import org.jivesoftware.openfire.container.PluginManager;
//import org.jivesoftware.openfire.interceptor.InterceptorManager;
//import org.jivesoftware.openfire.interceptor.PacketInterceptor;
//import org.jivesoftware.openfire.interceptor.PacketRejectedException;
//import org.jivesoftware.openfire.session.Session;
//import org.jivesoftware.openfire.user.User;
//import org.jivesoftware.openfire.user.UserManager;
//import org.jivesoftware.openfire.user.UserNameManager;
//import org.jivesoftware.openfire.user.UserNotFoundException;
//import org.jivesoftware.util.JiveGlobals;
//import org.jivesoftware.util.PropertyEventListener;
//import org.jivesoftware.util.TaskEngine;
//import org.muks.gcm.EventObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.xmpp.packet.JID;
//import org.xmpp.packet.Message;
//import org.xmpp.packet.Packet;
//import org.xmpp.packet.Presence;
//
////import com.google.gson.Gson;
//
//public class GcmPlugin implements Plugin, PacketInterceptor {
//
//    private static final Logger Log = LoggerFactory.getLogger(GcmPlugin.class);
//    private static final String URL = "plugin.gcmh.url";
//    private static final String MODE = "plugin.gcmh.mode";
//    private static final String DEBUG = "plugin.gcmh.debug";
//
//    public static final String MODE_ALL = "1";
//    public static final String MODE_OFFLINE = "2";
//    public static final String MODE_NO_MOBILE = "3";
//    public static final String MODE_EXCEPTION = "4";
//
//    public static final String DEBUG_ON = "1";
//    public static final String DEBUG_OFF = "2";
//
//
//    public GcmPlugin() {
//        interceptorManager = InterceptorManager.getInstance();
//    }
//
//    private InterceptorManager interceptorManager;
//    private XMPPServer mServer;
//    private PresenceManager mPresenceManager;
//    private UserManager mUserManager;
//    //private Gson mGson;
//
//    public void initializePlugin(PluginManager manager, File pluginDirectory) {
//        Log.info("GCM Plugin started");
//
//        initConf();
//        mServer = XMPPServer.getInstance();
//        mPresenceManager = mServer.getPresenceManager();
//        mUserManager = mServer.getUserManager();
//        //mGson = new Gson();
//
//        interceptorManager.addInterceptor(this);
//        Log.info("========= Muks: init plugin ============");
//    }
//
//    public void destroyPlugin() {
//        Log.info("GCM Plugin destroyed");
//        Log.info("========= Muks: init plugin ============");
//        interceptorManager.removeInterceptor(this);
//    }
//
//    public void interceptPacket(Packet packet, Session session,
//                                boolean incoming, boolean processed) throws PacketRejectedException {
//
//        if (processed) {
//            return;
//        }
//        if (!incoming) {
//            return;
//        }
//
//        if (packet instanceof Message) {
//            Message msg = (Message) packet;
//            process(msg);
//            Log.info("========= Muks: init plugin ============");
//        }
//        Log.info("========= Muks: init plugin ============");
//    }
//
//    private void process(final Message msg) {
//        Log.info("========= Muks: init plugin ============");
//        if(mDebug)Log.info("GCM Plugin process() called");
//        try {
//            if (checkTarget(msg)) {
//                if(mDebug)Log.info("GCM Plugin Check=true");
//                TimerTask messageTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        sendExternalMsg(msg);
//                    }
//                };
//                TaskEngine.getInstance().schedule(messageTask, 20);
//            } else {
//                if(mDebug)Log.info("GCM Plugin Check=false");
//            }
//        } catch (UserNotFoundException e) {
//            Log.error("GCM Plugin (UserNotFoundException) Something went reeeaaaaally wrong");
//            e.printStackTrace();
//            // Something went reeeaaaaally wrong if you end up here!!
//        }
//    }
//
//    private boolean checkTarget(Message msg) throws UserNotFoundException {
//        if(msg.getBody() == null || msg.getBody().equals("")){
//            return false;
//        }
//
//        JID toJID = msg.getTo().asBareJID();
//        if(mDebug)Log.info("GCM Plugin check() called");
//
//        if(!toJID.getDomain().contains(mServer.getServerInfo().getXMPPDomain())){
//            return false;
//        }
//
//        if (mMode.equalsIgnoreCase(GcmPlugin.MODE_ALL)) {
//            return true;
//        } else if (mMode.equalsIgnoreCase(GcmPlugin.MODE_OFFLINE)) {
//
//            String y = UserNameManager.getUserName(toJID);
//            if(mDebug)Log.info("GCM Plugin getUserName(...) = " + y);
//            User x = mUserManager.getUser(y);
//            if(mDebug)Log.info("GCM Plugin getUser(...) = " + x.toString());
//            try{
//                Presence z = mPresenceManager.getPresence(x);
//                if(z == null) return true;
//                if(mDebug)Log.info("GCM Plugin getPresence(...) = " + z.toString());
//                return !z.isAvailable();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        } else if (mMode.equalsIgnoreCase(GcmPlugin.MODE_NO_MOBILE)) {
//
//        } else if (mMode.equalsIgnoreCase(GcmPlugin.MODE_EXCEPTION)) {
//
//        }
//
//        return true;
//    }
//
//    private void sendExternalMsg(Message msg) {
//        if(mDebug)Log.info("GCM Plugin sendExternalMsg() called");
//
//        if(mURL == null){
//            Log.error("GCM Plugin: URL is null");
//            return;
//        }
//
//        EventObject temp = new EventObject();
//        temp.setBody(msg.getBody());
//        temp.setTo(msg.getTo().toBareJID());
//        temp.setFrom(msg.getFrom().toBareJID());
//        Log.info(temp.toString());
//        Log.info("========= Muks: init plugin ============");
//
////        try {
////            if(mDebug){
////                String x = Request
////                    .Post(mURL)
//////				.bodyForm(Form.form().add("data", mGson.toJson(temp)).build())
////                    .bodyString(mGson.toJson(temp), ContentType.APPLICATION_JSON)
////                    .execute()
////                    .returnContent().asString();
////                Log.info("GCM Plugin sendMsg(): "+x);
////            } else {
////                Request
////                    .Post(mURL)
//////				.bodyForm(Form.form().add("data", mGson.toJson(temp)).build())
////                    .bodyString(mGson.toJson(temp), ContentType.APPLICATION_JSON)
////                    .execute();
////            }
////        } catch (ClientProtocolException e) {
////            Log.error("GCM Plugin: ClientProtocolException");
////            Log.error("GCM Plugin: " + e.getMessage());
////            e.printStackTrace();
////        } catch (IOException e) {
////            Log.error("GCM Plugin: IOException");
////            Log.error("GCM Plugin: " + e.getMessage());
////            e.printStackTrace();
////        } catch (Exception e){
////            Log.error("GCM Plugin: (Unknown)Exception");
////            Log.error("GCM Plugin: " + e.getMessage());
////            e.printStackTrace();
////        }
//    }
//
//
//    String mURL, mMode;
//    boolean mDebug = false;
//
//    private void initConf() {
//        mURL = this.getURL();
//        mMode = this.getMode();
//        if (this.getDebug()) {
//            mDebug = true;
//        } else {
//            mDebug = false;
//        }
//    }
//
//    public void setURL(String pURL) {
//        JiveGlobals.setProperty(URL, pURL);
//        initConf();
//    }
//
//    public String getURL() {
//        return JiveGlobals.getProperty(URL, null);
//    }
//
//    public String getMode() {
//        return JiveGlobals.getProperty(MODE, GcmPlugin.MODE_ALL);
//    }
//
//    public void setMode(String mode) {
//        JiveGlobals.setProperty(MODE, mode);
//        initConf();
//    }
//
//    public void setDebug(boolean mode) {
//        if(mode){
//            JiveGlobals.setProperty(DEBUG, GcmPlugin.DEBUG_ON);
//        } else {
//            JiveGlobals.setProperty(DEBUG, GcmPlugin.DEBUG_OFF);
//        }
//        initConf();
//    }
//
//    public boolean getDebug() {
//        if(JiveGlobals.getProperty(DEBUG, GcmPlugin.DEBUG_OFF).equalsIgnoreCase(GcmPlugin.DEBUG_ON)){
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//
//}
