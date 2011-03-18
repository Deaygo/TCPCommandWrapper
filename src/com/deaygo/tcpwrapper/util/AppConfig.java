package com.deaygo.tcpwrapper.util;

import org.deaygo.util.file.PropertiesFileManager;

public final class AppConfig extends PropertiesFileManager
{
	public static String getIvKey()
	{
		return get("ivKey");
	}

	public static int getPort()
	{
		return getAsInt("port");
	}

	public static String getSecretKey()
	{
		return get("secretKey");
	}

	public static void init()
	{
		setConfPath("conf/server.properties");
		load();
		defaults.setProperty("port", "1234");
		defaults.setProperty("autoStart", "false");
		defaults.setProperty("encryptionEnabled", "false");
		defaults.setProperty("secretKey", "");
		defaults.setProperty("ivKey", "");
	}

	public static boolean isAutoStart()
	{
		return getAsBoolean("autoStart");
	}

	public static boolean isEncryptionEnabled()
	{
		return getAsBoolean("encryptionEnabled");
	}

	public static boolean load()
	{
		if (!PropertiesFileManager.load())
		{
			properties.setProperty("port", "12345");
			properties.setProperty("autoStart", "false");
			properties.setProperty("encryptionEnabled", "false");
			properties.setProperty("secretKey", "");
			properties.setProperty("ivKey", "");
			return save();
		}

		return true;
	}
}
