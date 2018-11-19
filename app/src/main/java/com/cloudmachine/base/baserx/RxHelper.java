package com.cloudmachine.base.baserx;

import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.NullStringToEmptyAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

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


    public static <M> Observable.Transformer<JsonObject, M> handleCommonResult(final Class<M> cls) {
        return new Observable.Transformer<JsonObject, M>() {
            @Override
            public Observable<M> call(Observable<JsonObject> observable) {
                return observable.flatMap(new Func1<JsonObject, Observable<M>>() {
                    @Override
                    public Observable<M> call(JsonObject s) {
                        Gson gson = new GsonBuilder().create();
                        BaseRespose response = gson.fromJson(s, new TypeToken<BaseRespose>() {
                        }.getType());
                        if (response.isSuccess()) { //结果正确
                            if (cls != null) {
                                JsonElement resultJE = s.get(Constants.RESULT);
                                if (resultJE != null && !resultJE.isJsonNull()) {
                                    try {
                                        M m;
                                        if (cls == String.class) {
                                            m = (M) resultJE.getAsString();
                                        } else {
                                            JsonObject resultJobj = resultJE.getAsJsonObject();
                                            if (cls == JsonObject.class) {
                                                m = (M) resultJobj;
                                            } else {
                                                m = gson.fromJson(resultJobj, cls);
                                            }
                                        }
                                        return createData(m);
                                    } catch (Exception e) {
                                        return createData(null);
                                    }
                                } else {
                                    return createData(null);
                                }
                            } else {
                                return createData(null);
                            }
                        } else {
                            if (Constants.checkToken(response)) {
                                //如果发生错误则解析到BaseError中，最终由onError处理
                                return Observable.error(new ServerException(response.getDevMsg()));
                            } else {
                                //如果发生错误则解析到BaseError中，最终由onError处理
                                return Observable.error(new ServerException(response.getMessage()));
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

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
                        if (result.success()) {
                            return createData(result.getResult());
                        } else {
                            if (Constants.checkToken(result)) {
                                return Observable.error(new ServerException(result.getDevMsg()));
                            } else {
                                return Observable.error(new ServerException(result.getMessage()));
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

    }


    /**
     * 对带分页信息的服务器返回数据进行预处理（拿到大父类）
     *
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
                        if (tBaseRespose.getCode() == 800) {
                            return createData(tBaseRespose);
                        } else {
                            if (Constants.checkToken(tBaseRespose)) {
                                return Observable.error(new ServerException(tBaseRespose.getDevMsg()));
                            } else {
                                return Observable.error(new ServerException(tBaseRespose.getMessage()));
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 对结果类型的数据进行预处理(只需要拿到message信息的)
     *
     * @return
     */
    public static Observable.Transformer<BaseRespose, String> handleBooleanResult() {
        return new Observable.Transformer<BaseRespose, String>() {
            @Override
            public Observable<String> call(Observable<BaseRespose> baseResposeObservable) {
                return baseResposeObservable.flatMap(new Func1<BaseRespose, Observable<String>>() {
                    @Override
                    public Observable<String> call(BaseRespose baseRespose) {
                        if (baseRespose.getCode() == 800) {
                            return createData(baseRespose.getMessage());
                        } else {
                            if (Constants.checkToken(baseRespose)) {
                                return Observable.error(new ServerException(baseRespose.getDevMsg()));
                            } else {
                                return Observable.error(new ServerException(baseRespose.getMessage()));
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static Observable.Transformer<BaseRespose, String> handleSwitchResult() {
        return new Observable.Transformer<BaseRespose, String>() {
            @Override
            public Observable<String> call(Observable<BaseRespose> baseResposeObservable) {
                return baseResposeObservable.flatMap(new Func1<BaseRespose, Observable<String>>() {
                    @Override
                    public Observable<String> call(BaseRespose baseRespose) {
                        if (baseRespose.getCode() == 800) {
                            return createData(baseRespose.getMessage());
                        } else if (baseRespose.getCode() == 16305) {
                            return Observable.error(new ServerException(String.valueOf(baseRespose.getCode())));
                        } else {
                            return Observable.error(new ServerException(baseRespose.getMessage()));
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
