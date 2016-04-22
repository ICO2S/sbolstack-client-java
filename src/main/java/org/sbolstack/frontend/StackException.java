package org.sbolstack.frontend;

public class StackException extends Exception
{
	  static final long serialVersionUID = 1;
	
	  public StackException()
	  {
		  super();
	  }
	  
	  public StackException(String message)
	  {
		  super(message);
	  }
	  
	  public StackException(String message, Throwable cause)
	  {
		  super(message, cause);
	  }
	  
	  public StackException(Throwable cause)
	  {
		  super(cause);
	  }
}
