package com.android.zd112.utils;

import android.os.Handler;
import android.os.Looper;

public final class ErrorLog {
    private static ExeceptionHandler mExeceptionHandler;
    private static Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;
    private static boolean mInstalled = false;

    public static synchronized  void install(ExeceptionHandler execeptionHandler){
        if(mInstalled){
            return;
        }
        mInstalled = true;
        mExeceptionHandler = execeptionHandler;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Looper.loop();
                    }catch (Throwable throwable){
                        if(throwable instanceof RunException){
                            return;
                        }
                        if(mExeceptionHandler != null){
                            mExeceptionHandler.handlerException(Looper.getMainLooper().getThread(),throwable);
                        }
                    }
                }
            }
        });
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if(mExeceptionHandler != null){
                    mExeceptionHandler.handlerException(thread,throwable);
                }
            }
        });
    }

    public static synchronized void uninstall(){
        if(!mInstalled){
            return;
        }
        mInstalled = false;
        mExeceptionHandler = null;
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                throw new RunException("exit the main...");
            }
        });
    }

    public interface ExeceptionHandler{
        void handlerException(Thread thread, Throwable throwable);
    }

    static final class RunException extends RuntimeException {
        public RunException(String msg){
            super(msg);
        }
    }
}
