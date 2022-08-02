package org.home;

import org.home.http.HttpResponse;
import org.home.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {

    private final static Logger log = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private final Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = """
                    <html>
                        <head>
                            <title>Simple Java Http Server</title>
                        </head>
                        <body>
                            Hey!
                        </body>
                    </html>
                    """;

            var response = new HttpResponse(HttpStatus.OK, html);

            outputStream.write(response.getBytes());

            log.info("Connection processing finished.");
        } catch (IOException ex) {
            log.error("Problem with communication", ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}
