package net.kst_d.common;

public class HttpInteractionException extends Exception{
    public HttpInteractionException(String message) {
	super(message);
    }

    public HttpInteractionException(Throwable cause) {
	super(cause);
    }
}
