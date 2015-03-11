package clientcode;

import java.io.*;
import java.nio.ByteBuffer;

import org.junit.*;

import clientcode.clientfilesub;
public class ClientTest {

	clientfilesub clientProgram=new clientfilesub(); 
	@Test
	public void bufferallocationtest() throws Exception
	{	
		
		InputStream in = new ByteArrayInputStream("My string for testing".getBytes());
		long expected=in.available();
		byte[] filearray = new byte[(int)expected];
		long actual = clientfilesub.allocatingbuffer(filearray, in);
		Assert.assertEquals(expected,actual);
	}
	@Test
	public void readingNumberOfFilesTest() throws Exception
	{
		String Expected = "3";
		ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(BAOS);
		int actual = clientfilesub.readNumberOfFilesRequired(Expected,dataOutputStream);
		byte[] expected =  BAOS.toByteArray();
		Assert.assertEquals(Integer.parseInt(Expected),actual);
	}
	@Test
	public void readingFileSizeTest() throws Exception
	{
		long value = 123858585l;
		ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.putLong(value);
		DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(buffer.array()));
		long Expected = clientfilesub.readFileSize(dataInputStream);
		Assert.assertEquals(Expected,value);
	}
	@Test
	public void sendingFileNametoServerTest() throws Exception
	{
		String actual  = "new.txt";
		OutputStream baos = new ByteArrayOutputStream();
		PrintWriter printWriter = new PrintWriter(baos,true);
		clientfilesub.sendingFileNametoServer("new.txt",printWriter);
		String Expected = baos.toString();
		Assert.assertEquals(Expected,actual+"\r\n");
	}
}
