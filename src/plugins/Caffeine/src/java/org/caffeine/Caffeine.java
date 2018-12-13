package org.caffeine;


import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Packet;

import java.io.File;



public class Caffeine implements PacketInterceptor, Plugin {
    private static final Logger Log = LoggerFactory.getLogger(Caffeine.class);
    private InterceptorManager interceptorManager;

    public Caffeine() {
        interceptorManager = InterceptorManager.getInstance();
    }

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register the interceptor
        interceptorManager.addInterceptor(this);
        Log.info("========= Muks: init plugin: init() ============");
    }

    @Override
    public void destroyPlugin() {
        // unregister the interceptor
        interceptorManager.removeInterceptor(this);
        Log.info("========= Muks: init plugin: destroy() ============");
    }

    @Override
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed)
        throws PacketRejectedException {
        if (!processed) {
            // process packet
            Log.info("========= Muks: init plugin: Intercepted() ============");
        }
    }
}
