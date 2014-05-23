package net.kst_d.common.log;

import net.kst_d.common.TicSessionID;

public interface MethodLogger {

    TicSessionID ticSessionID();

    /**
     * Определяет имя класса от которого производится голирование
     * @return строка с именем класса
     */
    String getName();

    /**
     * Возвращает родительский логгер
     * @return Logger
     */
    Logger parent();

    /**
     * Возвращает имя метода для которого будет произведено логирование
     * @return имя метода
     */
    String method();

    /**
     * Вызывать в конце какого-либо. метода. В журнале будет зафиксирована строка вида "...method: stop"
     */
    void exiting();

    /**
     * Вызывать в конце какого-либо. метода. В журнале будет зафиксирована строка вида "...method: stop, return..."
     * @param result результат работы метода. Будет записан в журнал после слова "return" - см выше.
     */
    void exiting(Object result);

    /**
     * Проверяет - будут ли журналироваться TRACE-ы
     * @return true - если будут
     */
    boolean isTraceEnabled();

    /**
     * Записывает в журнал сообщение уровня TRACE
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void trace(String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня TRACE
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void trace(String format, Throwable thr, Object... argArray);

    /**
     * Проверяет - будут ли журналироваться DEBUG-и
     * @return true - если будут
     */
    boolean isDebugEnabled();

    /**
     * Записывает в журнал сообщение уровня DEBUG
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void debug(String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня DEBUG
     * @param msg строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void debug(String msg, Throwable thr, Object... argArray);

    /**
     * Проверяет - будут ли журналироваться INFO
     * @return true - если будут
     */
    boolean isInfoEnabled();

    /**
     * Записывает в журнал сообщение уровня INFO
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void info(String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня INFO
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void info(String format, Throwable thr, Object... argArray);

    /**
     * Проверяет - будут ли журналироваться WARN-нинги
     * @return true - если будут
     */
    boolean isWarnEnabled();

    /**
     * Записывает в журнал сообщение уровня WARN
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void warn(String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня WARN
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void warn(String format, Throwable thr, Object... argArray);

    /**
     * Проверяет - будут ли журналироваться ERROR-ы
     * @return true - если будут
     */
    boolean isErrorEnabled();

    /**
     * Записывает в журнал сообщение уровня ERROR
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void error(String format, Object... argArray);

    /**
     * Записывает в журнал сообщение уровня ERROR
     * @param format строка сообщения с "вкраплениями" вида {}
     * @param thr Throwable
     * @param argArray данные для включения в строку сообщения (через {} в строке)
     */
    void error(String format, Throwable thr, Object... argArray);

}
