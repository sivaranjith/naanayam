package core.transaction;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import core.exceptions.ScriptExecutionException;
import core.exceptions.UTXOException;
import core.utils.HashingUtils;
import core.utxo.UTXODataBase;

public final class Transaction
{
	private final double version;
	private final int inputCount, outputCount;
	private final List<TransactionInput> transactionInputs;
	private final List<TransactionOutput> transactionOutputs;
	private final long lockTime;
	private final String transactionId;
	
	@SuppressWarnings("unchecked")
	public Transaction(final Map<String, Object> transactionDetails) throws UTXOException, ScriptExecutionException
	{
		this.version = (Double)transactionDetails.get("version");
		this.inputCount = (Integer)transactionDetails.get("inputCount");
		this.outputCount = (Integer)transactionDetails.get("outputCount");
		this.lockTime = (Long)transactionDetails.get("lockTime");
		
		this.transactionInputs = Collections.unmodifiableList(TransactionInput.getAsList((List<Map<String, Object>>)transactionDetails.get("transactionInputs")));
		this.transactionOutputs = Collections.unmodifiableList(TransactionOutput.getAsList((List<Map<String, Object>>)transactionDetails.get("transactionOutputs")));
	
		String currentTransactionId = "";
		try
		{
			currentTransactionId = HashingUtils.sha256Hash(toString());
		}
		catch( NoSuchAlgorithmException e )
		{
			e.printStackTrace();
		}
		
		this.transactionId = currentTransactionId;
		
		UTXODataBase.validateTransaction(this);
	}
	
	public String getSignableString()
	{
		final String outputStr = this.transactionOutputs.toString();
		return this.version + "" + this.inputCount + "" + this.outputCount + outputStr.substring(1, outputStr.length()-1) + this.lockTime; 
	}
	
	public String getTransactionId()
	{
		return this.transactionId;
	}
	
	@Override
	public String toString()
	{
		final String inputStr = this.transactionInputs.toString(), outputStr = this.transactionOutputs.toString();
		return this.version + "" + this.inputCount + inputStr.substring(1, inputStr.length()-1) + this.outputCount + outputStr.substring(1, outputStr.length()-1) + this.lockTime;
	}
	
	public List<TransactionInput> getInputTransactionList()
	{
		return this.transactionInputs;
	}
	
	public List<TransactionOutput> getOutputTransactionList()
	{
		return this.transactionOutputs;
	}
}