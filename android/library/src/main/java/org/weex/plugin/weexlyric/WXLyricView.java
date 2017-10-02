package org.weex.plugin.weexlyric;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
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
        new ParserLyricTask(filePath);
    }

    @WXComponentProp(name = "playStatus")
    public void setPlayStatus(boolean playStatus) {

    }

    @WXComponentProp(name = "displayMode")
    public void setDisplayMode(LyricView.DisplayMode playMode) {
        getHostView().setDisplayMode(playMode);
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
                lyricView.setPlayingTime(0);
            }
        }
    }
}
