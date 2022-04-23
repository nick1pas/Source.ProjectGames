package net.sf.l2j.gameserver.handler.voicedcommandhandlers.vote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public class RewardVote implements IVoicedCommandHandler
{
	private static enum ValueType
	{
		ACCOUNT_NAME,
		IP_ADRESS,
		HWID
	}

	private static final Logger _log = Logger.getLogger(RewardVote.class.getName());

	private static final String[] COMMANDS_LIST = new String[]
	{
		"reward"
	};

	private static int WAIT_TIME = 3;
	private static int MIN_LEVEL_TO_ALLOW_VOTE = 80;
	private static final long INTERVAL = WAIT_TIME * 60 * 1000; // in minutes.
	private static int REWARD_ID = 9517;
	private static int REWARD_AMOUNT = 1;

	public List<L2PcInstance> _safePeople = new ArrayList<>();

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (command.equalsIgnoreCase("reward"))
		{
			if (_safePeople.contains(activeChar))
			{
				activeChar.sendMessage("You can use this command only once at " + WAIT_TIME + " minutes.");
				return false;
			}

			_safePeople.add(activeChar);
			ThreadPoolManager.getInstance().scheduleGeneral(new PopSafePlayer(activeChar), INTERVAL);

			// getting IP of client, here we will have to check for HWID when we have LAMEGUARD
			String IPClient = activeChar.getClient().getConnection().getInetAddress().getHostAddress();
			// String IPIntern= activeChar.getClient().getConnectionAddress().get
			// sending IP to client for debug purpose
			// ??
			// Return 0 if he didnt voted. Date when he voted on website
			long dateHeVotedOnWebsite = VoteRead.checkVotedIP(IPClient);
			if (dateHeVotedOnWebsite > 0)
			{
				if (activeChar.getLevel() < MIN_LEVEL_TO_ALLOW_VOTE)
				{
					activeChar.sendMessage("You need to be at least " + MIN_LEVEL_TO_ALLOW_VOTE + " level in order to use this command.");
					return false;
				}

				//String uniqueID = activeChar.getNetConnection().getHWID();
				//if(uniqueID == null)
					//uniqueID = "";

				//Calculate if he can take reward
				if(canTakeReward(dateHeVotedOnWebsite, IPClient, activeChar))
				{
					//int[] reward = getReward();
					//if(reward.length < 2)
						//return false;

					activeChar.sendMessage("Successfully rewarded.");
   
					_log.info(activeChar.getName() + " got rewarded");
					insertInDataBase(dateHeVotedOnWebsite, IPClient, activeChar);
					activeChar.addItem("Vote Reward", REWARD_ID, REWARD_AMOUNT, activeChar, true);
					activeChar.sendMessage("Successfully rewarded.");
				}
			}
			else
			{
				activeChar.sendMessage("You didnt voted. Try again later!");
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * @param dateHeVoted
	 * @param iPClient 
	 * @param activeChar
	 **/
	private static void insertInDataBase(long dateHeVotedOnWebsite, String IPClient, L2PcInstance activeChar)
	{
		insertInDataBase(dateHeVotedOnWebsite, activeChar.getAccountName(), ValueType.ACCOUNT_NAME);
		insertInDataBase(dateHeVotedOnWebsite, IPClient, ValueType.IP_ADRESS);
		//insertInDataBase(dateHeVotedOnWebsite, HwID, ValueType.HWID);
	}
	
	private static void insertInDataBase(long dateHeVotedOnWebsite, String value, ValueType type)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM votes WHERE value=? AND value_type=?");
			statement.setString(1, value);
			statement.setInt(2, type.ordinal());
			rset = statement.executeQuery();

			if (rset.next()) // He already exit in database because he voted before.
			{
				int count = rset.getInt("vote_count");
				PreparedStatement statement2 = null;
				try
				{
					statement2 = con.prepareStatement("UPDATE votes SET date_voted_website=?, date_take_reward_in_game=?, vote_count=? WHERE value=? AND value_type=?");
					statement2.setLong(1, dateHeVotedOnWebsite);
					statement2.setLong(2, (System.currentTimeMillis() / 1000L));
					statement2.setInt(3, (count + 1));
					statement2.setString(4, value);
					statement2.setInt(5, type.ordinal());
					statement2.executeUpdate();
				}
				catch (SQLException e)
				{
					_log.warning("RewardVote:insertInDataBase(long,String,ValueType): " + e);
				}
				finally
				{
					DbUtils.closeQuietly(statement2);
				}
			}
			else
			{
				PreparedStatement statement2 = null;
				try
				{
					statement2 = con.prepareStatement("INSERT INTO votes(value, value_type, date_voted_website, date_take_reward_in_game, vote_count) VALUES (?, ?, ?, ?, ?)");
					statement2.setString(1, value);
					statement2.setInt(2, type.ordinal());
					statement2.setLong(3, dateHeVotedOnWebsite);
					statement2.setLong(4, (System.currentTimeMillis() / 1000L));
					statement2.setInt(5, 1);
					statement2.execute();
				}
				catch (SQLException e)
				{
					_log.warning("RewardVote:insertInDataBase(long,String,ValueType): " + e);
				}
				finally
				{
					DbUtils.closeQuietly(statement2);
				}
			}
		}
		catch (SQLException e)
		{
			_log.warning("RewardVote:insertInDataBase(long,String,ValueType): " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	private static boolean canTakeReward(long dateHeVotedOnWebsite, String IPClient, L2PcInstance activeChar)
	{
		int whenCanVote = canTakeReward(dateHeVotedOnWebsite, activeChar.getAccountName(), ValueType.ACCOUNT_NAME);
		int whenCanVoteIP = canTakeReward(dateHeVotedOnWebsite, IPClient, ValueType.IP_ADRESS);
		//int whenCanVoteHWID = canTakeReward(dateHeVotedOnWebsite, HwID, ValueType.HWID);

		whenCanVote = Math.max(whenCanVote, Math.max(whenCanVoteIP, 0));
		
		if(whenCanVote > 0)
		{
			if(whenCanVote > 60)
				activeChar.sendMessage("You can vote only once at 12 hours and 5 minutes. You still have to wait " + (int) (whenCanVote / 60) + " hours " + (whenCanVote % 60) + " minutes.");
			else
				activeChar.sendMessage("You can vote only once at 12 hours and 5 minutes. You still have to wait " + whenCanVote + " minutes.");
			return false;
		}
		return true;
	}

	private static int canTakeReward(long dateHeVotedOnWebsite, String value, ValueType type)
	{
		int dateLastVote = 0; // Date When he last voted on server
		int whenCanVote = 0; // The number of minutes when he can vote

		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT date_take_reward_in_game FROM votes WHERE value=? AND value_type=?");
			statement.setString(1, value);
			statement.setInt(2, type.ordinal());
			rset = statement.executeQuery();

			if (rset.next())
			{
				dateLastVote = rset.getInt("date_take_reward_in_game");
			}
		}
		catch (SQLException e)
		{
			_log.warning("RewardVote:canTakeReward(long,String,String): " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}

		// The number of minutes when he can vote
		if (dateLastVote == 0)
		{
			whenCanVote = (int) ((dateHeVotedOnWebsite - (System.currentTimeMillis() / 1000L)) / 60);
		}
		else
		{
			whenCanVote = (int) (((dateLastVote + (12 * 60 * 60) + 300) - (System.currentTimeMillis() / 1000L)) / 60);
		}

		return whenCanVote;
	}

	// this is the class to remove safe players to be reported again after the given time
	private class PopSafePlayer implements Runnable
	{
		private final L2PcInstance _safeplayer;

		public PopSafePlayer(L2PcInstance safeplayer)
		{
			_safeplayer = safeplayer;
		}

		@Override
		public void run()
		{
			if (_safePeople.contains(_safeplayer))
			{
				_safePeople.remove(_safeplayer);
			}
		}
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return COMMANDS_LIST;
	}
}