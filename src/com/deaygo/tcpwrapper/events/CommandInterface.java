package com.deaygo.tcpwrapper.events;

public interface CommandInterface
{
	/**
	 * Fires when a command is executed.
	 * 
	 * @param command
	 *            The actual command executed.
	 * @param args
	 *            The arguments of the command.
	 * @return The response to the client, null if no message is to be sent.
	 */
	String onCommand(String command, String[] args);
}
