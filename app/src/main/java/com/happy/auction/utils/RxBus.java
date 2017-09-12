package com.happy.auction.utils;

/**
 * Created by LiuCongshan on 17-9-7.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class RxBus {
    private final Subject<Object> bus;
    private Map<Object, List<Disposable>> subscriptions;

    private RxBus() {
        bus = PublishSubject.create();
    }

    public static RxBus getDefault() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 发送事件
     *
     * @param event 事件对象
     */
    public void post(Object event) {
        if (bus.hasObservers()) {
            bus.onNext(event);
        }
    }

    /**
     * 添加订阅协议
     *
     * @param subscriber   订阅者
     * @param subscription 订阅协议
     */
    private synchronized void add(Object subscriber, Disposable subscription) {
        if (subscriptions == null) {
            subscriptions = new HashMap<>();
        }

        List<Disposable> list = subscriptions.get(subscriber);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(subscription);
        subscriptions.put(subscriber, list);
    }

    /**
     * 订阅事件
     *
     * @param subscriber 订阅者
     * @param eventType  事件对象
     * @param callback   事件回调
     * @param <T>        事件类型
     */
    public <T> void subscribe(Object subscriber, Class<T> eventType, Consumer<T> callback) {
        Disposable subscription = bus.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .ofType(eventType)
                .subscribe(callback);
        add(subscriber, subscription);
    }

    /**
     * 订阅事件
     *
     * @param subscriber 订阅者
     * @param callback   事件回调
     * @param <T>        事件类型
     */
    public <T> void subscribe(Object subscriber, Consumer<T> callback) {
        Class<T> eventType = RawType.getRawType(callback);
        Disposable subscription = bus.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .ofType(eventType)
                .subscribe(callback);
        add(subscriber, subscription);
    }

    /**
     * 取消事件订阅
     *
     * @param subscriber 订阅者
     */
    public synchronized void unsubscribe(Object subscriber) {
        if (subscriptions == null) return;

        List<Disposable> list = subscriptions.get(subscriber);
        if (list == null || list.isEmpty()) return;

        for (Disposable subscription : list) {
            subscription.dispose();
        }
        list.clear();
        subscriptions.remove(subscriber);
    }

    /**
     * 取消所有订阅
     */
    public void unsubscribeAll() {
        Set<Map.Entry<Object, List<Disposable>>> set = subscriptions.entrySet();
        for (Map.Entry<Object, List<Disposable>> entry : set) {
            List<Disposable> list = entry.getValue();
            for (Disposable subscription : list) {
                subscription.dispose();
            }
            list.clear();
        }
        subscriptions = null;
    }

    private static class SingletonHolder {
        static volatile RxBus INSTANCE = new RxBus();
    }
}