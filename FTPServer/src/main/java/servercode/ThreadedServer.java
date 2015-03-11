package servercode;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.*;

import org.apache.log4j.Logger;
//

public class ThreadedServer
{
	static int PortAddress;
	static String ServerFolder;
	final static Logger logger = Logger.getLogger(ThreadedServer.class);
	public static void main(String[] args)
	{
			try
			{
			if(args.length == 0)
				{PathandPort("C:\\Server",5555);
				logger.info("You had not entered any arguments. Default port address(5555) and Access folder(C:\\Server) is selected.");}
			else
				PathandPort(args[1],Integer.parseInt(args[0]));
			String result = listeningAtPort(ServerFolder,PortAddress);
			}
			catch(ArrayIndexOutOfBoundsException x)
			{
//				System.out.println("Please Enter Command Line Arguments as Shown Below: \nPort Number -> Server Location (Directory) ");
				logger.fatal("Please Enter Command Line Arguments as Shown Below: \nPort Number -> Server Location (Directory) ");
			}
	}
static String listeningAtPort(String ServerFolder, int PortAddress)
{
		try
		{	
			 System.setProperty("javax.net.ssl.keyStore","keyStore.jks");
			 System.setProperty("javax.net.ssl.keyStorePassword","babu@polycom");
			 SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		     SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(PortAddress);
//			ServerSocket socketServer = new ServerSocket(PortAddress); // port Number to listen for the clients
			System.out.println("Listening on Port  " + PortAddress);
			logger.info("Listening on Port  " + PortAddress);
			while(true)
			{
//				Socket connection = socketServer.accept(); // waits here until connection is obtained and creates object when a client gets connected
				 SSLSocket connection = (SSLSocket) sslserversocket.accept();
//				System.out.println("Client connected from: " +connection.getInetAddress() +" and accesses from folder " + ServerFolder);
				logger.info("Client connected from: " +connection.getInetAddress() +"// and accesses from folder " + ServerFolder);
				WorkerThread newThread = new WorkerThread(connection, ServerFolder); // creates an object for the thread of class
				Thread thread = new Thread(newThread, "Client");       // new thread created with arguments of "runnable object" and "name"
				thread.start(); 									   // initiates the running of created thread
			}
		}
		catch (Exception e) 
		{
			logger.fatal("Sorry......! Exception occured. Details: ");
			e.printStackTrace();
			return "Failure";
		}
	}
private static  void PathandPort(String Folder, int portnumber)
{
	ThreadedServer.ServerFolder = Folder;
	ThreadedServer.PortAddress = portnumber;
}
}