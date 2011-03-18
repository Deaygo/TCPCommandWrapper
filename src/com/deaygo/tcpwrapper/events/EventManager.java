package com.deaygo.tcpwrapper.events;

import java.util.HashMap;

public class EventManager
{
	private static HashMap<String, CommandInterface>	commands;

	public static boolean addCommandListener(final String command, final CommandInterface callback)
	{
		return addCommandListener(command, callback, false);
	}

	public static boolean addCommandListener(final String command, final CommandInterface callback, final boolean replaceIfExists)
	{
		if (!replaceIfExists && commands.containsKey(command))
		{
			return false;
		}

		commands.put(command, callback);

		return true;
	}

	public static String fireCommandEvent(final String command, final String[] args)
	{
		if (!commands.containsKey(command))
		{
			return null;
		}

		return commands.get(command).onCommand(command, args);
	}
}
