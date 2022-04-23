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
package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 * Format: ch ddcdc
 * @author KenM
 */
public class ExPCCafePointInfo extends L2GameServerPacket
{
	private L2PcInstance _character;
	private int m_AddPoint;
	private int m_PeriodType;
	private int RemainTime;
	private int PointType;

	public ExPCCafePointInfo(L2PcInstance user, int modify, boolean add, int hour, boolean _double)
	{
		_character = user;
		m_AddPoint = modify;
		
	    if (add)
		{
		      m_PeriodType = 1;
		      PointType = 1;
		}
		else
		{
		      if (add && _double)
		      {
		              m_PeriodType = 1;
		              PointType = 0;
		      }
		      else
		      {
		              m_PeriodType = 2;
		              PointType = 2;
		      }
		}
		
		RemainTime = hour;

	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x31);
		writeD(_character.getPcBangScore());
		writeD(m_AddPoint);
		writeC(m_PeriodType);
		writeD(RemainTime);
		writeC(PointType);
	}
}