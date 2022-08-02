package org.home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {

    private final static Logger log = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        log.info("Server staring...");
        int port = 4000;
        String webroot = "tmp";
        log.info("Using port: " + port);
        log.info("Using webroot: " + webroot);

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(port, webroot);
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
