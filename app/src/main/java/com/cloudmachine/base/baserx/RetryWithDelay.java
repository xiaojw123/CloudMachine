package com.cloudmachine.base.baserx;

import com.autonavi.rtbt.IFrameForRTBT;
import com.cloudmachine.chart.utils.AppLog;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by xiaojw on 2018/5/30.
 */

public class RetryWithDelay implements Func1<Observable<? extends  Throwable>,Observable<?>> {



private   int maxRetryCount;
private   int  retryDelayMills;
private int currentRetryCount;

    /** @param maxRetryCount 最大重试次数
     *  @param retryDelayMills 重试时间间隔
     */
    public  RetryWithDelay(int maxRetryCount,int retryDelayMills){
        this.maxRetryCount=maxRetryCount;
        this.retryDelayMills=retryDelayMills;
    }

    @Override
    public Observable<?> call(final Observable<? extends Throwable> observable) {

        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (throwable instanceof IOException){
                    if (currentRetryCount<=maxRetryCount){
                        currentRetryCount++;
                        int currentDelayMills=retryDelayMills+retryDelayMills*currentRetryCount;
                        AppLog.print("currentTryCount————"+currentRetryCount+"， Delay————"+currentDelayMills);
                        return Observable.timer(currentDelayMills, TimeUnit.MILLISECONDS);
                    }

                }
                return Observable.error(throwable);
            }
        });
    }
}
