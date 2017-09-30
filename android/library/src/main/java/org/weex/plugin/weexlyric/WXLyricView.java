package org.weex.plugin.weexlyric;

import android.content.Context;
import android.support.annotation.NonNull;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
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

}
