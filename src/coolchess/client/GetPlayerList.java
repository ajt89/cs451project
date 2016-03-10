package coolchess.client;

import java.util.ArrayList;

import junit.framework.TestCase;

public class GetPlayerList extends TestCase {
	
	String username1 = "aj";
	String username2 = "carl";
	String hostname = "AJ-PC";
	String response1;
	String response2;
	String expected1 = username2;
	String expected2 = username1;
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
			ch1.clearPlayerlist();
			ch1.raw("PLAYERLIST");
			ch1.setResponse();
			System.out.println(ch1.getResponse());
			String[] responseIn1 = ch1.getResponse().split(" ");
			int listSize1 = Integer.parseInt(responseIn1[1]);
			for (int i = 0; i < listSize1; i++){
				ch1.setResponse();
				String response = ch1.getResponse();
				if (response.contains("PLAYERS")){
					String[] responseSplit = response.split(" ");
					ch1.setPlayerlist(responseSplit[1]);
				}
			}
			ch2.clearPlayerlist();
			ch2.raw("PLAYERLIST");
			ch2.setResponse();
			System.out.println(ch2.getResponse());
			String[] responseIn2 = ch2.getResponse().split(" ");
			int listSize2 = Integer.parseInt(responseIn2[1]);
			for (int i = 0; i < listSize2; i++){
				ch2.setResponse();
				String response = ch2.getResponse();
				if (response.contains("PLAYERS")){
					String[] responseSplit = response.split(" ");
					ch2.setPlayerlist(responseSplit[1]);
				}
			}
			ArrayList<String> pl1 = new ArrayList<String>();
			ArrayList<String> pl2 = new ArrayList<String>();
			pl1 = ch1.getPlayerlist();
			pl2 = ch2.getPlayerlist();
			response1 = pl1.get(0);
			response2 = pl2.get(0);
			ch1.QUIT();
			ch2.QUIT();
		} catch(Exception e){
			System.out.println(e);
		}
	}
	public void test(){
		assertEquals(expected1,response1);
		assertEquals(expected2,response2);
	}
}
