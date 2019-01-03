package org.caffeine;

import org.jivesoftware.openfire.MessageRouter;
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
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;
import java.io.File;


public class Caffeine implements PacketInterceptor, Plugin {
    private Logger LOG = LoggerFactory.getLogger(Caffeine.class);
    private static String TAG = "[Caffeine]: ";
    private InterceptorManager interceptorManager;

    private XMPPServer mServer;
    private PresenceManager mPresenceManager;
    private UserManager mUserManager;
    private MessageRouter msgRouter;

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
        msgRouter = mServer.getMessageRouter();

        LOG.info("");
        LOG.info("======================================================");
        LOG.info("{} init plugin: init()", TAG);

        JiveGlobals.setProperty("Mukthar", "Ahmed");
    }

    @Override
    public void destroyPlugin() {
        // unregister the interceptor
        interceptorManager.removeInterceptor(this);
        LOG.info("{} init plugin: destroyPlugin()", TAG);
        LOG.info("======================================================");
        LOG.info("");

    }

    @Override
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed)
        throws PacketRejectedException {
        LOG.info("");
        LOG.info("======================================================");

        LOG.info("JivesGlobal property: " + JiveGlobals.getProperty("Mukthar"));

        Message msg = (Message) packet;
        if (packet instanceof Message) {
            msgProcessor(msg, incoming, processed);
            LOG.info("JivesGlobal property: " + JiveGlobals.getProperty("Mukthar"));



        }
        LOG.info("{} init plugin: InterceptionEnd()", TAG);
        LOG.info("======================================================");
        LOG.info("");
    }

    private void msgProcessor(Message message, boolean isIncoming, boolean isProcessed) {
        if (isIncoming)
            incomingMsgProcessor(message, isProcessed);
        else
            outgoingMsgProcessor(message);
    }


    private void incomingMsgProcessor(Message message, boolean isProcessed) {
        LOG.info("InComing From: " + getFromJID(message) + ", To: " + getToJID(message) + ", " + message.getBody());
        if (!isProcessed) {
            LOG.info("Packet instance of Message, will be processing now...");
            processMessage(message);
        } else {
            LOG.info("Msg is already PROCESSED...");
        }

    }

    private void outgoingMsgProcessor(Message message) {
        LOG.info("OutGOING From: " + getFromJID(message) + ", To: " + getToJID(message) + ", " + message.getBody());
    }

    private void processMessage(Message msg) {
        LOG.info("Processing...");

        if (hasMessage(msg)) {
            LOG.info("IsChatMsg: " + getMessageType(msg).equals(Message.Type.valueOf("chat")));
            LOG.info("To: {}, From: {}", getToJID(msg), getFromJID(msg));

            parseRespond(msg);

//            TimerTask messageTask = new TimerTask() {
//                @Override
//                public void run() {
//                    sendExternalMsg(msg, incoming);
//                }
//            };
//            TaskEngine.getInstance().schedule(messageTask, 20);

        } else {
            LOG.info("Message body is empty/null..");
        }

    }

    private boolean hasMessage(Message msg) {
        return (msg.getBody() != null);
    }

    private Message.Type getMessageType(Message msg) {
        return msg.getType();
    }

    private JID getToJID(Message msg) {
        return msg.getTo();
    }

    private JID getFromJID(Message msg) {
        return msg.getFrom();
    }

    private void processChat() {

    }

    private void replyToChat() {

    }

    private boolean registerChat() {

        return true;
    }

    private boolean isUserOnline(JID toJID) throws UserNotFoundException {
        LOG.info("{} JID={}", TAG, toJID.toFullJID());
        String toUserName = UserNameManager.getUserName(toJID);
        LOG.info("ToUserName: {}", toUserName);
        User toUser = mUserManager.getUser(toUserName);
        LOG.info("User: {}", toUser.toString());

        Presence toUserPresenceState = mPresenceManager.getPresence(toUser);
        if (toUserPresenceState == null) return true;
        LOG.info("getPresence() = " + toUserPresenceState.toString());
        return toUserPresenceState.isAvailable();
    }


    private void parseRespond(Message msg) {
        if (msg.getBody().equalsIgnoreCase("hi")) {
            msg.setBody("Transformed to - Hello msg");
        }

        if (msg.getBody().equalsIgnoreCase("hello")) {
            msg.setBody("Transformed to - Hi msg");
        }
    }

    private Message sendExternalMsg(Message msg, boolean incoming) {

        String msgRoute = "";
        if (incoming)
            msgRoute = "Sending INCOMING msg";
        else
            msgRoute = "Sending OUTGOING msg";

        EventObject temp = new EventObject();
        temp.setBody(msg.getBody());
        temp.setTo(msg.getTo().toBareJID());
        temp.setFrom(msg.getFrom().toBareJID());

        LOG.info(msgRoute + "-> " + temp.toString());

        LOG.info("========= Muks: init plugin ============");
        return msg;
    }

}
