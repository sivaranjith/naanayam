package core.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class TransactionInput
{
	private final String previousOutputHash, scriptSig, sequence;
	private final int previousOutputIndex, scriptLength;
	
	TransactionInput(final Map<String, Object> inputDetails)
	{
		this.previousOutputHash = (String)inputDetails.get("previousOutputHash");
		this.scriptSig = (String)inputDetails.get("scriptSig");
		this.sequence = (String)inputDetails.get("sequence");
		this.previousOutputIndex = (Integer)inputDetails.get("previousOutputIndex");
		this.scriptLength = (Integer)inputDetails.get("scriptLength");
	}
	
	static List<TransactionInput> getAsList(final List<Map<String, Object>> inputDetails)
	{
		final List<TransactionInput> inputList = new ArrayList<>();
		
		for(Map<String, Object> input : inputDetails)
		{
			inputList.add(new TransactionInput(input));
		}
		
		return inputList;
	}
	
	public String getPreviousTransactionId()
	{
		return this.previousOutputHash;
	}
	
	public int getPreviousTransactionIndex()
	{
		return this.previousOutputIndex;
	}
	
	public String getScriptSig()
	{
		return this.scriptSig;
	}
	
	@Override
	public String toString()
	{
		return this.previousOutputIndex + this.previousOutputHash + this.scriptLength + this.scriptSig + this.sequence;
	}
}
