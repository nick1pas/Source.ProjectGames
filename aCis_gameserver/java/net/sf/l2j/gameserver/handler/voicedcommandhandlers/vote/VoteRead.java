/*
*This file is for L2J-Acis Interlude Source
*You have to include it in net\sf\l2j\gameserver\network folder, before compilation
*/
package	net.sf.l2j.gameserver.handler.voicedcommandhandlers.vote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class VoteRead 
{
	private static Logger _log = Logger.getLogger(VoteRead.class.getName());

	public static long checkVotedIP(String IP)
	{
		// int count = 0;
		long voteDate = 0;

		URL url = null;
		InputStreamReader isr = null;
		try
		{
			//HERE YOU HAVE TO ENTER YOUR SERVER'S ID
			url = new URL("http://l2top.co/reward/VoteCheck.php?id=1722&ip="+IP);
			
			isr = new InputStreamReader(url.openStream());

			BufferedReader br = new BufferedReader(isr);
			String strLine;
			while((strLine = br.readLine()) != null) // Read File Line By Line
			{
				if(!strLine.equals("FALSE"))
					voteDate = System.currentTimeMillis() / 1000L;

				_log.info("VoteRead: DATE[" + voteDate + "], IP[" + IP + "]");
			}

			isr.close(); // Close the input stream
		}
		catch(Exception e) // Catch exception if any
		{
			//_log.error("VoteRead: ERROR: ", e);
			return 0;
		} 

		return voteDate;
	}
}