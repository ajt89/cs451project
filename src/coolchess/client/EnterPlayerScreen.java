package coolchess.client;

import junit.framework.TestCase;

public class EnterPlayerScreen extends TestCase {
	
	String username = "aj";
	String hostname = "AJ-PC";
	String response;
	String expected = "Username accepted";
	int port = 6969;
	protected void setUp(){
		ClientHelper ch = new ClientHelper(hostname,port);
		try{
			ch.connect();
			ch.setResponse();
			ch.user(username);
			ch.setResponse();
			response = ch.getResponse();
			ch.QUIT();
		} catch(Exception e){
			System.out.println(e);
		}
	}
	public void test(){
		assertEquals(expected,response);
	}
}
