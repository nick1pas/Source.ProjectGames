/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.network.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;

import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.communitybbs.CommunityBoard;
import net.sf.l2j.gameserver.datatables.AdminCommandAccessRights;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.custom.AuctionTable;
import net.sf.l2j.gameserver.datatables.custom.IconTable;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.CustomBypassHandler;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.instancemanager.custom.AchievementsManager;
import net.sf.l2j.gameserver.instancemanager.custom.PvPZoneManager;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.L2Npc;
import net.sf.l2j.gameserver.model.actor.instance.L2OlympiadManagerInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2TeleporterInstance;
import net.sf.l2j.gameserver.model.entity.Hero;
import net.sf.l2j.gameserver.model.entity.events.achievments.base.Achievement;
import net.sf.l2j.gameserver.model.entity.events.achievments.base.Condition;
import net.sf.l2j.gameserver.model.entity.events.tournaments.properties.ArenaConfig;
import net.sf.l2j.gameserver.model.holder.AuctionHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.templates.StatsSet;
import net.sf.l2j.gameserver.util.GMAudit;
import net.sf.l2j.util.Rnd;

public final class RequestBypassToServer extends L2GameClientPacket
{
	private String _command;
	
	@Override
	protected void readImpl()
	{
		_command = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		
		if (!getClient().getFloodProtectors().getServerBypass().tryPerformAction(_command))
			return;
		
		if (_command.isEmpty())
		{
			_log.info(activeChar.getName() + " sent an empty requestBypass packet.");
			activeChar.logout();
			return;
		}
		
		try
		{
			if (_command.startsWith("admin_"))
			{
				String command = _command.split(" ")[0];
				
				IAdminCommandHandler ach = AdminCommandHandler.getInstance().getAdminCommandHandler(command);
				if (ach == null)
				{
					if (activeChar.isGM())
						activeChar.sendMessage("The command " + command.substring(6) + " doesn't exist.");
					
					_log.warning("No handler registered for admin command '" + command + "'");
					return;
				}
				
				if (!AdminCommandAccessRights.getInstance().hasAccess(command, activeChar.getAccessLevel()))
				{
					activeChar.sendMessage("You don't have the access rights to use this command.");
					_log.warning(activeChar.getName() + " tried to use admin command " + command + " without proper Access Level.");
					return;
				}
				
				if (Config.GMAUDIT)
					GMAudit.auditGMAction(activeChar.getName() + " [" + activeChar.getObjectId() + "]", _command, (activeChar.getTarget() != null ? activeChar.getTarget().getName() : "no-target"));
				
				ach.useAdminCommand(_command, activeChar);
			}
			else if (_command.startsWith("player_help "))
			{
				playerHelp(activeChar, _command.substring(12));
			}
			else if (_command.startsWith("voiced_"))
            {
                String command = _command.split(" ")[0];

                IVoicedCommandHandler ach = VoicedCommandHandler.getInstance().getHandler(_command.substring(7));

                if (ach == null)
                {
                    activeChar.sendMessage("The command " + command.substring(7) + " does not exist!");
                    _log.warning("No handler registered for command '" + _command + "'");
                    return;
                }

                ach.useVoicedCommand(_command.substring(7), activeChar, null);
            } 
			else if(_command.startsWith("custom_"))
			{
				L2PcInstance player = getClient().getActiveChar();
				CustomBypassHandler.getInstance().handleBypass(player, _command);
			}
			else if (_command.startsWith("npc_"))
			{
				if (!activeChar.validateBypass(_command))
					return;
				
				activeChar.setIsUsingCMultisell(false);
				
				int endOfId = _command.indexOf('_', 5);
				String id;
				if (endOfId > 0)
					id = _command.substring(4, endOfId);
				else
					id = _command.substring(4);
				
				try
				{
					final L2Object object = L2World.getInstance().findObject(Integer.parseInt(id));
					
					if (object != null && object instanceof L2Npc && endOfId > 0 && ((L2Npc) object).canInteract(activeChar))
						((L2Npc) object).onBypassFeedback(activeChar, _command.substring(endOfId + 1));
					
					activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				}
				catch (NumberFormatException nfe)
				{
				}
			}
			else if (_command.startsWith("classe_change"))
			{
				StringTokenizer st = new StringTokenizer(_command);
				st.nextToken();
				String type = null;
				type = st.nextToken();
				try
				{
					if (activeChar.getBaseClass() != activeChar.getClassId().getId())
					{
						activeChar.sendMessage("You need to be in your base class to be able to use this item.");
						return ;
					}
					
					/*
					if (activeChar.getLevel() < 79)
					{
						activeChar.sendMessage("You need to be at least 80 level in order to use class card.");
						return;			
					}
					
					if (activeChar.getClassId() == ClassId.duelist || activeChar.getClassId() == ClassId.dreadnought || activeChar.getClassId() == ClassId.phoenixKnight || activeChar.getClassId() == ClassId.hellKnight || activeChar.getClassId() == ClassId.sagittarius || activeChar.getClassId() == ClassId.adventurer || activeChar.getClassId() == ClassId.archmage || activeChar.getClassId() == ClassId.soultaker || activeChar.getClassId() == ClassId.arcanaLord || activeChar.getClassId() == ClassId.cardinal || activeChar.getClassId() == ClassId.hierophant || activeChar.getClassId() == ClassId.evaTemplar || activeChar.getClassId() == ClassId.swordMuse || activeChar.getClassId() == ClassId.windRider || activeChar.getClassId() == ClassId.moonlightSentinel || activeChar.getClassId() == ClassId.mysticMuse || activeChar.getClassId() == ClassId.elementalMaster || activeChar.getClassId() == ClassId.evaSaint || activeChar.getClassId() == ClassId.shillienTemplar || activeChar.getClassId() == ClassId.spectralDancer || activeChar.getClassId() == ClassId.ghostHunter || activeChar.getClassId() == ClassId.ghostSentinel || activeChar.getClassId() == ClassId.stormScreamer || activeChar.getClassId() == ClassId.spectralMaster || activeChar.getClassId() == ClassId.shillienSaint || activeChar.getClassId() == ClassId.titan || activeChar.getClassId() == ClassId.grandKhauatari || activeChar.getClassId() == ClassId.dominator || activeChar.getClassId() == ClassId.doomcryer || activeChar.getClassId() == ClassId.fortuneSeeker || activeChar.getClassId() == ClassId.maestro)
					{
						activeChar.sendMessage("You need to be at least third class in order to use class card.");
						return;			
					}
					*/

					if (activeChar.isInOlympiadMode())
					{
						activeChar.sendMessage("This item cannot be used on olympiad games.");
						return;			
					}
					ClassChangeCoin(activeChar, type); 
				}
				catch (StringIndexOutOfBoundsException e)
				{
				}
			}	
			else if (_command.startsWith("class_index"))
			{
				NpcHtmlMessage html = new NpcHtmlMessage(0);
				html.setFile("data/html/mods/class_changer/Class.htm");
				activeChar.sendPacket(html);
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			}
			//Achievements start
			else if (_command.startsWith("showMyAchievements"))
			{                      
				activeChar.getAchievemntData();
				showMyAchievements(activeChar);
			}
			else if (_command.startsWith("achievementInfo"))
			{                      
				StringTokenizer st = new StringTokenizer(_command, " ");
				st.nextToken();
				int id = Integer.parseInt(st.nextToken());

				showAchievementInfo(id, activeChar);
			}
			else if (_command.startsWith("getReward"))
			{
				StringTokenizer st = new StringTokenizer(_command, " ");
				st.nextToken();
				int id = Integer.parseInt(st.nextToken());

				if (id == 6)
				{
					activeChar.getInventory().destroyItemByItemId("", 3470, 100, activeChar, null);
					activeChar.saveAchievementData(id,0);
					AchievementsManager.getInstance().rewardForAchievement(id, activeChar);
				}
				else if (id == 3)
				{
					ItemInstance weapon = activeChar.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);

					if (weapon!= null)
					{
						int objid = weapon.getObjectId();
						if(AchievementsManager.getInstance().getAchievementList().get(id).meetAchievementRequirements(activeChar))
						{
							if(!AchievementsManager.getInstance().isBinded(objid,id))
							{
								AchievementsManager.getInstance().getBinded().add(objid+"@"+id);
								activeChar.saveAchievementData(id,objid);
								AchievementsManager.getInstance().rewardForAchievement(id, activeChar);
							}
							else
								activeChar.sendMessage("This item was already used to earn this achievement");
						}
						else
						{
							activeChar.sendMessage("Seems you don't meet the achievements requirements now.");
						}
					}
					else
						activeChar.sendMessage("You must equip your weapon in order to get rewarded.");
				}
				else
				{
					activeChar.saveAchievementData(id,0);
					AchievementsManager.getInstance().rewardForAchievement(id, activeChar);
				}
				showMyAchievements(activeChar);
			}
			// Navigate throught Manor windows
			else if (_command.startsWith("manor_menu_select?"))
			{
				L2Object object = activeChar.getTarget();
				if (object instanceof L2Npc)
					((L2Npc) object).onBypassFeedback(activeChar, _command);
			}
			else if (_command.startsWith("bbs_") || _command.startsWith("_bbs") || _command.startsWith("_friend") || _command.startsWith("_mail") || _command.startsWith("_block"))
			{
				CommunityBoard.getInstance().handleCommands(getClient(), _command);
			}
			else if (_command.startsWith("Quest "))
			{
				if (!activeChar.validateBypass(_command))
					return;
				
				String[] str = _command.substring(6).trim().split(" ", 2);
				if (str.length == 1)
					activeChar.processQuestEvent(str[0], "");
				else
					activeChar.processQuestEvent(str[0], str[1]);
			}
			else if (_command.startsWith("_match"))
			{
				String params = _command.substring(_command.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if (heroid > 0)
					Hero.getInstance().showHeroFights(activeChar, heroclass, heroid, heropage);
			}
			else if (_command.startsWith("_diary"))
			{
				String params = _command.substring(_command.indexOf("?") + 1);
				StringTokenizer st = new StringTokenizer(params, "&");
				int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
				int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
				int heroid = Hero.getInstance().getHeroByClass(heroclass);
				if (heroid > 0)
					Hero.getInstance().showHeroDiary(activeChar, heroclass, heroid, heropage);
			}
			else if (_command.startsWith("arenachange")) // change
			{
				final boolean isManager = activeChar.getCurrentFolkNPC() instanceof L2OlympiadManagerInstance;
				if (!isManager)
				{
					// Without npc, command can be used only in observer mode on arena
					if (!activeChar.inObserverMode() || activeChar.isInOlympiadMode() || activeChar.getOlympiadGameId() < 0)
						return;
				}
				
				if (OlympiadManager.getInstance().isRegisteredInComp(activeChar))
				{
					activeChar.sendPacket(SystemMessageId.WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME);
					return;
				}
				
				final int arenaId = Integer.parseInt(_command.substring(12).trim());
				activeChar.enterOlympiadObserverMode(arenaId);
			}
			else if (_command.startsWith("antibot"))
			{
				StringTokenizer st = new StringTokenizer(_command);
				st.nextToken();

				if (st.hasMoreTokens())
				{
					activeChar.checkCode(st.nextToken());
					return;
				}
				activeChar.checkCode("Fail");
			}
			else if (_command.startsWith("name_change"))
			{
				try
				{
					String name = _command.substring(12);
					
					if (name.length() < 3)
					{
						activeChar.sendMessage("Your name needs a minimum of 3 letters. Please try again.");
						return;
					}
					
					if (name.length() > 16)
					{
						activeChar.sendMessage("Your name cannot exceed 16 characters in length. Please try again.");
						return;
					}

					if (name.equals(activeChar.getName()))
					{
						activeChar.sendMessage("Please, choose a different name.");
						return;
					}
					
					if (activeChar.isClanLeader())
					{
						activeChar.sendMessage("Clan leaders can't change name!");
						return;
					}
					
					if (activeChar.getClan() != null)
					{
						activeChar.sendMessage("Clan members can't change name!");
						return;
					}
					
					if (!name.matches("^[a-zA-Z0-9]+$"))
					{
						activeChar.sendMessage("Incorrect name. Please try again.");
						return;
					}

					synchronized (CharNameTable.getInstance())
					{
						if (CharNameTable.doesCharNameExist(name))
						{
							activeChar.sendMessage("The chosen name already exists.");
							return;
						}
					}

					if (activeChar.destroyItemByItemId("Name Change", activeChar.getNameChangeItemId(), 1, null, true))
					{
						activeChar.setName(name);
						activeChar.sendMessage("Your new character name is " + name);
						activeChar.broadcastUserInfo();
						activeChar.store();
					}
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Fill out the field correctly.");
				}
			}
			else if (_command.startsWith("submitpin"))
			{
				try
				{
					String value = _command.substring(9);
					StringTokenizer s = new StringTokenizer(value," ");
					int _pin = activeChar.getPin();

					try
					{
						if (activeChar.getPincheck())
						{
							_pin = Integer.parseInt(s.nextToken());
							if (Integer.toString(_pin).length() < 4)
							{
								activeChar.sendMessage("You have to fill the pin box with 4 - 9 numbers. Not more, not less.");
								return;
							}
							
							if (Integer.toString(_pin).length() > 9)
							{
								activeChar.sendMessage("You have to fill the pin box with 4 - 9 numbers. Not more, not less.");
								return;
							}

							try (Connection con = L2DatabaseFactory.getInstance().getConnection())
							{
								PreparedStatement statement = con.prepareStatement("UPDATE characters SET pin=? WHERE obj_id=?");

								statement.setInt(1, _pin);
								statement.setInt(2, activeChar.getObjectId());
								statement.execute();
								statement.close();
								activeChar.setPincheck(false);
								activeChar.updatePincheck();
								activeChar.sendMessage("You successfully submitted your pin code.");
								activeChar.sendMessage("Your Pin Code is: " + _pin );
								UpdateLastIP(activeChar, activeChar.getAccountName());
								activeChar.broadcastUserInfo();
								activeChar.store();
								con.close();
							}
							catch(Exception e)
							{
								e.printStackTrace();
								_log.warning("could not set char first login:" + e);
							}      
						}
					}
					catch(Exception e)
					{
						activeChar.sendMessage("The Pin Code must be 4 - 9 numbers.");
					}
				}
				catch(Exception e)
				{
					activeChar.sendMessage("The Pin Code must be 4 - 9 numbers.");
				}

			}
			else if(_command.startsWith("enterpin"))
			{
				try
				{
					String value = _command.substring(8);
					StringTokenizer s = new StringTokenizer(value," ");
					int dapin = 0;
					int pin = 0;
					dapin = Integer.parseInt(s.nextToken());                               

					PreparedStatement statement = null;

					try (Connection con = L2DatabaseFactory.getInstance().getConnection())
					{
						statement = con.prepareStatement("SELECT pin FROM characters WHERE obj_Id=?");
						statement.setInt(1, activeChar.getObjectId());
						ResultSet rset = statement.executeQuery();

						while (rset.next())
						{
							pin = rset.getInt("pin");
						}

						if (pin == dapin)
						{
							activeChar.sendMessage("Pin Code Authenticated Successfully. You are now free to move.");
							activeChar.setIsImmobilized(false);
							activeChar.setIsSubmitingPin(false);
							activeChar.stopAbnormalEffect(0x0800);
							UpdateLastIP(activeChar, activeChar.getAccountName());
							activeChar.broadcastUserInfo();
							activeChar.store();
							con.close();
						}
						else
						{
							activeChar.sendMessage("Pin Code does not match with the submitted one. You will now get disconnected!");
							try 
							{
								Thread.sleep(5000);
							} 
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							activeChar.logout();
						}
					}
					catch (Exception e)
					{
						activeChar.sendMessage("The Pin Code must be 4 - 9 numbers.");
					}
				}
				catch (Exception e)
				{
					//e.printStackTrace();
					activeChar.sendMessage("The Pin Code MUST be 4 - 9 numbers.");
				}
			}
	        if (_command.startsWith("auction"))
	        {
	            try
	            {
	                String[] data = _command.substring(8).split(" - ");
	                int page = Integer.parseInt(data[0]);
	                String search = data[1];
	                showAuction(activeChar, page, search);
	            }
	            catch (Exception e)
	            {
	                showAuctionHtml(activeChar);
	                activeChar.sendMessage("Invalid input. Please try again.");
	                return;
	            }
	        }
	        else if (_command.startsWith("buy"))
	        {
	            int auctionId = Integer.parseInt(_command.substring(4));
	            AuctionHolder item = AuctionTable.getInstance().getItem(auctionId);
	           
	            if (item == null)
	            {
	            	showAuctionHtml(activeChar);
	                activeChar.sendMessage("Invalid choice. Please try again.");
	                return;
	            }
	           
	            if (activeChar.getInventory().getItemByItemId(item.getCostId()) == null || activeChar.getInventory().getItemByItemId(item.getCostId()).getCount() < item.getCostCount())
	            {
	            	showAuctionHtml(activeChar);
	                activeChar.sendMessage("Incorrect item count.");
	                return;
	            }
	           
	            activeChar.getInventory().destroyItemByItemId("Auction", item.getCostId(), item.getCostCount(), activeChar, null);
	           
	            L2PcInstance owner = L2World.getInstance().getPlayer(item.getOwnerId());
	            if (owner != null && owner.isOnline())
	            {
	                owner.addItem("Auction", item.getCostId(), item.getCostCount(), null, true);
	                owner.sendMessage("You have sold an item in the Auction Shop.");
	            }
	            else
	            {
	                addItemToOffline(item.getOwnerId(), item.getCostId(), item.getCostCount());
	            }
	           
	            ItemInstance i = activeChar.addItem("auction", item.getItemId(), item.getCount(), activeChar, true);
	            i.setEnchantLevel(item.getEnchant());
	            activeChar.sendPacket(new InventoryUpdate());
	            activeChar.sendMessage("You have purchased an item from the Auction Shop.");
	           
	            AuctionTable.getInstance().deleteItem(item);
	           
	            showAuctionHtml(activeChar);
	        }
	        else if (_command.startsWith("addpanel"))
	        {
	            int page = Integer.parseInt(_command.substring(9));
	           
	            showAddPanel(activeChar, page);
	        }
	        else if (_command.startsWith("additem"))
	        {
	            int itemId = Integer.parseInt(_command.substring(8));
	           
	            if (activeChar.getInventory().getItemByObjectId(itemId) == null)
	            {
	            	showAuctionHtml(activeChar);
	                activeChar.sendMessage("Invalid item. Please try again.");
	                return;
	            }
	           
	            showAddPanel2(activeChar, itemId);
	        }
	        else if (_command.startsWith("addit2"))
	        {
	            try
	            {
	                String[] data = _command.substring(7).split(" ");
	                int itemId = Integer.parseInt(data[0]);
	                String costitemtype = data[1];
	                int costCount = Integer.parseInt(data[2]);
	                int itemAmount = Integer.parseInt(data[3]);
	               
	                if (activeChar.getInventory().getItemByObjectId(itemId) == null)
	                {
	                	showAuctionHtml(activeChar);
	                    activeChar.sendMessage("Invalid item. Please try again.");
	                    return;
	                }
	                if (activeChar.getInventory().getItemByObjectId(itemId).getCount() < itemAmount)
	                {
	                	showAuctionHtml(activeChar);
	                    activeChar.sendMessage("Invalid item. Please try again.");
	                    return;
	                }
	                if (!activeChar.getInventory().getItemByObjectId(itemId).isTradable())
	                {
	                	showAuctionHtml(activeChar);
	                    activeChar.sendMessage("Invalid item. Please try again.");
	                    return;
	                }
	               
	                int costId = 0;
	                if (costitemtype.equals("Gold_Token"))
	                {
	                    costId = 9500;
	                }
	                else if (costitemtype.equals("Event_Token"))
	                {
	                    costId = 9501;
	                }
	                else if (costitemtype.equals("Ticket_Donate"))
	                {
	                    costId = 9511;
	                }
	                else if (costitemtype.equals("Appetizing_Apple"))
	                {
	                    costId = 9515;
	                }
	                
	                AuctionTable.getInstance().addItem(new AuctionHolder(AuctionTable.getInstance().getNextAuctionId(), activeChar.getObjectId(), activeChar.getInventory().getItemByObjectId(itemId).getItemId(), itemAmount, activeChar.getInventory().getItemByObjectId(itemId).getEnchantLevel(), costId, costCount));
	               
	                activeChar.destroyItem("auction", itemId, itemAmount, activeChar, true);
	                activeChar.sendPacket(new InventoryUpdate());
	                activeChar.sendMessage("You have added an item for sale in the Auction Shop.");
	                showAuctionHtml(activeChar);
	            }
	            catch (Exception e)
	            {
	            	showAuctionHtml(activeChar);
	                activeChar.sendMessage("Invalid input. Please try again.");
	                return;
	            }
	        }
	        else if (_command.startsWith("myitems"))
	        {
	            int page = Integer.parseInt(_command.substring(8));
	            showMyItems(activeChar, page);
	        }
	        else if (_command.startsWith("remove"))
	        {
	            int auctionId = Integer.parseInt(_command.substring(7));
	            AuctionHolder item = AuctionTable.getInstance().getItem(auctionId);
	           
	            if (item == null)
	            {
	            	showAuctionHtml(activeChar);
	                activeChar.sendMessage("Invalid choice. Please try again.");
	                return;
	            }
	           
	            AuctionTable.getInstance().deleteItem(item);
	           
	            ItemInstance i = activeChar.addItem("auction", item.getItemId(), item.getCount(), activeChar, true);
	            i.setEnchantLevel(item.getEnchant());
	            activeChar.sendPacket(new InventoryUpdate());
	            activeChar.sendMessage("You have removed an item from the Auction Shop.");
	            showAuctionHtml(activeChar);
	        }
			else if (_command.startsWith("tele_tournament"))
			{
				if (activeChar.isOlympiadProtection())
				{
					activeChar.sendMessage("Are you participating in the Olympiad.");
					return;
				}
				
				for (L2TeleporterInstance knownChar : activeChar.getKnownList().getKnownTypeInRadius(L2TeleporterInstance.class, 300))
				{
					if (knownChar != null)
					{
						activeChar.teleToLocation(ArenaConfig.Tournament_locx + Rnd.get(-100, 100), ArenaConfig.Tournament_locy + Rnd.get(-100, 100), ArenaConfig.Tournament_locz, 0);
						activeChar.setTournamentTeleport(true);
					}
				}
			}
	        
			StringTokenizer st = new StringTokenizer(_command, " ");
			String actualCommand = st.nextToken(); // Get actual command
			
			if (actualCommand.startsWith("voteZone"))
			{
				int playerId = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				
				PvPZoneManager.getInstance().setVoteZone(playerId, name);
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Bad RequestBypassToServer: ", e);
		}
	}

	public void showChatWindow(L2PcInstance player, String filename)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(filename);
		player.sendPacket(html);
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	public static void UpdateLastIP(L2PcInstance player ,String user)
	{
		String address = player.getClient().getConnection().getInetAddress().getHostAddress();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE accounts SET lastIP=? WHERE login=?");
			statement.setString(1, address);
			statement.setString(2, user);
			statement.execute();
			statement.close();
			con.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "UpdateMyIP: Error while saving new IP: ", e);
		}
	}
	
	private static void playerHelp(L2PcInstance activeChar, String path)
	{
		if (path.indexOf("..") != -1)
			return;
		
		final StringTokenizer st = new StringTokenizer(path);
		final String[] cmd = st.nextToken().split("#");
		
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/help/" + cmd[0]);
		if (cmd.length > 1)
			html.setItemId(Integer.parseInt(cmd[1]));
		html.disableValidation();
		activeChar.sendPacket(html);
	}
	
	private static void ClassChangeCoin(L2PcInstance player, String command)
	{
		String nameclasse = player.getTemplate().getClassName();

		String type = command;
		if (type.equals("---SELECT---"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile("data/html/mods/class_changer/Class.htm");
			player.sendPacket(html);
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		if (type.equals("Duelist"))
		{		
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 88)
				{
					player.sendMessage("Your class is already " + nameclasse + ".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(88);
				if (!player.isSubClassActive())
					player.setBaseClass(88);

				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("DreadNought"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 89)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(89);
				if (!player.isSubClassActive())
					player.setBaseClass(89);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Phoenix_Knight"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 90)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(90);
				if (!player.isSubClassActive())
					player.setBaseClass(90);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Hell_Knight"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 91)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(91);
				if (!player.isSubClassActive())
					player.setBaseClass(91);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Sagittarius"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 92)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(92);
				if (!player.isSubClassActive())
					player.setBaseClass(92);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Adventurer"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 93)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(93);
				if (!player.isSubClassActive())
					player.setBaseClass(93);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Archmage"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 94)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(94);
				if (!player.isSubClassActive())
					player.setBaseClass(94);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Soultaker"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 95)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(95);
				if (!player.isSubClassActive())
					player.setBaseClass(95);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Arcana_Lord"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 96)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(96);
				if (!player.isSubClassActive())
					player.setBaseClass(96);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Cardinal"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 97)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(97);
				if (!player.isSubClassActive())
					player.setBaseClass(97);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Hierophant"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 98)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(98);
				if (!player.isSubClassActive())
					player.setBaseClass(98);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Eva_Templar"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 99)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(99);
				if (!player.isSubClassActive())
					player.setBaseClass(99);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Sword_Muse"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 100)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(100);
				if (!player.isSubClassActive())
					player.setBaseClass(100);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Wind_Rider"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 101)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(101);
				if (!player.isSubClassActive())
					player.setBaseClass(101);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Moonli_Sentinel"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 102)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(102);
				if (!player.isSubClassActive())
					player.setBaseClass(102);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Mystic_Muse"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 103)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(103);
				if (!player.isSubClassActive())
					player.setBaseClass(103);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Elemental_Master"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 104)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(104);
				if (!player.isSubClassActive())
					player.setBaseClass(104);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Eva_Saint"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 105)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(105);
				if (!player.isSubClassActive())
					player.setBaseClass(105);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Shillien_Templar"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 106)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(106);
				if (!player.isSubClassActive())
					player.setBaseClass(106);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Spectral_Dancer"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 107)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(107);
				if (!player.isSubClassActive())
					player.setBaseClass(107);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Ghost_Hunter"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 108)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(108);
				if (!player.isSubClassActive())
					player.setBaseClass(108);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Ghost_Sentinel"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 109)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(109);
				if (!player.isSubClassActive())
					player.setBaseClass(109);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Storm_Screamer"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 110)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(110);
				if (!player.isSubClassActive())
					player.setBaseClass(110);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Spectral_Master"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 111)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(111);
				if (!player.isSubClassActive())
					player.setBaseClass(111);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Shillen_Saint"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 112)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(112);
				if (!player.isSubClassActive())
					player.setBaseClass(112);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Titan"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 113)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(113);
				if (!player.isSubClassActive())
					player.setBaseClass(113);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Grand_Khauatari"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 114)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(114);
				if (!player.isSubClassActive())
					player.setBaseClass(114);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Dominator"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 115)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(115);
				if (!player.isSubClassActive())
					player.setBaseClass(115);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Doomcryer"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 116)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(116);
				if (!player.isSubClassActive())
					player.setBaseClass(116);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Fortune_Seeker"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 117)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(117);
				if (!player.isSubClassActive())
					player.setBaseClass(117);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
		if (type.equals("Maestro"))
		{
			if (player.getInventory().getInventoryItemCount(9590, 0) >= 1)
			{
				if (player.getClassId().getId() == 118)
				{
					player.sendMessage("Your class is already "+nameclasse+".");				
					return;
				}

				RemoverSkills(player);

				player.setClassId(118);
				if (!player.isSubClassActive())
					player.setBaseClass(118);
				Finish(player);
			}
			else
			{				
				player.sendMessage("You dont have class card item!");
				return;
			}
		}
	}
	
	private static void RemoverSkills(L2PcInstance activeChar)
	{
		L2Skill[] skills = activeChar.getAllSkills();

		for (L2Skill skill : skills)
			activeChar.removeSkill(skill);
		
		activeChar.destroyItemByItemId("Classe Change", activeChar.getClassChangeItemId(), 1, null, true);
	}

	private static void Finish(L2PcInstance activeChar)
	{
		String newclass = activeChar.getTemplate().getClassName();

		DeleteHenna(activeChar, 0);
		
		activeChar.sendMessage(activeChar.getName() + " is now a " + newclass + ".");
		activeChar.refreshOverloaded();
		activeChar.store();
		activeChar.broadcastUserInfo();
		activeChar.sendSkillList();
		activeChar.sendPacket(new PlaySound("ItemSound.quest_finish"));

		if (activeChar.isNoble())
		{
			StatsSet playerStat = Olympiad.getNobleStats(activeChar.getObjectId());
			if (!(playerStat == null))
			{
				updateClasse(activeChar);
				DeleteHero(activeChar);
				activeChar.sendMessage("You now has " + Olympiad.getInstance().getNoblePoints(activeChar.getObjectId()) + " Olympiad points.");
			}
		}
		activeChar.sendMessage("You will be disconnected for security reasons.");
		waitSecs(5);
		
		activeChar.getClient().closeNow();
	}
	
	public static void updateClasse(L2PcInstance player)
	{
		if (player == null)
			return;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stmt = con.prepareStatement(INSERT_DATA);

			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, player.getClassId().getId());
			stmt.setInt(3, 18);
			stmt.execute();
			stmt.close();
			con.close();
		}
		catch (Exception e)
		{
			_log.warning("Class Card: Could not clear char Olympiad Points: " + e);
		}
	}
	

	public static void DeleteHero(L2PcInstance player)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement(DELETE_CHAR_HERO);
			statement.setInt(1, player.getObjectId());
			statement.execute();
			statement.close();
			con.close();
		}
		catch (Exception e)
		{
			_log.warning("could not clear char Hero: " + e);
		}
	}
	
	public static void DeleteHenna(L2PcInstance player, int classIndex)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			// Remove all henna info stored for this sub-class.
			PreparedStatement statement = con.prepareStatement(DELETE_CHAR_HENNAS);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, classIndex);
			statement.execute();
			statement.close();
			con.close();
		}
		catch (Exception e)
		{
			_log.warning("could not clear char Hero: " + e);
		}
	}

	// Updates That Will be Executed by MySQL
	// ----------------------------------------
	static String INSERT_DATA = "REPLACE INTO olympiad_nobles (char_id, class_id, olympiad_points) VALUES (?,?,?)";
	static String OLYMPIAD_UPDATE = "UPDATE olympiad_nobles SET class_id=?, olympiad_points=?, competitions_done=?, competitions_won=?, competitions_lost=?, competitions_drawn=? WHERE char_Id=?";
	static String DELETE_CHAR_HERO = "DELETE FROM heroes WHERE char_id=?";	
	static String DELETE_CHAR_HENNAS = "DELETE FROM character_hennas WHERE char_obj_id=? AND class_index=?";
	
	public static void waitSecs(int i)
	{
		try
		{
			Thread.sleep(i * 1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void showMyAchievements(L2PcInstance player)
	{
		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		StringBuilder tb = new StringBuilder();
		
		tb.append("<html><title>Achievements Book</title><body><br><center>");
		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br1>");
		
		tb.append("<table width=\"265\" cellpadding=\"5\" bgcolor=\"000000\"><tr>");
		tb.append("<td width=\"45\" valign=\"top\" align=\"center\"><img src=\"L2PvpZone.achivement_book\" width=\"32\" height=\"32\"></td>");
		tb.append("<td valign=\"top\"><font color=\"FF6600\">Character Achievements</font><br1>Beat your goals and collect trophies.</td></tr></table><br1>");

		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br>");
		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br1>");
		
		if (AchievementsManager.getInstance().getAchievementList().isEmpty())
		{
			tb.append("There are no Achievements created yet!");
		}
		else
		{
			int i = 0;

			for (Achievement a: AchievementsManager.getInstance().getAchievementList().values())
			{
				tb.append(getTableColor(i));
				tb.append("<tr><td width=270 align=\"left\">" + a.getName() + "</td><td width=50 align=\"right\"><a action=\"bypass -h achievementInfo "
						+ a.getID() + "\">info</a></td><td width=200 align=\"center\">" + getStatusString(a.getID(), player) + "</td></tr></table>");
				i++;
			}

			tb.append("<br><img src=\"l2ui.squaregray\" width=\"230\" height=\"1\">");
		}
		
		tb.append("</body></html>");

		msg.setHtml(tb.toString());
		player.sendPacket(msg);
	}
	
	private void showAchievementInfo(int achievementID, L2PcInstance player)
	{
		Achievement a = AchievementsManager.getInstance().getAchievementList().get(achievementID);

		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		StringBuilder tb = new StringBuilder();
		
		tb.append("<html><title>Achievements Book</title><body><br><center>");
		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br1>");
		
		tb.append("<table width=\"265\" cellpadding=\"5\" bgcolor=\"000000\"><tr>");
		tb.append("<td width=\"45\" valign=\"top\" align=\"center\"><img src=\"L2PvpZone.achivement_book\" width=\"32\" height=\"32\"></td>");
		tb.append("<td valign=\"top\"><font color=\"FF6600\">Character Achievements</font><br1>Beat your goals and collect trophies.</td></tr></table><br1>");

		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br>");
		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br1>");
		
		tb.append("<center><table width=270 border=0 bgcolor=\"000000\">");
		tb.append("<tr><td width=270 align=\"center\">" + a.getName() + "</td></tr></table><br>");
		tb.append("<center>Status: " + getStatusString(achievementID, player));

		if (a.meetAchievementRequirements(player) && !player.getCompletedAchievements().contains(achievementID))
		{
			tb.append("<button value=\"My Reward\" action=\"bypass -h getReward " + a.getID() + "\" back=\"buttons_bs.bs_64x15_1\" fore=\"buttons_bs.bs_64x15_2\" width=90 height=15>");
		}

		tb.append("<br><img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\"><br>");

		tb.append("<table width=270 border=0 bgcolor=\"000000\">");
		tb.append("<tr><td width=270 align=\"center\">Description</td></tr></table><br>");
		tb.append(a.getDescription());
		tb.append("<br><img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\"><br>");

		tb.append("<table width=270 border=0 bgcolor=\"000000\">");
		tb.append("<tr><td width=270 align=\"left\">Condition:</td><td width=100 align=\"left\">Value:</td><td width=200 align=\"center\">Status:</td></tr></table>");
		tb.append(getConditionsStatus(achievementID, player));
		tb.append("<br><img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\"><br>");
		tb.append("<center><button value=\"Back\" action=\"bypass -h showMyAchievements\" back=\"buttons_bs.bs_64x15_1\" fore=\"buttons_bs.bs_64x15_2\" width=90 height=15></center>");

		tb.append("</body></html>");

		msg.setHtml(tb.toString());
		player.sendPacket(msg);
	}
	
	private static String getStatusString(int achievementID, L2PcInstance player)
	{
		if (player.getCompletedAchievements().contains(achievementID))
		{
			return "<font color=\"5EA82E\">Completed</font>";
		}

		if (AchievementsManager.getInstance().getAchievementList().get(achievementID).meetAchievementRequirements(player))
		{
			return "<font color=\"LEVEL\">Get Reward</font>";
		}

		return "<font color=\"FF0000\">Not Completed</font>";
	}

	private static String getTableColor(int i)
	{
		if (i % 2 == 0)
			return "<center><table width=270 border=0 bgcolor=\"000000\">";

		return "<center><table width=270 border=0>";
	}

	private static String getConditionsStatus(int achievementID, L2PcInstance player)
	{
		int i = 0;
		String s = "</center>";
		Achievement a = AchievementsManager.getInstance().getAchievementList().get(achievementID);
		String completed = "<font color=\"5EA82E\">Completed</font></td></tr></table>";
		String notcompleted = "<font color=\"FF0000\">Not Completed</font></td></tr></table>";

		for (Condition c: a.getConditions())
		{
			s+= getTableColor(i);
			s+= "<tr><td width=270 align=\"left\">" + c.getName() + "</td><td width=100 align=\"left\">" + c.getValue() + "</td><td width=200 align=\"center\">";
			i++;

			if (c.meetConditionRequirements(player))
				s+= completed;
			else
				s+= notcompleted;
		}
		return s;
	}
	
	private void showMyItems(L2PcInstance player, int page)
	{
		HashMap<Integer, ArrayList<AuctionHolder>> items = new HashMap<>();
		int curr = 1;
		int counter = 0;

		ArrayList<AuctionHolder> temp = new ArrayList<>();
		for (Map.Entry<Integer, AuctionHolder> entry : AuctionTable.getInstance().getItems().entrySet())
		{
			if (entry.getValue().getOwnerId() == player.getObjectId())
			{
				temp.add(entry.getValue());

				counter++;

				if (counter == 10)
				{
					items.put(curr, temp);
					temp = new ArrayList<>();
					curr++;
					counter = 0;
				}
			}
		}
		items.put(curr, temp);

		if (!items.containsKey(page))
		{
			showAuctionHtml(player);
			player.sendMessage("Invalid page. Please try again.");
			return;
		}

		String html = "";
		html += "<html><title>Auction Manager</title><body><center><br1>";
		html += "<table width=100% bgcolor=000000 border=1>";
		html += "<tr><td>Item</td><td>Cost</td><td></td></tr>";
		for (AuctionHolder item : items.get(page))
		{
			html += "<tr>";
			html += "<td><img src=\""+IconTable.getIcon(item.getItemId())+"\" width=32 height=32 align=center></td>";
			html += "<td>Item: "+(item.getEnchant() > 0 ? "+"+item.getEnchant()+" "+ItemTable.getInstance().getTemplate(item.getItemId()).getName()+" - "+item.getCount() : ItemTable.getInstance().getTemplate(item.getItemId()).getName()+" - "+item.getCount());
			html += "<br1>Cost: "+item.getCostCount()+" "+ItemTable.getInstance().getTemplate(item.getCostId()).getName();
			html += "</td>";
			html += "<td fixwidth=71><button value=\"Remove\" action=\"bypass -h remove "+item.getAuctionId()+"\" width=70 height=21 back=\"buttons_bs.bs_64x27_1\" fore=\"buttons_bs.bs_64x27_2\">";
			html += "</td></tr>";
		}
		html += "</table><br><br>";

		html += "Page: "+page;
		html += "<br1>";

		if (items.keySet().size() > 1)
		{
			if (page > 1)
				html += "<a action=\"bypass -h myitems "+(page-1)+"\"><- Prev</a>";

			if (items.keySet().size() > page)
				html += "<a action=\"bypass -h myitems "+(page+1)+"\">Next -></a>";
		}

		html += "</center></body></html>";

		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setHtml(html);
		player.sendPacket(htm);
	}
   
    private void showAddPanel2(L2PcInstance player, int itemId)
    {
        ItemInstance item = player.getInventory().getItemByObjectId(itemId);
       
        String html = "";
        html += "<html><title>Auction Manager</title><body><center><br1>";
        html += "<img src=\"" + IconTable.getIcon(item.getItemId()) + "\" width=32 height=32 align=center>";
        html += "Item: " + (item.getEnchantLevel() > 0 ? " + " + item.getEnchantLevel() + " " + item.getName() : item.getName());
       
        if (item.isStackable())
        {
            html += "<br>Set amount of items to sell:";
            html += "<edit var=amm type=number width=120 height=17>";
        }
       
        html += "<br>Select price:";
        html += "<br><combobox width=120 height=17 var=ebox list=Gold_Token;Event_Token;Ticket_Donate;Appetizing_Apple;>";
        html += "<br><edit var=count type=number width=120 height=17>";
        html += "<br><button value=\"Add item\" action=\"bypass -h addit2 " + itemId + " $ebox $count " + (item.isStackable() ? "$amm" : "1") + "\" width=70 height=21 back=\"buttons_bs.bs_64x27_1\" fore=\"buttons_bs.bs_64x27_2\">";
        html += "</center></body></html>";
       
        NpcHtmlMessage htm = new NpcHtmlMessage(0);
        htm.setHtml(html);
        player.sendPacket(htm);
    }
   
    private void showAddPanel(L2PcInstance player, int page)
    {
        HashMap<Integer, ArrayList<ItemInstance>> items = new HashMap<>();
        int curr = 1;
        int counter = 0;
       
        ArrayList<ItemInstance> temp = new ArrayList<>();
        for (ItemInstance item : player.getInventory().getItems())
        {
        	if (item.getItemId() != 57 && item.isTradable() && !item.isEquipped())
            {
                temp.add(item);
               
                counter++;
               
                if (counter == 10)
                {
                    items.put(curr, temp);
                    temp = new ArrayList<>();
                    curr++;
                    counter = 0;
                }
            }
        }
        items.put(curr, temp);
       
        if (!items.containsKey(page))
        {
        	showAuctionHtml(player);
            player.sendMessage("Invalid page. Please try again.");
            return;
        }
       
        String html = "";
        html += "<html><title>Auction Manager</title><body><center><br1>";
        html += "Select item:";
        html += "<br><table width=100% bgcolor=000000 border=1>";
       
        for (ItemInstance item : items.get(page))
        {
            html += "<tr>";
            html += "<td>";
            html += "<img src=\"" + IconTable.getIcon(item.getItemId()) + "\" width=32 height=32 align=center></td>";
            html += "<td>" + (item.getEnchantLevel() > 0 ? " + " + item.getEnchantLevel() + " " + item.getName() : item.getName());
            html += "</td>";
            html += "<td><button value=\"Select\" action=\"bypass -h additem " + item.getObjectId() + "\" width=70 height=21 back=\"buttons_bs.bs_64x27_1\" fore=\"buttons_bs.bs_64x27_2\">";
            html += "</td>";
            html += "</tr>";
        }
        html += "</table><br><br>";
       
        html += "Page: "+page;
        html += "<br1>";
       
        if (items.keySet().size() > 1)
        {
            if (page > 1)
                html += "<a action=\"bypass -h addpanel " + (page-1) + "\"><- Prev</a>";
           
            if (items.keySet().size() > page)
                html += "<a action=\"bypass -h addpanel " + (page+1) + "\">Next -></a>";
        }
       
        html += "</center></body></html>";
       
        NpcHtmlMessage htm = new NpcHtmlMessage(0);
        htm.setHtml(html);
        player.sendPacket(htm);
    }
   
    private static void addItemToOffline(int playerId, int itemId, int count)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            PreparedStatement stm = con.prepareStatement("SELECT count FROM items WHERE owner_id=? AND item_id=?");
            stm.setInt(1, playerId);
            stm.setInt(2, itemId);
            ResultSet rset = stm.executeQuery();
           
            if (rset.next())
            {
                stm = con.prepareStatement("UPDATE items SET count=? WHERE owner_id=? AND item_id=?");
                stm.setInt(1, rset.getInt("count") + count);
                stm.setInt(2, playerId);
                stm.setInt(3, itemId);
               
                stm.execute();
            }
            else
            {
                stm = con.prepareStatement("INSERT INTO items VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                stm.setInt(1, playerId);
                stm.setInt(2, IdFactory.getInstance().getNextId());
                stm.setInt(3, itemId);
                stm.setInt(4, count);
                stm.setInt(5, 0);
                stm.setString(6, "INVENTORY");
                stm.setInt(7, 0);
                stm.setInt(8, 0);
                stm.setInt(9, 0);
                stm.setInt(10, 0);
                stm.setInt(11, -1);
                stm.setInt(12, 0);
               
                stm.execute();
            }
           
            rset.close();
            stm.close();
            con.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
   
    private void showAuction(L2PcInstance player, int page, String search)
    {
        boolean src = !search.equals("*null*");
       
        HashMap<Integer, ArrayList<AuctionHolder>> items = new HashMap<>();
        int curr = 1;
        int counter = 0;
       
        ArrayList<AuctionHolder> temp = new ArrayList<>();
        for (Map.Entry<Integer, AuctionHolder> entry : AuctionTable.getInstance().getItems().entrySet())
        {
            if (entry.getValue().getOwnerId() != player.getObjectId() && (!src || (src && ItemTable.getInstance().getTemplate(entry.getValue().getItemId()).getName().contains(search))))
            {
                temp.add(entry.getValue());
               
                counter++;
               
                if (counter == 10)
                {
                    items.put(curr, temp);
                    temp = new ArrayList<>();
                    curr++;
                    counter = 0;
                }
            }
        }
        items.put(curr, temp);
       
        if (!items.containsKey(page))
        {
        	showAuctionHtml(player);
            player.sendMessage("Invalid page. Please try again.");
            return;
        }
       
        String html = "<html><title>Auction Manager</title><body><center><br>";
        html += "<font color=\"FF6600\">Looking for a specific item?<br1></font>";
        html += "<img src=\"L2UI.SquareGray\" width=230 height=1><br>";
        html += "<edit var=srch width=150 height=13><br>";
        html += "<img src=\"L2UI.SquareGray\" width=230 height=1><br>";
        html += "<button value=\"Search\" action=\"bypass -h auction 1 - $srch\" width=80 height=13 back=\"buttons_bs.bs_64x15_1\" fore=\"buttons_bs.bs_64x15_2\">";
        html += "<br><table width=100% bgcolor=000000 border=1>";
        html += "<tr><td>Item</td><td>Cost</td><td></td></tr>";
        for (AuctionHolder item : items.get(page))
        {
            html += "<tr>";
            html += "<td><img src=\"" + IconTable.getIcon(item.getItemId()) + "\" width=32 height=32 align=center></td>";
            html += "<td>Item: " + (item.getEnchant() > 0 ? " + " + item.getEnchant() + " " + ItemTable.getInstance().getTemplate(item.getItemId()).getName() + " - " + item.getCount() : ItemTable.getInstance().getTemplate(item.getItemId()).getName() + " - " + item.getCount());
            html += "<br1>Cost: " + item.getCostCount() + " " + ItemTable.getInstance().getTemplate(item.getCostId()).getName();
            html += "</td>";
            html += "<td fixwidth=71><button value=\"Buy\" action=\"bypass -h buy " + item.getAuctionId() + "\" width=70 height=21 back=\"buttons_bs.bs_64x27_1\" fore=\"buttons_bs.bs_64x27_2\">";
            html += "</td></tr>";
        }
        html += "</table><br><br>";
       
        html += "Page: "+page;
        html += "<br1>";
       
        if (items.keySet().size() > 1)
        {
            if (page > 1)
                html += "<a action=\"bypass -h auction " + (page-1) + " - " + search + "\"><- Prev</a>";
           
            if (items.keySet().size() > page)
                html += "<a action=\"bypass -h auction " + (page+1) + " - " + search + "\">Next -></a>";
        }
       
        html += "</center></body></html>";
       
        NpcHtmlMessage htm = new NpcHtmlMessage(0);
        htm.setHtml(html);
        player.sendPacket(htm);
    }
    
	private void showAuctionHtml(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/menu/AuctionerManager.htm");
		activeChar.sendPacket(html);
		activeChar.sendPacket(ActionFailed.STATIC_PACKET);
	}
}