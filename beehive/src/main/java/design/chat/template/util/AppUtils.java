package design.chat.template.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * The class to provide utility methods
 * 
 * @author ATVApps
 * 
 */
public final class AppUtils {

    public static Bitmap getBitMapFromByteArray(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

}
