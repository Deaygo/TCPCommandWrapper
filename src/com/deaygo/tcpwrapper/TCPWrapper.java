package com.deaygo.tcpwrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import jline.ConsoleReader;

import org.deaygo.util.encryption.EncryptionManager;

import com.deaygo.tcpwrapper.events.EventManager;
import com.deaygo.tcpwrapper.util.AppConfig;

public class TCPWrapper implements Runnable
{
	public static void main(final String args[])
	{
		final TCPWrapper wrap = new TCPWrapper();
		System.out.println("Config File: " + AppConfig.getConfPath());
		System.out.println("Port Loaded: " + AppConfig.getPort());
		System.out.println("Auto-start Loaded: " + AppConfig.isAutoStart());

		try
		{
			final ConsoleReader consoleReader = new ConsoleReader();
			String line;
			final PrintWriter out = new PrintWriter(System.out);

			while ((line = consoleReader.readLine("> ")) != null)
			{
				if (line.equalsIgnoreCase("quit"))
				{
					wrap.close();
					break;
				}
				out.println("======>\"" + line + "\"");
				out.flush();
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	private final EncryptionManager	encryption	= new EncryptionManager();

	private ServerSocket			socket;
	private Thread					acceptThread;
	public EventManager             events = new EventManager();
	private volatile boolean		aborting	= false;

	public TCPWrapper()
	{

		AppConfig.init();
		if (AppConfig.isAutoStart())
		{
			start();
		}
	}

	public void close()
	{
		synchronized (this)
		{
			aborting = true;
		}

		// close the server socket
		if (socket != null)
		{
			try
			{
				socket.close();
				synchronized (this)
				{
					socket = null;
				}
			}
			catch (final IOException e)
			{
				// ignore
			}
		}

		// close the handlers
		// for (int i = 0; i < connectionHandlers.size(); i++)
		// {
		// TCPConnectionHandler handler =
		// (TCPConnectionHandler) connectionHandlers.elementAt(i);
		// handler.close();
		// }
	}

	@Override
	public void run()
	{
		final int port = AppConfig.getPort();

		try
		{
			socket = new ServerSocket(port);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

		while (!aborting)
		{
			try
			{
				final Socket sock = socket.accept();
				final Thread thread = new Thread(new ClientHandler(sock, this));
				thread.start();
			}
			catch (final IOException e)
			{
				if (!aborting)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void start()
	{
		acceptThread = new Thread(this);
		acceptThread.start();
	}
}
