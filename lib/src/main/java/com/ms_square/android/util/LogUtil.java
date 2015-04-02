package com.ms_square.android.util;

import android.util.Log;

import java.util.Locale;

public class LogUtil {

    private static String LOG_PREFIX = "LogUtil_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }
        return LOG_PREFIX + str;
    }

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void setLogPrefix(String prefix) {
        LOG_PREFIX = prefix;
    }

    public static void LOGV(final String tag, String format, Object... args) {
        Log.v(tag, buildMessage(format, args));
    }

    public static void LOGV(final String tag, Throwable tr, String format, Object... args) {
        Log.v(tag, buildMessage(format, args), tr);
    }

    public static void LOGD(final String tag, String format, Object... args) {
        Log.d(tag, buildMessage(format, args));
    }

    public static void LOGD(final String tag, Throwable tr, String format, Object... args) {
        Log.d(tag, buildMessage(format, args), tr);
    }

    public static void LOGI(final String tag, String format, Object... args) {
        Log.i(tag, buildMessage(format, args));
    }

    public static void LOGI(final String tag, Throwable tr, String format, Object... args) {
        Log.i(tag, buildMessage(format, args), tr);
    }

    public static void LOGW(final String tag, String format, Object... args) {
        Log.w(tag, buildMessage(format, args));
    }

    public static void LOGW(final String tag, Throwable tr, String format, Object... args) {
        Log.w(tag, buildMessage(format, args), tr);
    }

    public static void LOGE(final String tag, String format, Object... args) {
        Log.e(tag, buildMessage(format, args));
    }

    public static void LOGE(final String tag, Throwable tr, String format, Object... args) {
        Log.e(tag, buildMessage(format, args), tr);
    }

    public static void LOGWTF(final String tag, String format, Object... args) {
        Log.wtf(tag, buildMessage(format, args));
    }

    public static void LOGWTF(final String tag, Throwable tr, String format, Object... args) {
        Log.wtf(tag, buildMessage(format, args), tr);
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of LogUtil.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtil.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);
                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg);
    }

    private LogUtil() {
        new AssertionError();
    }
}