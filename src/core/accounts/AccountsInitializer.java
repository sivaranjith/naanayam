package core.accounts;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import core.utils.HashingUtils;

final class AccountsInitializer
{
	private static KeyFactory keyFactory;
	private static ECParameterSpec ecParamSpec;
	
	static
	{
		try
		{
			keyFactory = KeyFactory.getInstance("EC");
			ecParamSpec = HashingUtils.getECParamSpec();
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}
	
	public static KeyPair getKeyPair(final String privateKey, final String publicKeyPointX, final String publicKeyPointY)
	{
		PrivateKey privKey = null;
		PublicKey pubKey = null;
		try
		{
			final ECPrivateKeySpec ecSpec = new ECPrivateKeySpec(new BigInteger(privateKey), ecParamSpec);
			privKey = keyFactory.generatePrivate(ecSpec);
			
			final ECPoint ecPoint = new ECPoint(new BigInteger(publicKeyPointX), new BigInteger(publicKeyPointY));
			final KeySpec pubKeySpec = new ECPublicKeySpec(ecPoint, ecParamSpec);
			pubKey = keyFactory.generatePublic(pubKeySpec);
		}
		catch( InvalidKeySpecException e )
		{
			e.printStackTrace();
		}
		
		return new KeyPair(pubKey, privKey);
	}
}
