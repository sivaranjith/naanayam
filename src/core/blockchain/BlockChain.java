package core.blockchain;

import core.block.Block;

public final class BlockChain
{
	//head pointer for the block chain
	private BlockNode headPointer;

	//the node of a block chain has the corresponding block and along with the length of the current block node starting from gensis block
	private static final class BlockNode
	{
		private final Block blockPt;
		private final int length;
		private final BlockNode previousBlock;
	}
}
