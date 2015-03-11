package servercode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Runnable; 
import java.net.Socket;
import org.apache.log4j.Logger;

public class WorkerThread implements Runnable
{
				private Socket connection;	
				private String path;
				public WorkerThread(Socket connection, String Path)
				{
					this.connection = connection; 			// Global object directed to private object in this class
					path = Path;
				}
				final static Logger logger = Logger.getLogger(ThreadedServer.class);
				public void runfromhere()
				{
					try
					{
					DataInputStream datainputstream = new DataInputStream(connection.getInputStream());
					InputStreamReader inputstreamreader = new InputStreamReader(connection.getInputStream());
					DataOutputStream dataoutputstream = new DataOutputStream(connection.getOutputStream()); // writes data to the connected client
					OutputStream outputStream = connection.getOutputStream();   // accepts the input and sends to the client waiting on the socket
					handleClient(datainputstream,inputstreamreader,dataoutputstream,outputStream);
					}
					catch(Exception x)
					{
					 logger.fatal("Exception in run " +x);
					  x.printStackTrace();  
					}
				}
				protected String handleClient(DataInputStream datainputstream,InputStreamReader inputstreamreader, DataOutputStream dataoutputstream,OutputStream outputStream)	  
				{
					try
					{
						int numberoffiles = datainputstream.readInt();
						BufferedReader bufferreader = new BufferedReader(inputstreamreader); // Buffers the input data
						while(numberoffiles > 0) 
						{
							String fileName = bufferreader.readLine();   //attribute to store the File name
//							System.out.println("Requested a file named " + "\"" + fileName + "\"");
							logger.info("Client requested a file, named " + "\"" + fileName + "\"");
				//			String publicRoot = "C:\\Server\\";  // Trying to give source folder from command Arguments
							numberoffiles -- ;
							boolean dummy = fileExistenceCheck(fileName);
							dataoutputstream.writeBoolean(dummy);
							if(dummy)
								fileExists(fileName, dataoutputstream,outputStream);
							else
								fileDoesNotExist(fileName);
						}
						return "Success";
					}
					catch(Exception e)
					{
						  logger.fatal("Server ran into Exception. Details are: ");
						  e.printStackTrace();
						  return "Failure";
					}
				}
				protected void fileDoesNotExist(String requestedFile)
				{
					logger.warn("sorry " + requestedFile + " is not Available with server");
				}
				protected void fileExists(String requestedFile,DataOutputStream dataoutputstream,OutputStream outputStream)
				{
					logger.info(requestedFile + " is available with server. Calculating file size...");
					String result = fileTransfer(requestedFile,filePreparation(requestedFile,dataoutputstream), outputStream);
					logger.info(result);
				}	
				boolean fileExistenceCheck(String fileName)
				{
					File requestedFile = new File(path+"\\"+fileName);
					return (requestedFile.exists());	
				}
				byte[] filePreparation(String fileName,DataOutputStream dataoutputstream)
				{
					try
					{	
						File requestedFile = new File(path+"\\"+fileName);
						byte[] arrayoffile = new byte[(int)requestedFile.length()]; // allocating bytes according to the size of file
						dataoutputstream.writeLong(arrayoffile.length);     // Sends to the client about the size of the file
						FileInputStream fileinputstream = new FileInputStream(requestedFile); // reading bytes from a file <String Provided>
						BufferedInputStream buffer = new BufferedInputStream(fileinputstream); // buffer to hold the read input
						buffer.read(arrayoffile,0,arrayoffile.length); 		// writes into the allocated location from the buffer
//					    dataoutputstream.writeLong(arrayoffile.length);     // Sends to the client about the size of the file
					    logger.info("File Size sent to Client");
					    return arrayoffile;
					}		
					catch(Exception x)
					{
						logger.fatal("Exception in Preparing files "+x);
						x.printStackTrace();
						return null;
					}					
				}
				protected String fileTransfer(String fileName, byte[] arrayoffile,OutputStream outputStream)
				{
					try
					{
					    logger.info("Sending " + fileName + " of size (" + arrayoffile.length + "bytes )");	
					    outputStream.write(arrayoffile,0,arrayoffile.length);  // Writes to the Client on the requested file
					    outputStream.flush(); // Any information left in buffer should be written to the output
					    return "Done...!";
					}
					catch(Exception e)
					{
						logger.fatal("Exception in Transferring File "+e);
						e.printStackTrace();
						return "Fail";
					}
				}
				public void run()      // reaches here when a start is initiated
				{
				  	this.runfromhere(); // direct to working part of the server 
				 }
}