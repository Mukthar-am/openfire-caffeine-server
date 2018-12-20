package org.caffeine;

import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNameManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.TaskEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;
import java.io.File;
import java.util.TimerTask;


public class Caffeine implements PacketInterceptor, Plugin {
    private Logger LOG = LoggerFactory.getLogger(Caffeine.class);
    private static String TAG = "[Caffeine]: ";
    private InterceptorManager interceptorManager;

    private XMPPServer mServer;
    private PresenceManager mPresenceManager;
    private UserManager mUserManager;

    public Caffeine() {
        interceptorManager = InterceptorManager.getInstance();
    }

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register the interceptor
        interceptorManager.addInterceptor(this);
        mServer = XMPPServer.getInstance();
        mPresenceManager = mServer.getPresenceManager();
        mUserManager = mServer.getUserManager();
        LOG.info("{} init plugin: init()", TAG);
    }

    @Override
    public void destroyPlugin() {
        // unregister the interceptor
        interceptorManager.removeInterceptor(this);
        LOG.info("{} init plugin: destroyPlugin()", TAG);

    }

    @Override
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed)
        throws PacketRejectedException {
        if (!processed) {
            // process packet
            LOG.info("{} Msg NOT processed yet!", TAG);
            LOG.info(packet.toXML() + "\n");
        }

        if (processed) {
            LOG.info("{} Processed the message");
            LOG.info(packet.toXML());
            return;
        }

        if (!incoming)
            LOG.info("{}, Incoming msg: {}", TAG, packet.toXML());
        else
            LOG.info("{}, Outgoing msg: {}", TAG, packet.toXML());

        if (packet instanceof Message) {
            LOG.info("{} Packet instance of Message, will be processing now. ");
            Message msg = (Message) packet;
            process(msg);

        }
        LOG.info("{} init plugin: InterceptionEnd()", TAG);
    }

    private void process(final Message msg) {
        LOG.info("{} Processing...");
        try {
            if (checkTarget(msg)) {
                LOG.info("GCM Plugin Check=true");
                TimerTask messageTask = new TimerTask() {
                    @Override
                    public void run() {
                        sendExternalMsg(msg);
                    }
                };
                TaskEngine.getInstance().schedule(messageTask, 20);
            } else {
                LOG.info("GCM Plugin Check=false");
            }
        } catch (UserNotFoundException e) {
            LOG.error("GCM Plugin (UserNotFoundException) Something went reeeaaaaally wrong");
            e.printStackTrace();
            // Something went reeeaaaaally wrong if you end up here!!
        }
    }

    private String getMessageType(Message msg) {

        return null;
    }

    private void processChat() {

    }

    private void replyToChat() {

    }

    private boolean registerChat() {

        return true;
    }

    private boolean checkTarget(Message msg) throws UserNotFoundException {
        if (msg.getBody() == null || msg.getBody().equals("")) {
            return false;
        }

        JID toJID = msg.getTo().asBareJID();
        LOG.info("{} JID={}", TAG, toJID.toFullJID());
        if (!toJID.getDomain().contains(mServer.getServerInfo().getXMPPDomain())) {
            LOG.info("Returning from here.");
            return false;
        }

        String y = UserNameManager.getUserName(toJID);
        LOG.info("GCM Plugin getUserName(...) = " + y);
        User x = mUserManager.getUser(y);
        LOG.info("GCM Plugin getUser(...) = " + x.toString());
        try {
            Presence z = mPresenceManager.getPresence(x);
            if (z == null) return true;
            LOG.info("GCM Plugin getPresence(...) = " + z.toString());
            return !z.isAvailable();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private void sendExternalMsg(Message msg) {
        LOG.info("GCM Plugin sendExternalMsg() called");

        EventObject temp = new EventObject();
        temp.setBody(msg.getBody());
        temp.setTo(msg.getTo().toBareJID());
        temp.setFrom(msg.getFrom().toBareJID());
        LOG.info(temp.toString());
        LOG.info("========= Muks: init plugin ============");

//        try {
//            if(mDebug){
//                String x = Request
//                    .Post(mURL)
////				.bodyForm(Form.form().add("data", mGson.toJson(temp)).build())
//                    .bodyString(mGson.toJson(temp), ContentType.APPLICATION_JSON)
//                    .execute()
//                    .returnContent().asString();
//                Log.info("GCM Plugin sendMsg(): "+x);
//            } else {
//                Request
//                    .Post(mURL)
////				.bodyForm(Form.form().add("data", mGson.toJson(temp)).build())
//                    .bodyString(mGson.toJson(temp), ContentType.APPLICATION_JSON)
//                    .execute();
//            }
//        } catch (ClientProtocolException e) {
//            Log.error("GCM Plugin: ClientProtocolException");
//            Log.error("GCM Plugin: " + e.getMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.error("GCM Plugin: IOException");
//            Log.error("GCM Plugin: " + e.getMessage());
//            e.printStackTrace();
//        } catch (Exception e){
//            Log.error("GCM Plugin: (Unknown)Exception");
//            Log.error("GCM Plugin: " + e.getMessage());
//            e.printStackTrace();
//        }
    }


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
}
