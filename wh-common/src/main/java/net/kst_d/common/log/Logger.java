package net.kst_d.common.log;

import net.kst_d.common.TicSessionID;

public interface Logger {
    /**
     * Определяет имя класса от которого производится голирование
     * @return строка с именем класса
     */
    String getName();

    /**
     * Вызывать в начале какого-либо. метода. Вывод в журнал не осуществляет
     * @param ticSessionID идентификатор сессии
     * @param method название метода
     * @return MethodLogger
     */
    MethodLogger silentEnter(final TicSessionID ticSessionID, final String method);
    /**
     * Вызывать в начале какого-либо. метода. В журнале будет зафиксирована строка вида "...method: start"
     * @param ticSessionID идентификатор сессии
     * @param method название метода
     * @return MethodLogger
     */
    MethodLogger entering(TicSessionID ticSessionID, String method);

    /**
     * Вызывать в конце какого-либо. метода. В журнале будет зафиксирована строка вида "...method: stop"
     * @param ticSessionID идентификатор сессии
     * @param method название метода
     */
    void exiting(TicSessionID ticSessionID, String method);

    /**
     * Вызывать в конце какого-либо. метода. В журнале будет зафиксирована строка вида "...method: stop, return..."
     * @param ticSessionID идентификатор сессии
     * @param method название метода
     * @param result результат работы метода. Будет записан в журнал после слова "return" - см выше.
     */
    void exiting(TicSessionID ticSessionID, String method, Object result);

    /**
     * Проверяет - будут ли журналироваться TRACE-ы
     * @return true - если будут
     */
    boolean isTraceEnabled();

    /**
     * Записывает в журнал сообщение уровня TRACE
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void trace(TicSessionID ticSessionID, String method, String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня TRACE
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void trace(TicSessionID ticSessionID, String method, String format, Throwable thr, Object... argArray);

    /**
     * Проверяет - будут ли журналироваться DEBUG-и
     * @return true - если будут
     */
    boolean isDebugEnabled();

    /**
     * Записывает в журнал сообщение уровня DEBUG
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void debug(TicSessionID ticSessionID, String method, String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня DEBUG
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param msg строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void debug(TicSessionID ticSessionID, String method, String msg, Throwable thr, Object... argArray);

    /**
     * Проверяет - будут ли журналироваться INFO
     * @return true - если будут
     */
    boolean isInfoEnabled();

    /**
     * Записывает в журнал сообщение уровня INFO
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void info(TicSessionID ticSessionID, String method, String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня INFO
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void info(TicSessionID ticSessionID, String method, String format, Throwable thr, Object... argArray);

    /**
     * Проверяет - будут ли журналироваться WARN-нинги
     * @return true - если будут
     */
    boolean isWarnEnabled();

    /**
     * Записывает в журнал сообщение уровня WARN
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void warn(TicSessionID ticSessionID, String method, String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня WARN
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void warn(TicSessionID ticSessionID, String method, String format, Throwable thr, Object... argArray);

    /**
     * Проверяет - будут ли журналироваться ERROR-ы
     * @return true - если будут
     */
    boolean isErrorEnabled();

    /**
     * Записывает в журнал сообщение уровня ERROR
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void error(TicSessionID ticSessionID, String method, String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня ERROR
     * @param ticSessionID идентификатор сессии
     * @param method метод в котором инициируется сообщение
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void error(TicSessionID ticSessionID, String method, String format, Throwable thr, Object... argArray);
}
