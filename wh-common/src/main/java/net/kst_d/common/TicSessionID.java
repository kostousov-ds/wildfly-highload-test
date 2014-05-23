package net.kst_d.common;


import java.io.Serializable;

/**
 * Уникальный (почти) идентификатор внутренней сессии.
 * Под сессией понимается цепочка обработки одного внешнего запроса, не зависимо от способа его поступления.
 * Это может быть WebService, RADIUS или что-то еще.
 *
 * @see com.techinfocom.common.SessionIDGenerator
 */
public class TicSessionID implements Serializable {
    private static final long serialVersionUID = -2350173297320393192L;

    public static final TicSessionID EMPTY_ID = new TicSessionID("none");

    private final String sessionID;

    public TicSessionID(final String sessionID) {
	this.sessionID = sessionID;
    }

    public TicSessionID() {
	this.sessionID = Long.toHexString(System.nanoTime());
    }

    public String getSessionID() {
	return sessionID;
    }

    public String toString() {
	return sessionID;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || getClass() != o.getClass()) {
	    return false;
	}

	TicSessionID that = (TicSessionID) o;

	return sessionID.equals(that.sessionID);

    }

    @Override
    public int hashCode() {
	return sessionID.hashCode();
    }
}

