package net.tatans.coeus.network.view;

public class CoeusException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public CoeusException() {
		super();
	}
	
	public CoeusException(String msg) {
		super(msg);
	}
	
	public CoeusException(Throwable ex) {
		super(ex);
	}
	
	public CoeusException(String msg,Throwable ex) {
		super(msg,ex);
	}

}
