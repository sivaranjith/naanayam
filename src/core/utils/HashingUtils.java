package core.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import core.exceptions.ScriptExecutionException;

public final class HashingUtils
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
	
	public static ECParameterSpec getECParamSpec()
	{
		return ecParamSpec;
	}

	public static String sha256Hash(final String input) throws NoSuchAlgorithmException
	{
		final MessageDigest digest = MessageDigest.getInstance("SHA-256");
		final byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

		final StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < hash.length; i++)
		{
			final String hex = Integer.toHexString(0xff & hash[i]);
			
			if(hex.length() == 1)
			{
				hexString.append('0');
			}
			
			hexString.append(hex);
		}

		return hexString.toString();
	}
	
	public static String computeAddress(String input)
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
	
	public static String getSignedMessage(final String message, final String privKeyStr) throws NoSuchAlgorithmException, SignatureException, UnsupportedEncodingException, InvalidKeySpecException, InvalidKeyException
	{
		final KeySpec privKeySpec = new ECPrivateKeySpec(new BigInteger(privKeyStr), ecParamSpec);
		final PrivateKey privKey = keyFactory.generatePrivate(privKeySpec);
		final Signature dsa = Signature.getInstance("SHA1withECDSA");
		final byte[] strByte = message.getBytes("UTF-8");
		
		dsa.initSign(privKey);
		dsa.update(strByte);

		final byte[] realSig = dsa.sign();
		return new BigInteger(1, realSig).toString(16);
	}
	
	public static void verifySignedMessage(final String originalMessage, final String signatureStr, final String pubKeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, ScriptExecutionException
	{
		final String[] pubKeyPts = pubKeyStr.split(",");
		final ECPoint ecPoint = new ECPoint(new BigInteger(pubKeyPts[0]), new BigInteger(pubKeyPts[1]));
		final KeySpec pubKeySpec = new ECPublicKeySpec(ecPoint, ecParamSpec);
		final PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		
		final Signature signatureObj = Signature.getInstance("SHA1withECDSA");
		signatureObj.initVerify(pubKey);
		signatureObj.update(originalMessage.getBytes());
		
		if(signatureObj.verify(signatureStr.getBytes()))
		{
			throw new ScriptExecutionException("Exception while verifying the signature: " + signatureStr);
		}
				
	}
}