package cn.sharesdk.onekeyshare;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author LiuCongshan
 * @date 17-11-28
 */

public class ShareUtil {
    public static void share(String title, String content, String url, Bitmap logo, Context context) {
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setText(content);
        oks.setImageData(logo);
        oks.setUrl(url);
        oks.setSilent(true);
        oks.show(context);
    }
}
