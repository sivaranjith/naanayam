package core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashingUtils
{
	public static String sha256Hash(final String input) throws NoSuchAlgorithmException
	{
		final MessageDigest digest = MessageDigest.getInstance("SHA-256");
		final byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

		final StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < hash.length; i++)
		{
			String hex = Integer.toHexString(0xff & hash[i]);
			
			if(hex.length() == 1)
			{
				hexString.append('0');
			}
			
			hexString.append(hex);
		}

		return hexString.toString();
	}
}