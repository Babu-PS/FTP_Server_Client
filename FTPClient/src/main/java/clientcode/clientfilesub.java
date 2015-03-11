package clientcode;

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

import org.apache.log4j.Logger;
//import java.util.*;
public class clientfilesub
{
	final static Logger loggeR = Logger.getLogger(clientfilesub.class);
	protected static SSLSocket clientSocket =null;
//	protected static Socket clientSocket =null;
	public static void main(String[] args) 
	{
		int fileNumber = 3;
		try
		{
			System.setProperty("javax.net.ssl.trustStore","truststore.jks");
		    System.setProperty("javax.net.ssl.trustStorePassword","babu@polycom");

		    SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		    clientSocket = (SSLSocket) sslsocketfactory.createSocket(args[0],Integer.parseInt(args[1]));
//			createSocket(args[0],Integer.parseInt(args[1])); // connects to the server
			DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
			int numberOfFiles = readNumberOfFilesRequired(args[2],dataOutputStream);
			InputStream inputStream = clientSocket.getInputStream();			// input stream of bytes
			PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(),true);
			String downloadFolder = args[args.length - 1];
			for(;numberOfFiles != 0; numberOfFiles --)
			{
				String fileName = args[fileNumber]; 
				sendingFileNametoServer(fileName,socketOut);
				fileNumber = fileNumber + 1;			
				DataInputStream socketIn = new DataInputStream(clientSocket.getInputStream());
				loggeR.info("You requested a File named: " + fileName);
				if(!socketIn.readBoolean())			// reads the status of the requested file
				{
//					System.out.println(fileName + " doesn't exists on Server");
					loggeR.error(fileName + " doesn't exists on Server");
					loggeR.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				}
				else
				{
					String result = receiveFile(fileName,downloadFolder,inputStream);
				}
			}
//		    inputStream.close();
		}
		catch(ArrayIndexOutOfBoundsException e)
			{
				loggeR.fatal("This is error", e);
//			    System.out.println("Please Enter Command Line Arguments as Shown Below: \n IP Address -> Port Number -> Number of Files Required -> File Names -> Download Location ");
				loggeR.info("Please Enter Command Line Arguments as Shown Below: \n IP Address -> Port Number -> Number of Files Required -> File Names -> Download Location ");
			}
		catch(Exception x)
			{
				x.printStackTrace();
				loggeR.fatal("OOPS...! You ran into Error ", x);
//				System.out.println("Please Enter Command Line Arguments as Shown Below: \n IP Address -> Port Number -> Number of Files Required -> File Names -> Download Location ");
			}
	}
/*	protected static void createSocket(String string, int parseInt) {
		try 
		{clientSocket = new Socket(string,parseInt);
		} 
		catch (IOException e)
		{e.printStackTrace();}
	}*/
	static String receiveFile(String fileName,String downloadFolder, InputStream inputStream)
	{
			File requestedfile =new File(downloadFolder+ "\\"+fileName);			// creates a new file instance
			if(requestedfile.exists())
//				System.out.println("File Already Exists.. Overriding now...!");
				loggeR.warn("File Already Exists.. Overriding now...!");				
		try
		{
			DataInputStream socketIn = new DataInputStream(clientSocket.getInputStream());
			byte[] filearray = new byte[(int) readFileSize(socketIn)];						// allocates the bytes for the new file	
			FileOutputStream fileOutputStream = new FileOutputStream(requestedfile); // creates a file at the location named as the arguments passed into it.
			fileOutputStream.write(filearray, 0 , allocatingbuffer(filearray,inputStream));									 // write to the file the data stored in the array.
			fileOutputStream.flush();
//			System.out.println(fileName + " is downloaded at location " + downloadFolder  );
			loggeR.info(fileName + " is downloaded at location " + downloadFolder);
			loggeR.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			fileOutputStream.close();							 // close the file writing procedure
			return "Success";
     	}
		catch(Exception e)
		{
			loggeR.error("Error in handling the file obtained from the Server " + e);
			e.printStackTrace();
			return "Failure";
		}
	}
	public static int allocatingbuffer(byte[] filearray, InputStream inputStream) throws Exception
	{
		int bytesRead = inputStream.read(filearray, 0, filearray.length);   //  this variable contains the number of bytes read by this method
	    int current = bytesRead;											//  variable to count the total number of bytes read
	    do
	    {
	        bytesRead = inputStream.read(filearray, current, (filearray.length-current)); // read the next ones if file size is large
	        if(bytesRead >= 0) 
	        	current += bytesRead;													  // increment the count until the read bytes is Zero
	     } while(bytesRead > 0);
		return current;
	}

	static long readFileSize(DataInputStream socketIn) throws Exception 
	{
		long sizeofFile = socketIn.readLong();								// reads the size of the requested file
//		System.out.println("The file size is : " + sizeofFile + " bytes.");
			loggeR.info("The file size is : " + sizeofFile + " bytes.");
		return sizeofFile;
	}

	static void sendingFileNametoServer(String fileName,PrintWriter socketOut)
	{
			socketOut.println(fileName);	//  sends file name to the server
	}

	static int readNumberOfFilesRequired(String numberofFiles,DataOutputStream dataOutputStream)throws Exception
	{
			int numberOfFiles = Integer.parseInt(numberofFiles);
			dataOutputStream.writeInt(numberOfFiles);
			return numberOfFiles;
	}
}