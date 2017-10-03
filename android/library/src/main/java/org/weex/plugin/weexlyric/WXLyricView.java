package org.weex.plugin.weexlyric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.ttpod.lyric.Lyric;
import com.ttpod.lyric.LyricParser;
import com.ttpod.lyric.LyricView;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 2017/9/22
 */
@Component(lazyload = false)
public class WXLyricView extends WXComponent<LyricView> {

    public WXLyricView(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
    }

    @Override
    protected LyricView initComponentHostView(@NonNull Context context) {
        return new LyricView(context);
    }

    @Override
    protected boolean setProperty(String key, Object param) {
        return super.setProperty(key, param);
    }

    @WXComponentProp(name = "lyricFile")
    public void setLyricFile(String filePath) {
        new ParserLyricTask(filePath).execute();
    }

    @WXComponentProp(name = "playStatus")
    public void setPlayStatus(boolean playStatus) {
        if (playStatus) {
            startFlushView();
        } else {
            stopFlushView();
        }
    }

    @WXComponentProp(name = "playTime")
    public void updatePlayTime(int playingTime) {
        mCurPlayingTime = playingTime;
        boolean playFlag = mPlayFlag;
        stopFlushView();
        getHostView().setPlayingTime(playingTime);
        if (playFlag) {
            startFlushView();
        }
    }

    @WXComponentProp(name = "highlightColor")
    public void setHighlightColor(String highlightColor) {
        try {
            int intColor = Color.parseColor(highlightColor);
            getHostView().setColorHighlight(intColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @WXComponentProp(name = "normalColor")
    public void setNormalColor(String normalColor) {
        try {
            int intColor = Color.parseColor(normalColor);
            getHostView().setColorNormal(intColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @WXComponentProp(name = "kalaOK")
    public void setKalaOK(boolean kalaOK) {
        getHostView().setKalaOK(kalaOK);
    }

    @WXComponentProp(name = "textAlign")
    public void setTextAlign(Paint.Align align) {
        getHostView().setAlign(align);
    }

    @WXComponentProp(name = "slowScroll")
    public void setSlowScroll(boolean slowScroll) {
        getHostView().setSlowScroll(slowScroll);
    }

    @WXComponentProp(name = "displayMode")
    public void setDisplayMode(LyricView.DisplayMode playMode) {
        getHostView().setDisplayMode(playMode);
    }

    private class ParserLyricTask extends AsyncTask<String, Void, Lyric> {
        private String mLyricPath;

        public ParserLyricTask(String lyricPath) {
            mLyricPath = lyricPath;
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
            LyricView lyricView = getHostView();
            if (lyricView == null) {
                return;
            }
            lyricView.setLyric(lyric);
            if (lyric == null) {
                String msg;
                if (FileUtils.fileExists(mLyricPath)) {
                    msg = "解析歌词发生错误";
                } else {
                    msg = "歌词文件不存在";
                }
            } else {
                lyricView.setPlayingTime(mCurPlayingTime);
            }
        }
    }

    private void startFlushView() {
        mPlayFlag = true;
        mHandler.sendEmptyMessageDelayed(0, FLUSH_INTERVAL);
    }

    private void stopFlushView() {
        mPlayFlag = false;
        mHandler.removeCallbacksAndMessages(null);
    }

    private long mCurPlayingTime = 0;  //默认跳到xx秒位置
    private static final long MAX_DURATION_TIME = 8 * 60 * 1000;  //假设最大6分钟位置

    private static final int FLUSH_INTERVAL = 50;

    private boolean mPlayFlag;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final LyricView lyricView = getHostView();
            if (lyricView == null) {
                return;
            }
            mCurPlayingTime += FLUSH_INTERVAL;
            lyricView.setPlayingTime(mCurPlayingTime);
            if (mCurPlayingTime < MAX_DURATION_TIME) {
                mHandler.sendEmptyMessageDelayed(0, FLUSH_INTERVAL);
            } else {
                mPlayFlag = false;
            }
        }
    };
}
