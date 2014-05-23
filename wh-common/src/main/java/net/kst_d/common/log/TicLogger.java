package net.kst_d.common.log;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import net.kst_d.common.TicSessionID;


/**
 * LogByEvent включается при установке системного свойства <code>{@value #PROP_VALUE}</code>
 */
public class TicLogger implements Logger {

    public static final String PROP_VALUE = "bisyslog.event";
    protected final String fqcn;
    protected final org.apache.log4j.Logger logger;
    protected final InternalLogger logImpl;

    private static interface InternalLogger {
	void log(Priority level, TicSessionID ticSessionID, String method, String message, Throwable thr);
    }

    protected TicLogger(final String fqcn, final boolean byEvent) {
	this.fqcn = fqcn;
	logger = org.apache.log4j.Logger.getLogger(fqcn);
	logImpl = byEvent ? this::logByString : this::logByEvent;
    }


    protected TicLogger(final String fqcn) {
	this(fqcn, System.getProperty(PROP_VALUE) == null);

    }

    @Override
    public MethodLogger entering(final TicSessionID ticSessionID, final String method) {
	trace(ticSessionID, method, "start");
	return new TicMethodLogger(this, ticSessionID, method);
    }

    @Override
    public MethodLogger silentEnter(final TicSessionID ticSessionID, final String method) {
	return new TicMethodLogger(this, ticSessionID, method);
    }

    @Override
    public void exiting(final TicSessionID ticSessionID, final String method) {
	trace(ticSessionID, method, "stop");
    }

    @Override
    public void exiting(final TicSessionID ticSessionID, final String method, final Object result) {
	trace(ticSessionID, method, "stop, return {}", result);
    }

    @Override
    public String getName() {
	return fqcn;
    }

    @Override
    public boolean isTraceEnabled() {
	return logger.isEnabledFor(Level.TRACE);
    }

    @Override
    public void trace(final TicSessionID ticSessionID, final String method, final String format, final Object... argArray) {
	log(Level.TRACE, ticSessionID, method, format, null, argArray);
    }

    @Override
    public void trace(final TicSessionID ticSessionID, final String method, final String format, final Throwable thr, final Object... argArray) {
	log(Level.TRACE, ticSessionID, method, format, thr, argArray);
    }

    @Override
    public boolean isDebugEnabled() {
	return logger.isEnabledFor(Level.DEBUG);
    }

    @Override
    public void debug(final TicSessionID ticSessionID, final String method, final String format, final Object... argArray) {
	log(Level.DEBUG, ticSessionID, method, format, null, argArray);
    }

    @Override
    public void debug(final TicSessionID ticSessionID, final String format, final String msg, final Throwable thr, final Object... argArray) {
	log(Level.DEBUG, ticSessionID, format, format, thr, argArray);
    }

    @Override
    public boolean isInfoEnabled() {
	return logger.isEnabledFor(Level.INFO);
    }

    @Override
    public void info(final TicSessionID ticSessionID, final String method, final String format, final Object... argArray) {
	log(Level.INFO, ticSessionID, method, format, null, argArray);
    }

    @Override
    public void info(final TicSessionID ticSessionID, final String method, final String format, final Throwable thr, final Object... argArray) {
	log(Level.INFO, ticSessionID, method, format, thr, argArray);
    }

    @Override
    public boolean isWarnEnabled() {
	return logger.isEnabledFor(Level.WARN);
    }

    @Override
    public void warn(final TicSessionID ticSessionID, final String method, final String format, final Object... argArray) {
	log(Level.WARN, ticSessionID, method, format, null, argArray);
    }

    @Override
    public void warn(final TicSessionID ticSessionID, final String method, final String format, final Throwable thr, final Object... argArray) {
	log(Level.WARN, ticSessionID, method, format, thr, argArray);
    }

    @Override
    public boolean isErrorEnabled() {
	return logger.isEnabledFor(Level.ERROR);
    }

    @Override
    public void error(final TicSessionID ticSessionID, final String method, final String format, final Object... argArray) {
	log(Level.ERROR, ticSessionID, method, format, null, argArray);
    }

    @Override
    public void error(final TicSessionID ticSessionID, final String method, final String format, final Throwable thr, final Object... argArray) {
	log(Level.ERROR, ticSessionID, method, format, thr, argArray);
    }

    protected void log(final Priority level, final TicSessionID ticSessionID, final String method, final String format, final Throwable thr, final Object... argArray) {
	if (logger.isEnabledFor(level)) {
	    final String message = MessageFormatter.arrayFormat(format, argArray);
	    logImpl.log(level, ticSessionID, method, message, thr);
	}
    }

    private void logByEvent(final Priority level, final TicSessionID ticSessionID, final String method, final String message, final Throwable thr) {
	final TicLoggingEvent event = new TicLoggingEvent(fqcn, logger, level, message, thr, ticSessionID, method);
	logger.callAppenders(event);
    }

    private void logByString(final Priority level, final TicSessionID ticSessionID, final String method, final String message, final Throwable thr) {
	logger.log(level, String.valueOf(ticSessionID) + ":" + method + ": " + message, thr);

    }
}
