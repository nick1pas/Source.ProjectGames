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
package net.sf.l2j.gameserver.datatables.custom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.PledgeImage;
import net.sf.l2j.util.Rnd;

/**
 * 
 * @author Fissban
 *
 */
public class AntiBotTable
{
	public static Logger _log = Logger.getLogger(AntiBotTable.class.getName());

	public static Map<Integer, antiBotData> _imageAntiBotOri = new HashMap<>();
	public static Map<Integer, antiBotData> _imageAntiBotClient = new HashMap<>();

	public final static int[] img_antibot_id =
		{
		7000, 7001, 7002, 7003, 7004, 7005, 7006, 7007, 7008, 7009
		};

	public void loadImage()
	{
		LoadImgAntiBot();
		_log.log(Level.INFO, "loading " + _imageAntiBotOri.size() + " images of AntiBot");
	}

	private static void LoadImgAntiBot()
	{
		_imageAntiBotOri.clear();
		int cont = 0;

		for (int imgId : img_antibot_id)
		{
			File image = new File("data/images/antibot/" + imgId + ".dds");
			_imageAntiBotOri.put(cont, new antiBotData(cont, ConverterImgBytes(image)));
			cont++;
		}

		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new startEncriptCaptcha(), 100, 600000); // 10 Minutes
	}

	public void sendImage(L2PcInstance player, int imgId)
	{
		PledgeImage packet = null;

		if ((imgId >= 50000) && (imgId <= 800000))
		{
			for (Entry<Integer, antiBotData> entrySet : _imageAntiBotClient.entrySet())
			{
				antiBotData imgCoding = entrySet.getValue();

				if (imgId == imgCoding.getCodificacion())
				{
					packet = new PledgeImage(imgId, imgCoding.getImagen());
				}
			}
		}

		player.sendPacket(packet);
	}

	public static class startEncriptCaptcha implements Runnable
	{
		public startEncriptCaptcha()
		{

		}

		@Override
		public void run()
		{
			_imageAntiBotClient.clear();

			for (Entry<Integer, antiBotData> entrySet : _imageAntiBotOri.entrySet())
			{
				entrySet.getValue().getImagen();
				_imageAntiBotClient.put(entrySet.getKey(), new antiBotData(Rnd.get(50000, 800000), entrySet.getValue().getImagen()));
			}
		}
	}

	public int getAntiBotClientID(int pos)
	{
		int returnCoding = 0;

		for (Entry<Integer, antiBotData> entrySet : _imageAntiBotClient.entrySet())
		{
			int numeroImage = entrySet.getKey().intValue(); 

			if (pos == numeroImage)
			{
				antiBotData imgCoding = entrySet.getValue();
				returnCoding = imgCoding.getCodificacion();
			}

			if (pos > 9)
			{
				_log.log(Level.SEVERE, "error in getAntiBotClientID...number dont exist");
			}
		}
		return returnCoding;
	}

	public static class antiBotData
	{
		int _codificacion;
		byte[] _data;

		public antiBotData(int codificacion, byte[] data)
		{
			_codificacion = codificacion;
			_data = data;
		}

		public int getCodificacion()
		{
			return _codificacion;
		}

		public byte[] getImagen()
		{
			return _data;
		}
	}

	private static byte[] ConverterImgBytes(File imagen)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		try (FileInputStream fis = new FileInputStream(imagen))
		{
			for (int readNum; (readNum = fis.read(buffer)) != -1;)
			{
				bos.write(buffer, 0, readNum);
			}
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, "Error when converter image to byte[]");
		}

		return bos.toByteArray();
	}

	public static AntiBotTable getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final AntiBotTable _instance = new AntiBotTable();
	}
}