package net.kst_d.common.log;

import net.kst_d.common.TicSessionID;

public class TicMethodLogger implements MethodLogger {
    private Logger logger;
    private TicSessionID ticSessionID;
    private String method;

    protected TicMethodLogger(final Logger logger, final TicSessionID ticSessionID, final String method) {
	this.logger = logger;
	this.ticSessionID = ticSessionID;
	this.method = method;
    }

    @Override
    public Logger parent() {
	return logger;
    }

    @Override
    public TicSessionID ticSessionID() {
	return ticSessionID;
    }

    @Override
    public String method() {
	return method;
    }

    @Override
    public void exiting(final Object result) {
	logger.exiting(ticSessionID, method, result);
    }

    @Override
    public void exiting() {
	logger.exiting(ticSessionID, method);
    }

    @Override
    public String getName() {
	return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
	return logger.isTraceEnabled();
    }

    @Override
    public void trace(final String format, final Object... argArray) {
	logger.trace(ticSessionID, method, format, argArray);
    }

    @Override
    public void trace(final String format, final Throwable tht, final Object... argArray) {
	logger.trace(ticSessionID, method, format, tht, argArray);
    }

    @Override
    public boolean isDebugEnabled() {
	return logger.isDebugEnabled();
    }

    @Override
    public void debug(final String format, final Object... argArray) {
	logger.debug(ticSessionID, method, format, argArray);
    }

    @Override
    public void debug(final String msg, final Throwable thr, final Object... argArray) {
	logger.debug(ticSessionID, method, msg, thr, argArray);
    }

    @Override
    public boolean isInfoEnabled() {
	return logger.isInfoEnabled();
    }

    @Override
    public void info(final String format, final Object... argArray) {
	logger.info(ticSessionID, method, format, argArray);
    }

    @Override
    public void info(final String format, final Throwable thr, final Object... argArray) {
	logger.info(ticSessionID, method, format, thr, argArray);
    }

    @Override
    public boolean isWarnEnabled() {
	return logger.isWarnEnabled();
    }

    @Override
    public void warn(final String format, final Object... argArray) {
	logger.warn(ticSessionID, method, format, argArray);
    }

    @Override
    public void warn(final String format, final Throwable thr, final Object... argArray) {
	logger.warn(ticSessionID, method, format, thr, argArray);
    }

    @Override
    public boolean isErrorEnabled() {
	return logger.isErrorEnabled();
    }

    @Override
    public void error(final String format, final Object... argArray) {
	logger.error(ticSessionID, method, format, argArray);
    }

    @Override
    public void error(final String format, final Throwable thr, final Object... argArray) {
	logger.error(ticSessionID, method, format, thr, argArray);
    }
}
