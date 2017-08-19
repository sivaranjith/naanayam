package core.exceptions;

import java.util.logging.Logger;

public class UTXOException extends Exception
{

	private static final long serialVersionUID = 1L;
	final String message;
	final static Logger LOGGER = Logger.getLogger(UTXOException.class.toString());

	public UTXOException(final String transactionId, final int transactionIndex)
	{
		this(transactionId + ":" + transactionIndex + " this trnasaction and the index doesn't exist in the utxo data base");
	}
	
	public UTXOException(final String message)
	{
		super(message);
		this.message = message;
	}
	
	public UTXOException(final Throwable cause)
	{
		super(cause);
		this.message = cause.getMessage();
	}
	
	public String getLocalizedMessage()
	{
		LOGGER.info(this.message);
		return this.message;
	}
}
