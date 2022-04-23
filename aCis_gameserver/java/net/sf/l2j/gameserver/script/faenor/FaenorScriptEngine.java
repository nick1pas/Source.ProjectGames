/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.script.faenor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.script.ScriptContext;

import net.sf.l2j.gameserver.script.Parser;
import net.sf.l2j.gameserver.script.ParserNotCreatedException;
import net.sf.l2j.gameserver.script.ScriptDocument;
import net.sf.l2j.gameserver.script.ScriptEngine;
import net.sf.l2j.util.XMLFilter;

import org.w3c.dom.Node;

/**
 * @author Luis Arias
 */
public class FaenorScriptEngine extends ScriptEngine
{
	private static final Logger _log = Logger.getLogger(FaenorScriptEngine.class.getName());

	protected FaenorScriptEngine()
	{
		final File packDirectory = new File("data/xml/faenor/");
		final File[] files = packDirectory.listFiles(new XMLFilter());
		for (File file : files)
		{
			try (InputStream in = new FileInputStream(file))
			{
				parseScript(new ScriptDocument(file.getName(), in), null);
			}
			catch (IOException e)
			{
				_log.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

	public void parseScript(ScriptDocument script, ScriptContext context)
	{
		Node node = script.getDocument().getFirstChild();
		String parserClass = "faenor.Faenor" + node.getNodeName() + "Parser";
		
		Parser parser = null;
		try
		{
			parser = createParser(parserClass);
		}
		catch (ParserNotCreatedException e)
		{
			_log.log(Level.WARNING, "ERROR: No parser registered for Script: " + parserClass + ": " + e.getMessage(), e);
		}
		
		if (parser == null)
		{
			_log.warning("Unknown Script Type: " + script.getName());
			return;
		}
		
		try
		{
			parser.parseScript(node, context);
			_log.info(getClass().getSimpleName() + ": Loaded  " + script.getName() + " successfully.");
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Script Parsing Failed: " + e.getMessage(), e);
		}
	}

	public static FaenorScriptEngine getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final FaenorScriptEngine _instance = new FaenorScriptEngine();
	}
}
