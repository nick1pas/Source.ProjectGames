package net.sf.l2j.gameserver.model.entity.events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.util.Broadcast;

public class AutoVoteReward
{
	protected static final Logger _log = Logger.getLogger(AutoVoteReward.class.getName());

	//protected List<String> already_rewarded;

	private int _l2networkVotesCount = 0;
	private int _hopzoneVotesCount = 0;
	private int _topzoneVotesCount = 0;
	protected List<String> _already_rewarded;

	protected static boolean _l2network = false;
	protected static boolean _topzone = false;
	protected static boolean _hopzone = false;

	private static HashMap<String, Integer> playerIps = new HashMap<>();

	private AutoVoteReward()
	{
		_log.info("Vote Reward - System Initiated.");

		if (_hopzone)
		{
			int hopzone_votes = getHopZoneVotes();

			if (hopzone_votes == -1)
			{
				hopzone_votes = 0;
			}

			setHopZoneVoteCount(hopzone_votes);
		}

		if (_topzone)
		{
			int topzone_votes = getTopZoneVotes();

			if (topzone_votes == -1)
			{
				topzone_votes = 0;
			}

			setTopZoneVoteCount(topzone_votes);
		}

		if (_l2network)
		{
			int l2network_votes = getL2NetworkVotes();

			if (l2network_votes == -1)
			{
				l2network_votes = 0;
			}

			setL2NetworkVoteCount(l2network_votes);
		}

		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoReward(), Config.VOTES_SYSYEM_INITIAL_DELAY, Config.VOTES_SYSYEM_STEP_DELAY);
	}

	protected class AutoReward implements Runnable
	{
		@Override
		public void run()
		{
			final int minutes = (Config.VOTES_SYSYEM_STEP_DELAY / 1000) / 60;

			if (_hopzone)
			{
				final int hopzone_votes = getHopZoneVotes();

				if (hopzone_votes != -1)
				{
					_log.info("HopZone: Server Votes: " + hopzone_votes);
					Broadcast.gameAnnounceToOnlinePlayers("HopZone: Actual votes are " + hopzone_votes + "...");

					if (hopzone_votes != 0 && hopzone_votes >= getHopZoneVoteCount() + Config.VOTES_FOR_REWARD)
					{
						_already_rewarded = new ArrayList<>();

						Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().values();

						Broadcast.gameAnnounceToOnlinePlayers("HopZone: Great Work! Check your inventory for Reward!!");

						for (L2PcInstance p : pls)
						{
							boolean canReward = false;
							String pIp = p.getClient().getConnection().getInetAddress().getHostAddress();
							if (playerIps.containsKey(pIp))
							{
								int count = playerIps.get(pIp);
								if (count < Config.VOTE_DUALBOXES_ALLOWED)
								{
									playerIps.remove(pIp);
									playerIps.put(pIp, count+1);
									canReward = true;
								}
							}
							else
							{
								canReward = true;
								playerIps.put(pIp, 1);
							}
							if (canReward)
							{
								for (int i = 0; i < Config.VOTES_REWARDS_LIST.length; i++)
								{
									p.addItem("Vote reward.", Config.VOTES_REWARDS_LIST[i][0], Config.VOTES_REWARDS_LIST[i][1], p, true);
								}
							}
							else
							{
								p.sendMessage("Already " + Config.VOTE_DUALBOXES_ALLOWED + " character(s) of your ip have been rewarded, so this character won't be rewarded.");
							}
						}
						playerIps.clear();
						setHopZoneVoteCount(hopzone_votes);
					}
					Broadcast.gameAnnounceToOnlinePlayers("HopZone: Next reward in " + minutes + " minutes at " + (getHopZoneVoteCount() + Config.VOTES_FOR_REWARD) + " votes!!");
					Broadcast.gameAnnounceToOnlinePlayers("Site Web: " + Config.SERVER_WEB_SITE);
				}
			}

			if (_topzone && _hopzone && _l2network  && Config.VOTES_SYSYEM_STEP_DELAY > 0)
			{
				try
				{
					Thread.sleep(Config.VOTES_SYSYEM_STEP_DELAY / 2);
				}
				catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			if (_topzone)
			{
				final int topzone_votes = getTopZoneVotes();

				if (topzone_votes != -1)
				{
					_log.info("TopZone: Server Votes: " + topzone_votes);
					Broadcast.gameAnnounceToOnlinePlayers("TopZone: Actual votes are " + topzone_votes + "...");

					if (topzone_votes != 0 && topzone_votes >= getTopZoneVoteCount() + Config.VOTES_FOR_REWARD)
					{
						_already_rewarded = new ArrayList<>();

						Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().values();

						Broadcast.gameAnnounceToOnlinePlayers("TopZone: Great Work! Check your inventory for Reward!!");

						for (L2PcInstance p : pls)
						{
							boolean canReward = false;
							String pIp = p.getClient().getConnection().getInetAddress().getHostAddress();
							if (playerIps.containsKey(pIp))
							{
								int count = playerIps.get(pIp);
								if (count < Config.VOTE_DUALBOXES_ALLOWED)
								{
									playerIps.remove(pIp);
									playerIps.put(pIp, count+1);
									canReward = true;
								}
							}
							else
							{
								canReward = true;
								playerIps.put(pIp, 1);
							}
							if (canReward)
							{
								for (int i = 0; i < Config.VOTES_REWARDS_LIST.length; i++)
								{
									p.addItem("Vote reward.", Config.VOTES_REWARDS_LIST[i][0], Config.VOTES_REWARDS_LIST[i][1], p, true);
								}
							}
							else
							{
								p.sendMessage("Already " + Config.VOTE_DUALBOXES_ALLOWED + " character(s) of your ip have been rewarded, so this character won't be rewarded.");
							}
						}
						playerIps.clear();
						setTopZoneVoteCount(topzone_votes);
					}
					Broadcast.gameAnnounceToOnlinePlayers("TopZone: Next reward in " + minutes + " minutes at " + (getTopZoneVoteCount() + Config.VOTES_FOR_REWARD) + " Votes!!");
					Broadcast.gameAnnounceToOnlinePlayers("Site Web: " + Config.SERVER_WEB_SITE);
				}
			}

			if (_topzone && _hopzone && _l2network && Config.VOTES_SYSYEM_STEP_DELAY > 0)
			{
				try
				{
					Thread.sleep(Config.VOTES_SYSYEM_STEP_DELAY / 2);
				}
				catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			if (_l2network)
			{
				final int l2network_votes = getL2NetworkVotes();

				if (l2network_votes != -1)
				{
					_log.info("L2NetWork: Server Votes: " + l2network_votes);
					Broadcast.gameAnnounceToOnlinePlayers("L2Network: Actual votes are " + l2network_votes + "...");

					if (l2network_votes != 0 && l2network_votes >= getL2NetworkVoteCount() + Config.VOTES_FOR_REWARD)
					{
						_already_rewarded = new ArrayList<>();

						Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().values();

						Broadcast.gameAnnounceToOnlinePlayers("L2Network: Great Work! Check your inventory for Reward!!");

						for (L2PcInstance p : pls)
						{
							boolean canReward = false;
							String pIp = p.getClient().getConnection().getInetAddress().getHostAddress();
							if (playerIps.containsKey(pIp))
							{
								int count = playerIps.get(pIp);
								if (count < Config.VOTE_DUALBOXES_ALLOWED)
								{
									playerIps.remove(pIp);
									playerIps.put(pIp, count+1);
									canReward = true;
								}
							}
							else
							{
								canReward = true;
								playerIps.put(pIp, 1);
							}
							if (canReward)
							{
								for (int i = 0; i < Config.VOTES_REWARDS_LIST.length; i++)
								{
									p.addItem("Vote reward.", Config.VOTES_REWARDS_LIST[i][0], Config.VOTES_REWARDS_LIST[i][1], p, true);
								}
							}
							else
							{
								p.sendMessage("Already " + Config.VOTE_DUALBOXES_ALLOWED + " character(s) of your ip have been rewarded, so this character won't be rewarded.");
							}
						}
						playerIps.clear();
						setL2NetworkVoteCount(l2network_votes);
					}
					Broadcast.gameAnnounceToOnlinePlayers("L2Network: Next reward in " + minutes + " minutes at " + (getL2NetworkVoteCount() + Config.VOTES_FOR_REWARD) + " Votes!!");
					Broadcast.gameAnnounceToOnlinePlayers("Site Web: " + Config.SERVER_WEB_SITE);
				}
			}
		}
	}

	protected int getHopZoneVotes()
	{
		int votes = -1;
		URL url = null;
		URLConnection con = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		try
		{
			url = new URL(Config.VOTES_SITE_HOPZONE_URL);
			con = url.openConnection();
			con.addRequestProperty("User-L2Hopzone", "Mozilla/4.76");
			is = con.getInputStream();
			isr = new InputStreamReader(is);		    
			in = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				if (inputLine.contains("rank anonymous tooltip"))
				{
					votes = Integer.valueOf(inputLine.split(">")[2].replace("</span", ""));
					break;
				}
			}
		}
		catch (Exception e)
		{
			_log.info("Vote Reward: Server HopZone is offline or something is wrong in link");
			Broadcast.gameAnnounceToOnlinePlayers("HopZone is offline. We will check reward as it will be online again");
		}
		finally
		{
			if(in!=null)
			try
			{
					in.close();
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
			if(isr!=null)
			try
			{
					isr.close();
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
			if(is!=null)
			try
			{
					is.close();
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}

		}
		return votes;
	}

	protected int getTopZoneVotes()
	{
		int votes = -1;
		URL url = null;
		URLConnection con = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader in = null;

		try
		{
			url = new URL(Config.VOTES_SITE_TOPZONE_URL);
			con = url.openConnection();
			con.addRequestProperty("User-Agent", "L2TopZone");
			is = con.getInputStream();
			isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				votes = Integer.valueOf(inputLine);
				break;
			}
		}
		catch (final Exception e)
		{
			_log.info("Vote Reward: Server TopZone is offline or something is wrong in link");
			Broadcast.gameAnnounceToOnlinePlayers("TopZone is offline. We will check reward as it will be online again");
		}
		finally
		{
			if (in != null)
			try
			{
					in.close();
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
			if (isr != null)
			try
			{
					isr.close();
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
			if (is != null)
			try
			{
					is.close();
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
		}
		return votes;
	}

	protected int getL2NetworkVotes()
	{
		int votes = -1;
		URL url = null;
		URLConnection con = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader in = null;

		try
		{
			url = new URL(Config.VOTES_SITE_L2NETWORK_URL);
			con = url.openConnection();
			con.addRequestProperty("User-Agent", "L2Network");
			is = con.getInputStream();
			isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				if (inputLine.contains("color:#e7ebf2"))
				{
					votes = Integer.valueOf(inputLine.split(">")[2].replace("</b", ""));
					break;
				}
			}
		}
		catch (final Exception e)
		{
			_log.info("Vote Reward: Server L2Network is offline or something is wrong in link");
			Broadcast.gameAnnounceToOnlinePlayers("L2Network is offline. We will check reward as it will be online again");
		}
		finally
		{
			if (in != null)
			try
			{
					in.close();
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
			if (isr != null)
			try
			{
					isr.close();
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
			if (is != null)
			try
			{
					is.close();
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
		}
		return votes;
	}

	protected void setHopZoneVoteCount(final int voteCount)
	{
		_hopzoneVotesCount = voteCount;
	}

	protected int getHopZoneVoteCount()
	{
		return _hopzoneVotesCount;
	}

	protected void setTopZoneVoteCount(final int voteCount)
	{
		_topzoneVotesCount = voteCount;
	}

	protected int getTopZoneVoteCount()
	{
		return _topzoneVotesCount;
	}

	protected void setL2NetworkVoteCount(final int voteCount)
	{
		_l2networkVotesCount = voteCount;
	}

	protected int getL2NetworkVoteCount()
	{
		return _l2networkVotesCount;
	}

	public static AutoVoteReward getInstance()
	{

		if (Config.VOTES_SITE_HOPZONE_URL != null && !Config.VOTES_SITE_HOPZONE_URL.equals(""))
			_hopzone = true;

		if (Config.VOTES_SITE_TOPZONE_URL != null && !Config.VOTES_SITE_TOPZONE_URL.equals(""))
			_topzone = true;

		if (Config.VOTES_SITE_L2NETWORK_URL != null && !Config.VOTES_SITE_L2NETWORK_URL.equals(""))
			_l2network = true;

		if (_topzone || _hopzone || _l2network)
			return SingletonHolder._instance;

		return null;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AutoVoteReward _instance = new AutoVoteReward();
	}
}