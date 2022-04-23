package phantom.ai.walker;

import phantom.FakePlayer;
import phantom.FakePlayerConfig;
import phantom.ai.FakePlayerAI;
import phantom.ai.FakePlayerUtilsAI;

import net.sf.l2j.gameserver.GeoData;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.Location;
import net.sf.l2j.util.Rnd;

public class CitizenAI extends FakePlayerAI
{
	public CitizenAI(FakePlayer character)
	{
		super(character);
		setup();
	}
	
	@Override
	public void setup()
	{
		_fakePlayer.setIsRunning(true);
	}
	
	@Override
	public void thinkAndAct()
	{
		setBusyThinking(true);
		handleDeath();
		ThreadPoolManager.getInstance().scheduleAi(new Runnable()
		{
			@Override
			public void run()
			{
				getFake().despawnPlayer();
			}
			
		}, Rnd.get(FakePlayerConfig.DESPAWN_CITIZEN_RANDOM_TIME_1 * 60 * 1000, FakePlayerConfig.DESPAWN_CITIZEN_RANDOM_TIME_2 * 60 * 1000));
		
		FakePlayerUtilsAI.maybeAnnounce(getFake());
		
		//Goddard
		if (!(getFake().isInsideRadius(147725, -56517, 10000, true)))
		{
			backToCityCenter(getFake());
		}
		
		//Giran
		/*
		if (!(getFake().isInsideRadius(82370, 148335, 10000, true)))
		{
			backToCityCenter(getFake());
		}
		*/
		
    	if (Rnd.get(1, 1000000) <= FakePlayerConfig.FAKE_SOCIAL_CHANCE)
    	{
    		switch (Rnd.get(0, 1))
    		{
    			case 0:
    			{
    				if ((Rnd.get(1, 1000000) <= FakePlayerConfig.FAKE_SIT_CHANCE) && !getFake().isSitting()) 
    				{
    					ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable()
    					{
    						@Override
    						public void run()
    						{
    							getFake().sitDown();
    						}
    						
    					}, Rnd.get(10, 50) * 1000, Rnd.get(10, 50) * 1000);
    				}
    				else if((Rnd.get(1, 1000000) <= FakePlayerConfig.FAKE_SIT_CHANCE) && getFake().isSitting())
    				{
    					ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable()
    					{
    						@Override
    						public void run()
    						{
    							getFake().standUp();
    						}
    						
    					}, Rnd.get(10, 50) * 1000, Rnd.get(10, 50) * 1000);
    				}
    				break;
    			}
    			case 1:
    			{
    				startWalk(getFake());
    			}
    		}
    		return;
    	}
	}
	
	public FakePlayer getFake()
	{
		return _fakePlayer;
	}
	
	public static void backToCityCenter(FakePlayer fake)
	{
		int radius = Rnd.get(-150, 150);
		fake.setRunning();
		if (GeoData.getInstance().canMoveFromToTarget(fake.getX(), fake.getY(), fake.getZ(), 147725, -56517, -2775))
		{
			fake.getAI().setIntention(CtrlIntention.MOVE_TO, new Location(147725 + radius, -56517 + radius, -2775));
		}
		/*
		if (GeoData.getInstance().canMoveFromToTarget(fake.getX(), fake.getY(), fake.getZ(), 82370, 148335, -3472))
		{
			fake.getAI().setIntention(CtrlIntention.MOVE_TO, new Location(82370 + radius, 148335 + radius, -3472));
		}
		*/
	}
	
	public static void startWalk(FakePlayer paramPlayer)
	{
		ThreadPoolManager.getInstance().scheduleGeneral(new PhantomWalk(paramPlayer), Rnd.get(5200, 48540));
	}

	static class PhantomWalk implements Runnable
	{
		FakePlayer _phantom;
		
		public PhantomWalk(FakePlayer paramPlayer)
		{
			_phantom = paramPlayer;
		}
		
		@Override
		public void run()
		{
			if (!_phantom.isDead() || !_phantom.isSitting())
			{
				if (_phantom.isSpawnProtected())
					_phantom.setProtection(false);
				
				_phantom.randomWalk();
				startWalk(_phantom);
			}
		}
	}
}