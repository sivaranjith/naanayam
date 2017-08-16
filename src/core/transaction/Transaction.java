package core.transaction;

import java.util.List;

public final class Transaction
{
	private final double version;
	private final int inputCount;
	private final List<TransactionInput> transactionInputs;
	private final List<TransactionOutput> transactionOutputs;
	private final long locktime;
	private final String currentTransactionId;
}

final class TransactionInput
{
	private final String previousOutputHash, scriptSig, sequence;
	private final int previousOutputIndex, scriptLength;
}

final class TransactionOutput
{
	private final int value, scriptLength;
	private final String scriptPubKey;
}
