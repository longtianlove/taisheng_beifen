package com.taisheng.now.application;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by long on 17/4/4.
 */

public class SampleApplication extends TinkerApplication {
    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.taisheng.now.application.SampleAppLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

}
