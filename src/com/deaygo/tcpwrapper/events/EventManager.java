package com.deaygo.tcpwrapper.events;

import java.util.HashMap;

import com.deaygo.tcpwrapper.ClientHandler;

public class EventManager
{
	private HashMap<String, CommandInterface>	commands = new HashMap<String, CommandInterface>();
	private CommandInterface disconnect = null;

	public boolean addCommandListener(final String command, final CommandInterface callback)
	{
		return addCommandListener(command, callback, false);
	}
	
	public boolean addDisconnectListener(final CommandInterface callback)
    {
	    disconnect = callback;
	    return true;
    }

	public boolean addCommandListener(final String command, final CommandInterface callback, final boolean replaceIfExists)
	{
		if (!replaceIfExists && commands.containsKey(command))
		{
			return false;
		}

		commands.put(command, callback);

		return true;
	}

	public String fireCommandEvent(ClientHandler client, final String command, final String[] args)
	{
		if (!commands.containsKey(command))
		{
			return null;
		}

		return commands.get(command).onCommand(client, command, args);
	}
	
	public void fireDisconnectEvent(ClientHandler client)
	{
	    if ( disconnect != null )
	        disconnect.onCommand(client, null, null);
	}
}
