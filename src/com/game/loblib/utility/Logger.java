package com.game.loblib.utility;

import android.util.Log;

public class Logger {

	protected static int _logLevel;
	
	protected static final int NONE = 0;
	protected static final int ERROR = 1;
	protected static final int WARN = 2;
	protected static final int INFO = 3;
	protected static final int DEBUG = 4;
	protected static final int VERBOSE = 5;
	protected static final int ALL = Integer.MAX_VALUE;
	
	public static void init() {
		_logLevel = WARN;
	}
	
	public static void e(String tag, String msg) {
		e(tag, msg, null);
	}
	
	public static void e(String tag, String msg, Throwable tr) {	
		if (_logLevel < ERROR) return;
		if (tr != null)
			Log.e(tag, msg, tr);
		else
			Log.e(tag, msg);
	}
	
	public static void e(StringBuffer tag, String msg) {	
		e(tag, msg, null);
	}
	
	public static void e(StringBuffer tag, String msg, Throwable tr) {	
		if (_logLevel < ERROR) return;
		if (tr != null)
			Log.e(tag.toString(), msg, tr);
		else
			Log.e(tag.toString(), msg);
	}
	
	public static void w(String tag, String msg) {
		w(tag, msg, null);
	}
	
	public static void w(String tag, String msg, Throwable tr) {	
		if (_logLevel < WARN) return;
		if (tr != null)
			Log.w(tag, msg, tr);
		else
			Log.w(tag, msg);
	}
	
	public static void w(StringBuffer tag, String msg) {
		w(tag, msg, null);
	}
	
	public static void w(StringBuffer tag, String msg, Throwable tr) {
		if (_logLevel < WARN) return;
		if (tr != null)
			Log.e(tag.toString(), msg, tr);
		else
			Log.e(tag.toString(), msg);
	}
	
	public static void i(String tag, String msg) {
		i(tag, msg, null);
	}
	
	public static void i(String tag, String msg, Throwable tr) {	
		if (_logLevel < INFO) return;
		if (tr != null)
			Log.i(tag, msg, tr);
		else
			Log.i(tag, msg);
	}
	
	public static void i(StringBuffer tag, String msg) {
		i(tag, msg, null);
	}
	
	public static void i(StringBuffer tag, String msg, Throwable tr) {
		if (_logLevel < INFO) return;
		if (tr != null)
			Log.i(tag.toString(), msg, tr);
		else
			Log.i(tag.toString(), msg);
	}
	
	public static void d(String tag, String msg) {
		d(tag, msg, null);
	}
	
	public static void d(String tag, String msg, Throwable tr) {	
		if (_logLevel < DEBUG) return;
		if (tr != null)
			Log.d(tag, msg, tr);
		else
			Log.d(tag, msg);
	}
	
	public static void d(StringBuffer tag, String msg) {
		d(tag, msg, null);
	}
	
	public static void d(StringBuffer tag, String msg, Throwable tr) {
		if (_logLevel < DEBUG) return;
		if (tr != null)
			Log.d(tag.toString(), msg, tr);
		else
			Log.d(tag.toString(), msg);
	}
	
	public static void v(String tag, String msg) {
		v(tag, msg, null);
	}
	
	public static void v(String tag, String msg, Throwable tr) {	
		if (_logLevel < VERBOSE) return;
		if (tr != null)
			Log.v(tag, msg, tr);
		else
			Log.v(tag, msg);
	}
	
	public static void v(StringBuffer tag, String msg) {
		v(tag, msg, null);
	}
	
	public static void v(StringBuffer tag, String msg, Throwable tr) {
		if (_logLevel < VERBOSE) return;
		if (tr != null)
			Log.v(tag.toString(), msg, tr);
		else
			Log.v(tag.toString(), msg);
	}
}
