
package net.tatans.coeus.exception;

public class ApplicationException extends TatansException {
	private static final long serialVersionUID = 1L;
	
	public ApplicationException() {}
	
	
	public ApplicationException(String msg) {
		super(msg);
	}
	
	public ApplicationException(Throwable ex) {
		super(ex);
	}
	
	public ApplicationException(String msg,Throwable ex) {
		super(msg,ex);
	}
	
}
