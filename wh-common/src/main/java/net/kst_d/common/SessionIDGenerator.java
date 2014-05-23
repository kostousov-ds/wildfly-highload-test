package net.kst_d.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


/**
 * Класс прадназначен для генерации псевдослучайной строки,
 * состоящей из цифр и чисел из заданного словаря.
 */
public final class SessionIDGenerator {
    /**
     * Длина строки по умолчанию.
     */
    public static final int DEFAULT_STR_LEN = 20;
    /**
     * Словарь возможных символов в строке.
     */
    private static final char[] numbers = {
	'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    };
    private static final char[] symbols = {
	'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
	'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd',
	'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'
//    ,
//    'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D',
//    'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'
    };

    private static final char[] wide_symbols = {
	'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
	'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd',
	'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'
	,
	'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D',
	'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'
    };

    private static final String DEV_RANDOM = "/dev/urandom";

    /**
     * Метод генерирует псевдослучайную строку, состоящую из цифр и чисел длиной {@value #DEFAULT_STR_LEN} символов.
     *
     * @return псевдослучайная строка
     */
    public static String getNewSessionID() {
	return getNewRandomString(DEFAULT_STR_LEN);
    }

    /**
     * Для генерации случайной строки используется файл /dev/urandom, в случае если этого файла нет
     * (например на Windows машинах) при каждом вызове метода создается экземпляр {@link Random}.
     *
     * @param len длина строки
     * @return случайная строка заданной длины
     */
    public static String getNewRandomString(final int len) {
	return getRandom(len, symbols);
    }

    public static String getNewRandomWideString(final int len) {
	return getRandom(len, wide_symbols);
    }

    public static String getNewRandomNumberString(final int len) {
	return getRandom(len, numbers);
    }

    public static int getNewRandomInt() {
	final byte[] b = getRandomBytes(4);
	return b[0] << 24 | b[1] << 16 | b[2] << 8 | b[3];
    }

    public static int getNewRandomUnsignedInt() {
	final byte[] b = getRandomBytes(4);
	return (b[0] << 24 | b[1] << 16 | b[2] << 8 | b[3]) & 0x7fffffff;
    }

    public static int getNewRandomInt(final int max) {
	return getNewRandomInt() % max;
    }

    public static int getNewRandomUnsignedInt(final int max) {
	return getNewRandomUnsignedInt() % max;
    }

    public static String getRandom(final int len, final char[] dict) {
	final char id[] = new char[len];
	final byte[] tmpArray = getRandomBytes(len);

	for (int j = 0; j < id.length; j++) {
	    id[j] = dict[((int) tmpArray[j] & 0xff) % dict.length];
	}

	return new String(id);
    }

    public static byte[] getRandomBytes(final int len) {
	final byte tmpArray[] = new byte[len];

	try {
	    //Будем надеяться, что это Unix-like система
	    InputStream inRnd = null;
	    try {
		inRnd = new FileInputStream(DEV_RANDOM);
		inRnd.read(tmpArray);
	    } finally {
		if (inRnd != null) {
		    inRnd.close();
		}
	    }
	} catch (IOException e) {
	    //Как известно, в некоторых системах /dev/random отсутствует
	    //Для поддержки этих малоизвестных систем приходится использовать
	    //этот убогий код
	    final Random rnd = new Random(System.currentTimeMillis());
	    rnd.nextBytes(tmpArray);
	}
	return tmpArray;
    }
}
