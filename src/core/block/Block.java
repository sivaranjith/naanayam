package core.block;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import core.exceptions.BlockCreationException;
import core.transaction.Transaction;
import core.utils.HashingUtils;

/*
 * remember that coinbase transaction also should be send along the transaction list
 * also the all the transaction should have been validated before
 *
 */
public final class Block
{
	private int version, transactionCount;
	private long timeStamp, nonce;
	private String previousBlockHash, blockDetailsAsString;
	private BigInteger nBits;
	private String merkleTreeRoot;
	private List<Transaction> transactions;
	
	@SuppressWarnings("unchecked")
	public Block(final Map<String, Object> blockDetails) throws BlockCreationException
	{
		try
		{
			this.version = (Integer)blockDetails.get("version");
			this.timeStamp = System.currentTimeMillis();
			this.nonce = 0L;
			this.previousBlockHash = (String)blockDetails.get("previousBlockHash");
			this.transactions = Collections.unmodifiableList((List<Transaction>)blockDetails.get("transactions"));
			this.transactionCount = this.transactions.size();
			this.nBits = new BigInteger("19015f53", 16);
			this.merkleTreeRoot = getMerkleTreeRoot();
			this.blockDetailsAsString = getBlockDetailsAsString();
			
			computeProofOfWork();
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			new BlockCreationException("Technical problem in creating block");
		}
	}
	
	private String getMerkleTreeRoot() throws NoSuchAlgorithmException
	{
		final List<String> transactionIdList = this.transactions.stream().map(t->t.getTransactionId()).collect(Collectors.toList());
		final Queue<String> hashHolder = new LinkedList<>(transactionIdList);
		String merkleTreeRootHash = "";
		
		while(!hashHolder.isEmpty())
		{
			final String topElement1 = hashHolder.poll(), topElement2 = hashHolder.poll();
			
			if(topElement2 == null)
			{
				break;
			}
			
			hashHolder.add(HashingUtils.sha256Hash(topElement1 + topElement2));
		}
		
		return merkleTreeRootHash;
	}
	
	private void computeProofOfWork() throws BlockCreationException, NoSuchAlgorithmException
	{
		for(;this.nonce < Long.MAX_VALUE; ++this.nonce)
		{
			final String hash = HashingUtils.sha256Hash(this.blockDetailsAsString + this.nonce);
			
			if(this.nBits.compareTo(new BigInteger(hash, 16)) < 0)
			{
				break;
			}
		}
		
		if(((Long)Long.MAX_VALUE).equals(this.nonce))
		{
			throw new BlockCreationException("nonce available to get a valid proof of work");
		}
	}
	
	private String getBlockDetailsAsString()
	{
		return this.version + "" + this.timeStamp + this.previousBlockHash + this.transactions.toString() + this.transactionCount + this.nBits + this.merkleTreeRoot;
	}
}
