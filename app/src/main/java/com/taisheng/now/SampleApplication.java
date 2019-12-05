package com.taisheng.now;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by long on 17/4/4.
 */

public class SampleApplication extends TinkerApplication {
    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.taisheng.now.SampleAppLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
