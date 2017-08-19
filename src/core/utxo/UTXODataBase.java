package core.utxo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import core.exceptions.ScriptExecutionException;
import core.exceptions.UTXOException;
import core.script.ScriptExecutor;
import core.transaction.Transaction;
import core.transaction.TransactionInput;
import core.transaction.TransactionOutput;

public final class UTXODataBase
{
	private static final Map<String, Map<Integer, UTXONodeDetails>> utxoMap = new HashMap<>();
	
	public static synchronized boolean validateTransaction(final Transaction transactionObj) throws UTXOException, ScriptExecutionException
	{
		for(TransactionInput input : transactionObj.getInputTransactionList())
		{
			final String previousTransactionId = input.getPreviousTransactionId(), scriptSig = input.getScriptSig();
			final int previousTransactionIndex = input.getPreviousTransactionIndex();

			final String scriptPubKey = Optional.ofNullable(utxoMap.get(previousTransactionId))
												.map(transaciton->transaciton.get(previousTransactionId))
												.orElseThrow(()->new UTXOException(previousTransactionId, previousTransactionIndex))
												.scriptPubKey;
			
			ScriptExecutor.executeScript(transactionObj.getSignableString(), scriptSig, scriptPubKey);
		}

		addTransaction(transactionObj);
		return true;
	}
	
	private static void addTransaction(final Transaction transactionObj)
	{
		final String transactionId = transactionObj.getTransactionId();
		final Map<Integer, UTXONodeDetails> utxoNodeList = new HashMap<>();
		final List<TransactionOutput> outputList = transactionObj.getOutputTransactionList();
		utxoMap.put(transactionId, utxoNodeList);
		
		for(int i = 0; i < outputList.size(); ++i)
		{
			final TransactionOutput output = outputList.get(i);
			utxoNodeList.put(i, new UTXONodeDetails(output.getValue(), output.getScriptPubKey()));
		}
	}
}