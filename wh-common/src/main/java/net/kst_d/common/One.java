package net.kst_d.common;

public class One<T> {
    public final T _1;

    public One(T _1) {
	this._1 = _1;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || getClass() != o.getClass()) {
	    return false;
	}

	One one = (One) o;

	if (_1 != null ? !_1.equals(one._1) : one._1 != null) {
	    return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	return _1 != null ? _1.hashCode() : 0;
    }

    @Override
    public String toString() {
	return "One{" +
	    "_1=" + _1 +
	    '}';
    }
}
