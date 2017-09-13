
package core.blockchain;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import core.block.Block;
import core.utils.FileUtils;

public final class BlockChain
{
	//head pointer for the block chain
	private static List<BlockNode> headPointer = new LinkedList<>();
	private static final int MAX_QUEUE_SIZE = 5;
	
	public static synchronized void addBlock(final Block blockObj)
	{
		if(headPointer.size() == MAX_QUEUE_SIZE)
		{
			final String firstNodeStr = headPointer.remove(0).toString();	
			try
			{
				FileUtils.writeToFile("blockchaindata.txt", firstNodeStr);
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		if(headPointer == null)
		{
			headPointer.add(new BlockNode(blockObj, 0));
		}
		else
		{
			final BlockNode headNode = headPointer.get(headPointer.size() - 1);
			headPointer.add(new BlockNode(blockObj, headNode.length + 1));
		}
	}
	
	//the node of a block chain has the corresponding block and along with the length of the current block node starting from gensis block
	private static final class BlockNode
	{
		final Block blockObj;
		final int length;
		
		BlockNode(final Block blockObj, final int length)
		{
			this.blockObj = blockObj;
			this.length = length;
		}
		
		@Override
		public String toString()
		{
			return this.blockObj + "" + this.length;
		}
	}
}
