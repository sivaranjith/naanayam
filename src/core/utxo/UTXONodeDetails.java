package core.utxo;

final class UTXONodeDetails
{
	final int value;
	final String scriptPubKey;
	
	UTXONodeDetails(final int value, final String scriptPubKey)
	{
		this.value = value;
		this.scriptPubKey = scriptPubKey;
	}
}