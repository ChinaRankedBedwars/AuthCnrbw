package iafenvoy.authcnrbw;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.IOException;

@Plugin(id = "authcnrbw", name = "AuthCnrbw", version = "1.0.0", authors = {"IAFEnvoy"})
public class AuthCnrbw {
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public AuthCnrbw(ProxyServer server, Logger logger) throws IOException {
        this.server = server;
        this.logger = logger;
        LocalServer.start();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.server.getEventManager().register(this, new CodeEventHandler());
    }
}
