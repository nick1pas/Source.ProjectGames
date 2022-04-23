package net.sf.l2j.gameserver.cache;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sf.l2j.gameserver.idfactory.IdFactory;

import gov.nasa.worldwind.formats.dds.DDSConverter;

public class ImagesCache
{
	public static final Logger _log = Logger.getLogger(ImagesCache.class.getName());
	
	private static final int[] SIZES =
	{
		1,
		2,
		4,
		8,
		16,
		32,
		64,
		128,
		256,
		512,
		1024
	};
	private static final int MAX_SIZE = SIZES[(SIZES.length - 1)];
	
	public static final Pattern HTML_PATTERN = Pattern.compile("%image:(.*?)%", 32);

	private static final ImagesCache _instance = new ImagesCache();
	
	private final Map<String, Integer> _imagesId = new HashMap<>();
	
	private final Map<Integer, byte[]> _images = new HashMap<>();
	
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private final Lock readLock = this.lock.readLock();
	
	private final Lock writeLock = this.lock.writeLock();
	
	public static final ImagesCache getInstance()
	{
		return _instance;
	}
	
	private ImagesCache()
	{
		load();
	}
	
	public void load()
	{
		_log.info("ImagesChache: Loading images...");
		
		File dir = new File("./data/images/");
		if ((!dir.exists()) || (!dir.isDirectory()))
		{
			_log.info("ImagesChache: Files missing, loading aborted.");
			return;
		}
		
		int count = 0;
		for (File file : dir.listFiles())
		{
			if (!file.isDirectory())
			{
				file = resizeImage(file);
				if (file != null)
				{
					count++;
					
					String fileName = file.getName();
					try
					{
						ByteBuffer bf = DDSConverter.convertToDDS(file);
						byte[] image = bf.array();
						int imageId = IdFactory.getInstance().getNextId();
						
						this._imagesId.put(fileName.toLowerCase(), Integer.valueOf(imageId));
						this._images.put(imageId, image);
						
						_log.info("ImagesChache: Loaded " + fileName + " image.");
					}
					catch (IOException ioe)
					{
						_log.warning("ImagesChache: Error while loading " + fileName + " image.");
					}
				}
			}
		}
		_log.info("ImagesChache: Loaded " + count + " images");
	}
	
	private static File resizeImage(File file)
	{
		BufferedImage image;
		try
		{
			image = ImageIO.read(file);
		}
		catch (IOException ioe)
		{
			_log.warning("ImagesChache: Error while resizing " + file.getName() + " image.");
			return null;
		}
		
		if (image == null)
		{
			return null;
		}
		int width = image.getWidth();
		int height = image.getHeight();
		
		boolean resizeWidth = true;
		if (width > MAX_SIZE)
		{
			image = image.getSubimage(0, 0, MAX_SIZE, height);
			resizeWidth = false;
		}
		
		boolean resizeHeight = true;
		if (height > MAX_SIZE)
		{
			image = image.getSubimage(0, 0, width, MAX_SIZE);
			resizeHeight = false;
		}
		
		int resizedWidth = width;
		if (resizeWidth)
		{
			for (int size : SIZES)
			{
				if (size >= width)
				{
					resizedWidth = size;
					break;
				}
			}
		}
		int resizedHeight = height;
		if (resizeHeight)
		{
			for (int size : SIZES)
			{
				if (size >= height)
				{
					resizedHeight = size;
					break;
				}
			}
		}
		if ((resizedWidth != width) || (resizedHeight != height))
		{
			for (int x = 0; x < resizedWidth; x++)
			{
				for (int y = 0; y < resizedHeight; y++)
				{
					image.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
			String filename = file.getName();
			String format = filename.substring(filename.lastIndexOf("."));
			try
			{
				ImageIO.write(image, format, file);
			}
			catch (IOException ioe)
			{
				_log.warning("ImagesChache: Error while resizing " + file.getName() + " image.");
				return null;
			}
		}
		return file;
	}
	
	public int getImageId(String val)
	{
		int imageId = 0;
		
		this.readLock.lock();
		try
		{
			if (this._imagesId.get(val.toLowerCase()) != null)
			{
				imageId = this._imagesId.get(val.toLowerCase()).intValue();
			}
		}
		finally
		{
			this.readLock.unlock();
		}
		
		return imageId;
	}
	
	public byte[] getImage(int imageId)
	{
		byte[] image = null;
		
		this.readLock.lock();
		try
		{
			image = this._images.get(imageId);
		}
		finally
		{
			this.readLock.unlock();
		}
		
		return image;
	}

	public Lock getWriteLock()
	{
		return writeLock;
	}
}