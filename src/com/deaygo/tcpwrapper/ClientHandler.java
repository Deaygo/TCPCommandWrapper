package com.deaygo.tcpwrapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable
{
	Socket	sock;

	public ClientHandler(final Socket sock)
	{
		this.sock = sock;
	}

	@Override
	public void run()
	{
		BufferedInputStream is = null;
		BufferedOutputStream os = null;
		OutputStreamWriter osw = null;
		try
		{
			is = new BufferedInputStream(sock.getInputStream());
			os = new BufferedOutputStream(sock.getOutputStream());
			osw = new OutputStreamWriter(os, "US-ASCII");
		}
		catch (final IOException e1)
		{
			if (sock != null)
			{
				try
				{
					sock.close();
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
			e1.printStackTrace();
			return;
		}
		final InputStreamReader isr = new InputStreamReader(is);
		while (sock.isConnected() && !sock.isClosed())
		{
			try
			{
				int ch;
				StringBuffer process = new StringBuffer();
				while (true)
				{
					ch = isr.read();

					if (ch == '\r')
					{
						continue;
					}
					else if (ch == '\n')
					{
						break;
					}
					process.append((char) ch);
				}

				System.out.println(process);

				if (process.toString().equalsIgnoreCase("quit"))
				{
					sock.close();
					break;
				}

				final String returnCode = "MultipleSocketServer repsonded\r\n";
				osw.write(returnCode);
				osw.flush();

				process = null;
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			is.close();
			isr.close();
			osw.close();
			os.close();
			sock = null;
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
