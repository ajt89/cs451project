package coolchess.client;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;

public class DenyChallenge extends TestCase {

	String username1 = "aj";
	String username2 = "carl";
	String hostname = "AJ-PC";
	String response;
	String expected = username2 + " " + username1 + " denied";
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
			
			ch1.raw(username1 + " challenge " + username2);
			ch2.setResponse();
			ch2.setResponse();
			ch2.raw(username1 + " denied");
			ch1.setResponse();
			ch1.setResponse();
			ch1.setResponse();
			response = ch1.getResponse();
			System.out.println(response);
			
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
