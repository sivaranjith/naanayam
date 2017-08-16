package core.accounts;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

final class AccountsInitializer
{
	private static final ECGenParameterSpec EC_SPEC = new ECGenParameterSpec("secp256k1");
	private static KeyPairGenerator keyGen;
	private static KeyFactory keyFactory;
	private static ECParameterSpec ecParamSpec;
	
	static
	{
		try
		{
			keyFactory = KeyFactory.getInstance("EC");
			keyGen = KeyPairGenerator.getInstance("EC");
			keyGen.initialize(EC_SPEC);
			
			KeyPair apair = keyGen.generateKeyPair(); 
		    ECPublicKey apub  = (ECPublicKey)apair.getPublic();
		    ecParamSpec = apub.getParams();
		}
		catch( NoSuchAlgorithmException | InvalidAlgorithmParameterException e )
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
