package com.cloudmachine.base.baserx;

import com.cloudmachine.base.bean.BaseRespose;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * des:对服务器返回数据成功和失败处理
 * Created by xsf
 * on 2016.09.9:59
 */

/**************使用例子******************/
/*_apiService.login(mobile, verifyCode)
        .compose(RxSchedulersHelper.io_main())
        .compose(RxResultHelper.handleResult())
        .//省略*/

public class RxHelper {
    /**
     * 对服务器返回数据进行预处理(正常情况只需要result)
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseRespose<T>, T> handleResult() {
        return new Observable.Transformer<BaseRespose<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseRespose<T>> tObservable) {
                return tObservable.flatMap(new Func1<BaseRespose<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseRespose<T> result) {
                       // LogUtils.logd("result from api : " + result);
                        if (result.success()) {
                            return createData(result.result);
                        } else {
                            return Observable.error(new ServerException(result.getMessage()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

    }


    /**
     * 对带分页信息的服务器返回数据进行预处理（拿到大父类）
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseRespose<T>, BaseRespose<T>> handleBaseResult() {
        return new Observable.Transformer<BaseRespose<T>, BaseRespose<T>>() {
            @Override
            public Observable<BaseRespose<T>> call(Observable<BaseRespose<T>> baseResposeObservable) {
                return baseResposeObservable.flatMap(new Func1<BaseRespose<T>, Observable<BaseRespose<T>>>() {
                    @Override
                    public Observable<BaseRespose<T>> call(BaseRespose<T> tBaseRespose) {
                        if (tBaseRespose.code == 800) {
                            return createData(tBaseRespose);
                        } else {
                            return Observable.error(new ServerException(tBaseRespose.getMessage()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 对结果类型的数据进行预处理(只需要拿到message信息的)
     * @return
     */
    public static Observable.Transformer<BaseRespose, String> handleBooleanResult() {
        return new Observable.Transformer<BaseRespose, String>() {
            @Override
            public Observable<String> call(Observable<BaseRespose> baseResposeObservable) {
                return baseResposeObservable.flatMap(new Func1<BaseRespose, Observable<String>>() {
                    @Override
                    public Observable<String> call(BaseRespose baseRespose) {
                        if (baseRespose.code == 800) {
                            return createData(baseRespose.message);
                        } else {
                            return Observable.error(new ServerException(baseRespose.message));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

    }


    private static <T> Observable<T> createBaseData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }



}
