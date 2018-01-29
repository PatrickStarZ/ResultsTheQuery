package com.example.patrickstar.resultsthequery.Utils;

/**
 * Created by PatrickStar on 2018/1/24 0024.
 */

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public final class HandlerUtil {

    private HandlerUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static class HandlerHolder extends Handler {
        WeakReference<OnReceiveMessageListener> mListenerWeakReference;

        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         *
         * @param listener 收到消息回调接口
         */
        public HandlerHolder(OnReceiveMessageListener listener) {
            mListenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListenerWeakReference != null && mListenerWeakReference.get() != null) {
                mListenerWeakReference.get().handlerMessage(msg);
            }
        }
    }

    /**
     * 收到消息回调接口
     */
    public interface OnReceiveMessageListener {
        void handlerMessage(Message msg);
    }
}
