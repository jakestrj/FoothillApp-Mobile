package com.novallc.foothillappmobile.activity.util;

import java.lang.ref.WeakReference;

public abstract class WeakRunnable<T> implements Runnable {
    protected WeakReference<T> weakRef;

    public WeakRunnable(T reference) {
        this.weakRef = new WeakReference(reference);
    }
}
