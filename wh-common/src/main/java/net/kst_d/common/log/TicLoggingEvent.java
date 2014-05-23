package net.kst_d.common.log;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

import net.kst_d.common.TicSessionID;


public class TicLoggingEvent extends LoggingEvent {

    private static final long serialVersionUID = -4130328551352959711L;
    private final TicSessionID ticSessionID;
    private String method;
    private Object msg;

    public TicLoggingEvent(final String fqnOfCategClass, final Category logger, final Priority level, final String message, final Throwable throwable, final TicSessionID ticSessionID, final String method) {
	super(fqnOfCategClass, logger, level, ticSessionID + ":" + method + ": " + message, throwable);
	this.ticSessionID = ticSessionID;
	this.method = method;
	msg = message;
    }

    public TicSessionID getTicSessionID() {
	return ticSessionID;
    }

    public String getMethod() {
	return method;
    }

    public Object getMsg() {
	return msg;
    }
}
