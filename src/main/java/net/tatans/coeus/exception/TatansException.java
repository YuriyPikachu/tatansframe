
package net.tatans.coeus.exception;

public class TatansException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public TatansException() {
		super();
	}
	
	public TatansException(String msg) {
		super(msg);
	}
	
	public TatansException(Throwable ex) {
		super(ex);
	}
	
	public TatansException(String msg,Throwable ex) {
		super(msg,ex);
	}

}
