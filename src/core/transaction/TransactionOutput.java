package core.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class TransactionOutput
{
	private final int value, scriptLength;
	private final String scriptPubKey;
	
	TransactionOutput(final Map<String, Object> outputDetails)
	{
		this.scriptPubKey = (String)outputDetails.get("scriptPubKey");
		this.value = (Integer)outputDetails.get("value");
		this.scriptLength = (Integer)outputDetails.get("scriptLength");
	}
	
	static List<TransactionOutput> getAsList(final List<Map<String, Object>> inputDetails)
	{
		final List<TransactionOutput> inputList = new ArrayList<>();
		
		for(Map<String, Object> input : inputDetails)
		{
			inputList.add(new TransactionOutput(input));
		}
		
		return inputList;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public String getScriptPubKey()
	{
		return this.scriptPubKey;
	}
	
	@Override
	public String toString()
	{
		return this.value + "" + this.scriptLength + this.scriptPubKey;
	}
}
