package ams.cms.exception;

public class FileStorageException extends RuntimeException{

	private static final long serialVersionUID = 6377647769724311793L;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FileStorageException(String message) {
		super();
		this.message = message;
	}

	public FileStorageException() {
		super();
	}

	public FileStorageException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public FileStorageException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FileStorageException(Throwable arg0) {
		super(arg0);
	}
	
}
