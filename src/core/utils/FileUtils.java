package core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileUtils
{
	public static void writeToFile(final String fileName, final String content) throws IOException
	{
		final File dataFile = new File(fileName);
		if(!dataFile.exists())
		{
			dataFile.createNewFile();
		}
		
		try(final FileWriter fw = new FileWriter(fileName, true);final BufferedWriter bw = new BufferedWriter(fw))
		{
			bw.write(content);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			throw e;
		}
	}
}
