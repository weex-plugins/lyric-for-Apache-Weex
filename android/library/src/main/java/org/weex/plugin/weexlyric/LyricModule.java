package org.weex.plugin.weexlyric;

import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.component.WXComponent;
import com.ttpod.lyric.Lyric;
import com.ttpod.lyric.LyricParser;
import com.ttpod.lyric.LyricView;

import java.util.Map;

@WeexModule(name = "lyric")
public class LyricModule extends WXModule {

    private LyricView mLyricView;

    //sync ret example
    @JSMethod
    public String syncRet(String param) {
        return param;
    }

    //async ret example
    @JSMethod
    public void asyncRet(String param, JSCallback callback) {
        callback.invoke(param);
    }

    @JSMethod
    public void bindView(String ref) {
        WXComponent lyricComponent = findComponent(ref);
        if (lyricComponent instanceof WXLyricView) {
            WXLyricView wxLyricView = (WXLyricView) lyricComponent;
            mLyricView = wxLyricView.getHostView();
        }
    }

    @JSMethod
    public void setLyricPath(String lyricPath, JSCallback callback) {
        if (findLyricView()) {
            mLyricView.setState(LyricView.STATE_SEARCHING);
            new ParserLyricTask(lyricPath, callback).execute();
        } else {
            notifyOperateFailed(callback, "找不到歌词组件");
        }
    }

    @Override
    public void onActivityDestroy() {
        mLyricView = null;
        super.onActivityDestroy();
    }

    private boolean findLyricView() {
        if (mLyricView == null) {
            WXComponent lyricComponent = findComponent("lyric");
            if (lyricComponent instanceof WXLyricView) {
                WXLyricView wxLyricView = (WXLyricView) lyricComponent;
                mLyricView = wxLyricView.getHostView();
            }
        }
        return mLyricView != null;
    }

    private void notifyOperateFailed(JSCallback callback, String msg) {
        Map<String, Object> params = new ArrayMap<>();
        params.put("code", 0);
        params.put("msg", msg);
        callback.invoke(params);
    }

    private void notifyOperateSuccess(JSCallback callback) {
        Map<String, Object> params = new ArrayMap<>();
        params.put("code", 1);
        params.put("msg", "ok");
        callback.invoke(params);
    }

    private class ParserLyricTask extends AsyncTask<String, Void, Lyric> {
        private JSCallback mJsCallback;
        private String mLyricPath;

        public ParserLyricTask(String lyricPath, JSCallback jsCallback) {
            mLyricPath = lyricPath;
            mJsCallback = jsCallback;
        }

        @Override
        protected Lyric doInBackground(String... params) {
            Lyric lyric = null;
            if (FileUtils.fileExists(mLyricPath)) {
                lyric = LyricParser.parse(mLyricPath);
            }
            return lyric;
        }

        @Override
        protected void onPostExecute(Lyric lyric) {
            super.onPostExecute(lyric);
            if (lyric == null) {
                String msg;
                if (FileUtils.fileExists(mLyricPath)) {
                    msg = "解析歌词发生错误";
                } else {
                    msg = "歌词文件不存在";
                }
                notifyOperateFailed(mJsCallback, msg);
            } else {
                if (mLyricView != null) {
                    mLyricView.setLyric(lyric);
                    mLyricView.setPlayingTime(0);
                }
                notifyOperateSuccess(mJsCallback);
            }
        }
    }
}