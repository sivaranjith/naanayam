package core.block;

import java.util.List;

public final class Block
{
	private final int version, transactionCount;
	private final long timeStamp, nonce;
	private final String previousBlockHash, merkleTreeRoot, nBits;
	private final List<String> transactions;
}
