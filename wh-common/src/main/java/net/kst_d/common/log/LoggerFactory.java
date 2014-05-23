package net.kst_d.common.log;

@SuppressWarnings ("rawtypes")
public final class LoggerFactory {

    private LoggerFactory() {
	//do nothing
    }

    public static Logger getLogger(final Class cls) {
	return getLogger(cls.getName());
    }

    public static Logger getLogger(final String name) {
	return new TicLogger(name);
    }
}
