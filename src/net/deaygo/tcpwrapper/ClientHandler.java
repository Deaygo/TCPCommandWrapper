package net.deaygo.tcpwrapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket sock;
    TCPWrapper wrapper;
    BufferedInputStream is = null;
    BufferedOutputStream os = null;
    OutputStreamWriter osw = null;
    Console console;

    public ClientHandler(final Socket sock, final TCPWrapper wrapper) {
        console = System.console();
        this.sock = sock;
        this.wrapper = wrapper;
    }

    @Override
    public void run() {

        try {
            is = new BufferedInputStream(sock.getInputStream());
            os = new BufferedOutputStream(sock.getOutputStream());
            osw = new OutputStreamWriter(os, "US-ASCII");
        } catch (final IOException e1) {
            if (sock != null) {
                try {
                    sock.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            e1.printStackTrace();
            return;
        }
        final InputStreamReader isr = new InputStreamReader(is);
        while ((sock != null) && sock.isConnected() && !sock.isClosed()) {
            try {
                int ch;
                StringBuffer process = new StringBuffer();
                while (true) {
                    ch = isr.read();

                    if (ch == -1) {
                        break;
                    }

                    if (ch == '\r') {
                        continue;
                    } else if (ch == '\n') {
                        break;
                    }
                    process.append((char) ch);
                }

                System.out.println(process);
                /*
                if (process.toString().equalsIgnoreCase("quit"))
                {
                	sock.close();
                	break;
                }*/

                int loc = process.indexOf(" ");
                if (loc == -1) {
                    loc = process.length();
                }
                final String command = process.substring(0, loc);

                if (loc + 1 > process.length()) {
                    loc = process.length();
                } else {
                    loc++;
                }

                process.replace(0, loc, "");
                final String[] args = process.toString().split(" ");

                System.out.println("Command: " + command);

                String returnCode = wrapper.events.fireCommandEvent(this, command, args);
                if (returnCode == null) {
                    returnCode = "";
                }
                osw.write(returnCode + "\r\n");
                osw.flush();

                process = null;
            } catch (final Exception e) {
                try {
                    sock.close();
                } catch (final IOException e1) {

                }
                wrapper.events.fireDisconnectEvent(this);
                sock = null;
            }
        }

        try {
            is.close();
            isr.close();
            osw.close();
            os.close();
            sock = null;
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(final String message) {

        try {
            osw.write(message + "\r\n");
            osw.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
