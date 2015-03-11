package servercode;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.junit.*;
public class ServerTest 
{
/*	ThreadedServer serverprogram = new ThreadedServer();
	@Test
	public void listeningAtPortTest() throws Exception
	{
		int PortAddress  = 5555;
		String actual = ThreadedServer.listeningAtPort("C:\\Server", PortAddress);
		Assert.assertEquals("Failure",actual); // Run twice in order to raise Exception so the case succeeds.
	}*/
	final Socket socket = mock(Socket.class);
	WorkerThread workerThread = new WorkerThread(socket, "C:\\Server");
	@Test
	public void filePreparationTest() throws Exception
	{
		File requestedFile = new File("C:\\Server\\new.txt");
		byte[] arrayoffile = new byte[(int)requestedFile.length()];
		FileInputStream fileinputstream = new FileInputStream(requestedFile);
		fileinputstream.read(arrayoffile,0,arrayoffile.length);
		ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(BAOS);
		byte[] result = new byte[(int)requestedFile.length()];
		result = workerThread.filePreparation("new.txt",dataOutputStream);
//		byte[] expected =  BAOS.toByteArray();
		Assert.assertArrayEquals(arrayoffile, result);
	}
	@Test
	public void fileExistenceTest() throws Exception
	{
		boolean result = workerThread.fileExistenceCheck("new.txt");
				Assert.assertEquals(true,result); // if file exists
	}
private Socket mock(Class<Socket> class1) {
	// TODO Auto-generated method stub
	return new Socket();
}
	@Test
	public void fileTransferTest() throws Exception
	{
		File requestedFile = new File("C:\\Server\\new.txt");
		byte[] arrayoffile = new byte[(int)requestedFile.length()];
		FileInputStream fileinputstream = new FileInputStream(requestedFile);
		fileinputstream.read(arrayoffile,0,arrayoffile.length);
		OutputStream os = new ByteArrayOutputStream();
		byte[] expected = ((ByteArrayOutputStream) os).toByteArray();
		Assert.assertEquals("Done...!",workerThread.fileTransfer("new.txt", arrayoffile,os));
		fileinputstream.close();
	}
	@Test
	public void handleClientTest() throws Exception
	{
		InputStream IS = new ByteArrayInputStream("new.txt".getBytes());
		int value = 1;
		ByteBuffer buffer = ByteBuffer.allocate(4);
	    buffer.putInt(value);
		DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(buffer.array()));
		InputStreamReader inputStreamReader = new InputStreamReader(IS);
		DataOutputStream dataOutputStream = new DataOutputStream(new ByteArrayOutputStream());
		OutputStream outputStream = new ByteArrayOutputStream();
		byte[] expected = ((ByteArrayOutputStream) outputStream).toByteArray();
		Assert.assertEquals("Success",workerThread.handleClient(dataInputStream,inputStreamReader, dataOutputStream,outputStream));
	}
}
