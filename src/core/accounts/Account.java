package core.accounts;

import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import core.utils.HashingUtils;

public final class Account
{
	private final ECPrivateKey privKey;
	private final ECPublicKey pubKey;
	private final String address;
	private final Map<String, String> accountDetails;

	public Account()
	{
        final KeyPair pair = AccountsGenerator.getKeyPair();
        
        this.privKey = (ECPrivateKey)pair.getPrivate();
        this.pubKey = (ECPublicKey)pair.getPublic();
		
		final ECPoint ecPointObj = this.pubKey.getW();
		this.address = HashingUtils.computeAddress(new String(ecPointObj.getAffineX() + "," + ecPointObj.getAffineY()));
        this.accountDetails = Collections.unmodifiableMap(fillAccountDetails());
	}
	
	public Account(final String privateKey, final String publicKeyPointX, final String publicKeyPointY)
	{
		final KeyPair pair = AccountsInitializer.getKeyPair(privateKey, publicKeyPointX, publicKeyPointY);
		
		this.privKey = (ECPrivateKey)pair.getPrivate();
		this.pubKey = (ECPublicKey)pair.getPublic();
		
		final ECPoint ecPointObj = this.pubKey.getW();
		this.address = HashingUtils.computeAddress(new String(ecPointObj.getAffineX() + "," + ecPointObj.getAffineY()));
		this.accountDetails = Collections.unmodifiableMap(fillAccountDetails());
	}
	
	public Map<String, String> getAccountDetails()
	{
		return this.accountDetails;
	}

	private Map<String, String> fillAccountDetails()
	{
		final Map<String, String> accountDetails = new HashMap<String, String>();
		
		accountDetails.put("privateKey", this.privKey.getS().toString());
		accountDetails.put("publicKeyX", this.pubKey.getW().getAffineX().toString());
		accountDetails.put("publicKeyY", this.pubKey.getW().getAffineY().toString());
		accountDetails.put("publicKey", new String(this.pubKey.getEncoded()));
		accountDetails.put("address", this.address);
		
		return accountDetails;
	}
}
