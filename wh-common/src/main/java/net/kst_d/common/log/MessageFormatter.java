/*
 * Copyright (c) 2004-2007 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kst_d.common.log;

import java.util.HashMap;
import java.util.Map;

// contributors: lizongbo: proposed special treatment of array parameter values
// J�rn Huxhorn: pointed out double[] omission, suggested deep array copy

/**
 * Formats messages according to very simple substitution rules. Substitutions
 * can be made 1, 2 or more arguments.
 * <p/>
 * For example,
 * <p/>
 * <pre>
 * MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;)
 * </pre>
 * <p/>
 * will return the string "Hi there.".
 * <p/>
 * The {} pair is called the <em>formatting anchor</em>. It serves to
 * designate the location where arguments need to be substituted within the
 * message pattern.
 * <p/>
 * In case your message contains the '{' or the '}' character, you do not have
 * to do anything special unless the '}' character immediately follows '{'. For
 * example,
 * <p/>
 * <pre>
 * MessageFormatter.format(&quot;Set {1,2,3} is not equal to {}.&quot;, &quot;1,2&quot;);
 * </pre>
 * <p/>
 * will return the string "Set {1,2,3} is not equal to 1,2.".
 * <p/>
 * <p/>
 * If for whatever reason you need to place the string "{}" in the message
 * without its <em>formatting anchor</em> meaning, then you need to escape the
 * '{' character with '\', that is the backslash character. Only the '{'
 * character should be escaped. There is no need to escape the '}' character.
 * For example,
 * <p/>
 * <pre>
 * MessageFormatter.format(&quot;Set \\{} is not equal to {}.&quot;, &quot;1,2&quot;);
 * </pre>
 * <p/>
 * will return the string "Set {} is not equal to 1,2.".
 * <p/>
 * <p/>
 * The escaping behavior just described can be overridden by escaping the escape
 * character '\'. Calling
 * <p/>
 * <pre>
 * MessageFormatter.format(&quot;File name is C:\\\\{}.&quot;, &quot;file.zip&quot;);
 * </pre>
 * <p/>
 * will return the string "File name is C:\file.zip".
 * <p/>
 * <p/>
 * See {@link #format(String, Object)}, {@link #format(String, Object, Object)}
 * and {@link #arrayFormat(String, Object[])} methods for more details.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
@SuppressWarnings ("rawtypes")
final public class MessageFormatter {
    private static final char DELIM_START = '{';
    private static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    /**
     * Performs single argument substitution for the 'messagePattern' passed as
     * parameter.
     * <p/>
     * For example,
     * <p/>
     * <pre>
     * MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;);
     * </pre>
     * <p/>
     * will return the string "Hi there.".
     * <p/>
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param arg            The argument to be substituted in place of the formatting
     *                       anchor
     * @return The formatted message
     */
    public static String format(final String messagePattern, final Object arg) {
	return arrayFormat(messagePattern, new Object[] {arg});
    }

    /**
     * Performs a two argument substitution for the 'messagePattern' passed as
     * parameter.
     * <p/>
     * For example,
     * <p/>
     * <pre>
     * MessageFormatter.format(&quot;Hi {}. My name is {}.&quot;, &quot;Alice&quot;, &quot;Bob&quot;);
     * </pre>
     * <p/>
     * will return the string "Hi Alice. My name is Bob.".
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param arg1           The argument to be substituted in place of the first
     *                       formatting anchor
     * @param arg2           The argument to be substituted in place of the second
     *                       formatting anchor
     * @return The formatted message
     */
    public static String format(final String messagePattern, final Object arg1,	final Object arg2) {
	return arrayFormat(messagePattern, new Object[] {arg1, arg2});
    }

    /**
     * Same principle as the {@link #format(String, Object)} and
     * {@link #format(String, Object, Object)} methods except that any number of
     * arguments can be passed in an array.
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param argArray       An array of arguments to be substituted in place of
     *                       formatting anchors
     * @return The formatted message
     */
    public static String arrayFormat(final String messagePattern, final Object[] argArray) {
	if (messagePattern == null) {
	    return null;
	}
	if (argArray == null) {
	    return messagePattern;
	}
	int i = 0;
	int j;
	final StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

	for (int L = 0; L < argArray.length; L++) {

	    j = messagePattern.indexOf(DELIM_STR, i);

	    if (j == -1) {
		// no more variables
		if (i == 0) { // this is a simple string
		    return messagePattern;
		} else { // add the tail string which contains no variables and return
		    // the result.
		    sbuf.append(messagePattern.substring(i, messagePattern.length()));
		    return sbuf.toString();
		}
	    } else {
		if (isEscapedDelimeter(messagePattern, j)) {
		    if (isDoubleEscaped(messagePattern, j)) {
			// The escape character preceding the delimiter start is
			// itself escaped: "abc x:\\{}"
			// we have to consume one backward slash
			sbuf.append(messagePattern.substring(i, j - 1));
			deeplyAppendParameter(sbuf, argArray[L], new HashMap());
			i = j + 2;
		    } else {
			L--; // DELIM_START was escaped, thus should not be incremented
			sbuf.append(messagePattern.substring(i, j - 1));
			sbuf.append(DELIM_START);
			i = j + 1;
		    }
		} else {
		    // normal case
		    sbuf.append(messagePattern.substring(i, j));
		    deeplyAppendParameter(sbuf, argArray[L], new HashMap());
		    i = j + 2;
		}
	    }
	}
	// append the characters following the last {} pair.
	sbuf.append(messagePattern.substring(i, messagePattern.length()));
	return sbuf.toString();
    }

    private static boolean isEscapedDelimeter(final String messagePattern, final int delimeterStartIndex) {
	if (delimeterStartIndex == 0) {
	    return false;
	}
	final char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
	return potentialEscape == ESCAPE_CHAR;
    }

    private static boolean isDoubleEscaped(final String messagePattern, final int delimeterStartIndex) {
	return delimeterStartIndex >= 2
	    && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR;
    }

    // special treatment of array values was suggested by 'lizongbo'
    private static void deeplyAppendParameter(final StringBuffer sbuf, final Object obj, final Map seenMap) {
	if (obj == null) {
	    sbuf.append("null");
	    return;
	}
	if (obj.getClass().isArray()) {
	    // check for primitive array types because they
	    // unfortunately cannot be cast to Object[]
	    if (obj instanceof boolean[]) {
		booleanArrayAppend(sbuf, (boolean[]) obj);
	    } else if (obj instanceof byte[]) {
		byteArrayAppend(sbuf, (byte[]) obj);
	    } else if (obj instanceof char[]) {
		charArrayAppend(sbuf, (char[]) obj);
	    } else if (obj instanceof short[]) {
		shortArrayAppend(sbuf, (short[]) obj);
	    } else if (obj instanceof int[]) {
		intArrayAppend(sbuf, (int[]) obj);
	    } else if (obj instanceof long[]) {
		longArrayAppend(sbuf, (long[]) obj);
	    } else if (obj instanceof float[]) {
		floatArrayAppend(sbuf, (float[]) obj);
	    } else if (obj instanceof double[]) {
		doubleArrayAppend(sbuf, (double[]) obj);
	    } else {
		objectArrayAppend(sbuf, (Object[]) obj, seenMap);
	    }
	} else {
	    safeObjectAppend(sbuf, obj);
	}
    }

    private static void safeObjectAppend(final StringBuffer sbuf, final Object obj) {
	try {
	    final String oAsString = obj.toString();
	    sbuf.append(oAsString);
	} catch (Throwable t) {
	    System.err.println("SLF4J: Failed toString() invocation on an object of type [" + obj.getClass().getName() + "]");
	    t.printStackTrace();
	    sbuf.append("[FAILED toString()]");
	}

    }

    @SuppressWarnings ("unchecked")
    private static void objectArrayAppend(final StringBuffer sbuf, final Object[] data, final Map seenMap) {
	sbuf.append('[');
	if (seenMap.containsKey(data)) {
	    sbuf.append("...");
	} else {
	    seenMap.put(data, null);
	    final int len = data.length;
	    for (int i = 0; i < len; i++) {
		deeplyAppendParameter(sbuf, data[i], seenMap);
		if (i != len - 1) {
		    sbuf.append(", ");
		}
	    }
	    // allow repeats in siblings
	    seenMap.remove(data);
	}
	sbuf.append(']');
    }

    private static void booleanArrayAppend(final StringBuffer sbuf, final boolean[] data) {
	sbuf.append('[');
	final int len = data.length;
	for (int i = 0; i < len; i++) {
	    sbuf.append(data[i]);
	    if (i != len - 1) {
		sbuf.append(", ");
	    }
	}
	sbuf.append(']');
    }

    private static void byteArrayAppend(final StringBuffer sbuf, final byte[] data) {
	sbuf.append('[');
	final int len = data.length;
	for (int i = 0; i < len; i++) {
	    sbuf.append(data[i]);
	    if (i != len - 1) {
		sbuf.append(", ");
	    }
	}
	sbuf.append(']');
    }

    private static void charArrayAppend(final StringBuffer sbuf, final char[] data) {
	sbuf.append('[');
	final int len = data.length;
	for (int i = 0; i < len; i++) {
	    sbuf.append(data[i]);
	    if (i != len - 1) {
		sbuf.append(", ");
	    }
	}
	sbuf.append(']');
    }

    private static void shortArrayAppend(final StringBuffer sbuf, final short[] data) {
	sbuf.append('[');
	final int len = data.length;
	for (int i = 0; i < len; i++) {
	    sbuf.append(data[i]);
	    if (i != len - 1) {
		sbuf.append(", ");
	    }
	}
	sbuf.append(']');
    }

    private static void intArrayAppend(final StringBuffer sbuf, final int[] data) {
	sbuf.append('[');
	final int len = data.length;
	for (int i = 0; i < len; i++) {
	    sbuf.append(data[i]);
	    if (i != len - 1) {
		sbuf.append(", ");
	    }
	}
	sbuf.append(']');
    }

    private static void longArrayAppend(final StringBuffer sbuf, final long[] data) {
	sbuf.append('[');
	final int len = data.length;
	for (int i = 0; i < len; i++) {
	    sbuf.append(data[i]);
	    if (i != len - 1) {
		sbuf.append(", ");
	    }
	}
	sbuf.append(']');
    }

    private static void floatArrayAppend(final StringBuffer sbuf, final float[] data) {
	sbuf.append('[');
	final int len = data.length;
	for (int i = 0; i < len; i++) {
	    sbuf.append(data[i]);
	    if (i != len - 1) {
		sbuf.append(", ");
	    }
	}
	sbuf.append(']');
    }

    private static void doubleArrayAppend(final StringBuffer sbuf, final double[] data) {
	sbuf.append('[');
	final int len = data.length;
	for (int i = 0; i < len; i++) {
	    sbuf.append(data[i]);
	    if (i != len - 1) {
		sbuf.append(", ");
	    }
	}
	sbuf.append(']');
    }
}
