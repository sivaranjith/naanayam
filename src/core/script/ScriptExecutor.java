package core.script;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Stack;

import core.exceptions.ScriptExecutionException;
import core.utils.HashingUtils;

public final class ScriptExecutor
{
	public static void executeScript(final String originalMessage, String scriptSig, String scriptPubKey) throws ScriptExecutionException
	{
		final Stack<String> stackFrame = new Stack<>();
		final String[] tokens = (scriptSig + " " + scriptPubKey).split(" ");
		
		for(String token : tokens)
		{
			if(!token.startsWith("OP_"))
			{
				stackFrame.push(token);
			}
			else
			{
				operate((token.equals("OP_CHECKSIG") ? originalMessage : ""), token, stackFrame);
			}
			
		}
	}
	
	private static void operate(final String originalMessage, final String token, final Stack<String> stackFrame) throws ScriptExecutionException
	{
		switch(token)
		{
			case "OP_DUP":stackFrame.push(stackFrame.peek());
						break;
			case "OP_HASH160":stackFrame.push(HashingUtils.computeAddress(stackFrame.pop()));
						break;
			case "OP_EQUALVERIFY":
						if(stackFrame.pop().equals(stackFrame.pop()))
						{
							throw new ScriptExecutionException("Top two items are not the same. EQUALVERIFY has failed");
						}
						break;
			case "OP_CHECKSIG":
						try
						{
							final String pubKeyStr = stackFrame.pop(), signatureStr = stackFrame.pop();
							HashingUtils.verifySignedMessage(originalMessage, signatureStr, pubKeyStr);
						}
						catch(InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e)
						{
							throw new ScriptExecutionException(e);
						}
						break;
			default: throw new ScriptExecutionException("No such operation is available in the scripting language");
		}
	}


}
