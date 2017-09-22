package org.weex.plugin.weexlyric;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

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

    @WXComponentProp(name = "filePath")
    public void setLyricPath(String lyricPath) {
        getHostView().setState(LyricView.STATE_SEARCHING);
        new ParserLyricTask().execute(lyricPath);
    }

    @Override
    protected boolean setProperty(String key, Object param) {
        return super.setProperty(key, param);
    }

    private class ParserLyricTask extends AsyncTask<String, Void, Lyric> {
        private String mLyricPath;

        @Override
        protected Lyric doInBackground(String... params) {
            mLyricPath = params[0];
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
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
            } else {
                LyricView lyricView = getHostView();
                if (lyricView != null) {
                    lyricView.setLyric(lyric);
                    lyricView.setPlayingTime(0);
                }
            }
        }
    }
}
