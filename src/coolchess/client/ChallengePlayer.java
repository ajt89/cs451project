package coolchess.client;

import junit.framework.TestCase;

public class ChallengePlayer extends TestCase {
	
	String username1 = "aj";
	String username2 = "carl";
	String hostname = "AJ-PC";
	String response;
	String expected = username1 + ": challenge " + username2;
	int port = 6969;
	protected void setUp(){
		ClientHelper ch1 = new ClientHelper(hostname,port);
		ClientHelper ch2 = new ClientHelper(hostname,port);
		try{
			ch1.connect();
			ch1.setResponse();
			ch1.user(username1);
			ch1.setResponse();
			ch2.connect();
			ch2.setResponse();
			ch2.user(username2);
			ch2.setResponse();
			
			ch1.raw("challenge " + username2);
			ch2.setResponse();
			response = ch2.getResponse();
			
			ch1.QUIT();
			ch2.QUIT();
		} catch(Exception e){
			System.out.println(e);
		}
	}
	public void test(){
		assertEquals(expected,response);
	}
}
