package core.accounts;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

final class AccountsGenerator
{
	private static final ECGenParameterSpec EC_SPEC = new ECGenParameterSpec("secp256k1");
	private static KeyPairGenerator keyGen;
	
	static
	{
		try
		{
			final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			keyGen = KeyPairGenerator.getInstance("EC");
			keyGen.initialize(EC_SPEC, random);
		}
		catch( NoSuchAlgorithmException | InvalidAlgorithmParameterException e )
		{
			e.printStackTrace();
		}
	}
	
	public static KeyPair getKeyPair()
	{
		return keyGen.generateKeyPair();
	}
}