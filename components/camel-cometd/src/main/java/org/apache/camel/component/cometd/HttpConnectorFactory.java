package org.apache.camel.component.cometd;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;


/**
 * Playing around with Jetty 9
 * See http://git.eclipse.org/c/jetty/org.eclipse.jetty.project.git/tree/examples/embedded/src/main/java/org/eclipse/jetty/embedded/ManyConnectors.java
 */

class HttpConnectorFactory {

    ServerConnector httpConnector(Server server) {
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig()));
        http.setPort(8080);
        http.setIdleTimeout(30000);
        return http;
    }

    ServerConnector httpsConnector(Server server, SslContextFactory sslContextFactory) {
        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());
        ServerConnector https = new ServerConnector(server, sslConnectionFactory, new HttpConnectionFactory(httpsConfig()));
        https.setPort(8443);
        https.setIdleTimeout(500000);
        return https;
    }

    // You can pass the old HttpConfiguration object as an
    // argument to effectively clone the contents. On this HttpConfiguration object we add a
    // SecureRequestCustomizer which is how a new connector is able to resolve the https connection before
    // handing control over to the Jetty Server.
    private static HttpConfiguration httpsConfig() {
        HttpConfiguration config = httpConfig();
        config.addCustomizer(new SecureRequestCustomizer());
        return config;
    }

    // HTTP Configuration
    // HttpConfiguration is a collection of configuration information
    // appropriate for http and https. The default scheme for http is
    // <code>http</code> of course, as the default for secured http is
    // <code>https</code> but we show setting the scheme to show it can be
    // done. The port for secured communication is also set here.
    private static HttpConfiguration httpConfig() {
        HttpConfiguration config = new HttpConfiguration();
        config.setSecureScheme("https");
        config.setSecurePort(8443);
        config.setOutputBufferSize(32768);
        return config;
    }
}
