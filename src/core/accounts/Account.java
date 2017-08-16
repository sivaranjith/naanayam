package core.accounts;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import core.utils.HashingUtils;

public final class Account
{
	private final ECPrivateKey privKey;
	private final ECPublicKey pubKey;
	private final String address;

	public Account()
	{
        final KeyPair pair = AccountsGenerator.getKeyPair();
        privKey = (ECPrivateKey)pair.getPrivate();
        pubKey = (ECPublicKey)pair.getPublic();
        address = computeAddress(new String(pubKey.getEncoded()));
	}
	
	public Account(final String privateKey, final String publicKeyPointX, final String publicKeyPointY)
	{
		final KeyPair pair = AccountsInitializer.getKeyPair(privateKey, publicKeyPointX, publicKeyPointY);
		privKey = (ECPrivateKey)pair.getPrivate();
        pubKey = (ECPublicKey)pair.getPublic();
        address = computeAddress(new String(pubKey.getEncoded()));
	}

	private String computeAddress(String input)
	{
		try
		{
			input = HashingUtils.sha256Hash(input);
			input = HashingUtils.sha256Hash(input);
			return input;
		}
		catch( NoSuchAlgorithmException e )
		{
			e.printStackTrace();
			return null;
		}
	}
}
