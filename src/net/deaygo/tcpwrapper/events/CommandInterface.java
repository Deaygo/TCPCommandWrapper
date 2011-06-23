package net.deaygo.tcpwrapper.events;

import net.deaygo.tcpwrapper.ClientHandler;

public interface CommandInterface {
    /**
     * Fires when a command is executed.
     * 
     * @param command
     *            The actual command executed.
     * @param args
     *            The arguments of the command.
     * @return The response to the client, null if no message is to be sent.
     */
    String onCommand(ClientHandler client, String command, String[] args);
}
