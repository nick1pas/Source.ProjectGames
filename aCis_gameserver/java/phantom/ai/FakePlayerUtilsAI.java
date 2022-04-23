package phantom.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

import phantom.FakePlayerConfig;
import phantom.FakePlayer;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.util.Broadcast;
import net.sf.l2j.util.Rnd;

public class FakePlayerUtilsAI
{
	private static ArrayList<String> _fakesTellPhrases = new ArrayList<String>();
    private static ArrayList<String> _fakesPeacePhrases = new ArrayList<String>();
	
    public static void load()
    {
    	_fakesTellPhrases.clear();

        parseFile("tell", _fakesTellPhrases);
        parseFile("peace", _fakesPeacePhrases);
    }
    
    private static void parseFile(String file_name, ArrayList<String> phrases)
    {
        LineNumberReader lnr = null;
        BufferedReader br = null;
        FileReader fr = null;
        try 
        {
            File Data = new File("./config/phantom/chat/" + file_name + ".talk");
            if (!Data.exists())
            {
                return;
            }

            fr = new FileReader(Data);
            br = new BufferedReader(fr);
            lnr = new LineNumberReader(br);
            String line;
            while((line = lnr.readLine()) != null)
            {
                if (line.trim().length() == 0 || line.startsWith("#"))
                    continue;

                phrases.add(line);
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fr != null)
                {
                    fr.close();
                }
                if (br != null) 
                {
                    br.close();
                }
                if (lnr != null)
                {
                    lnr.close();
                }
            }
            catch (Exception e1)
            {
            	
            }
        }
    }
   
	public static int getRandomClan()
	{
		return FakePlayerConfig.LIST_CLAN_ID.get(Rnd.get(FakePlayerConfig.LIST_CLAN_ID.size()));
	}
	
	public static void answerPlayers(L2PcInstance sender, FakePlayer receiver, String text)
	{
		ThreadPoolManager.getInstance().scheduleAi(new Runnable()
		{
			@Override
			public void run()
			{
				sender.sendPacket(new CreatureSay(receiver.getObjectId(), Say2.TELL, receiver.getName(), getRandomTellPhrase()));
			}
			
		}, Rnd.get(10, 50) * 1000);
	}
	
	public static void maybeAnnounce(FakePlayer fake)
	{
		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				if (Rnd.get(1, 1000000) <= FakePlayerConfig.FAKE_CHANCE_TO_TALK_SOCIAL)
				{
					Broadcast.toSelfAndKnownPlayers(fake, new CreatureSay(fake.getObjectId(), Rnd.chance(80) ? Say2.SHOUT : Say2.ALL, fake.getName(), FakePlayerUtilsAI.getRandomPeacePhrase()));
				}
			}
		}, Rnd.get(10, 50) * 1000, Rnd.get(10, 50) * 1000);
	}
	
    public static String getRandomTellPhrase() 
    {
    	return _fakesTellPhrases.get(Rnd.get(_fakesTellPhrases.size()));
    }
    
    public static String getRandomPeacePhrase() 
    {
    	return _fakesPeacePhrases.get(Rnd.get(_fakesPeacePhrases.size()));
    }
}