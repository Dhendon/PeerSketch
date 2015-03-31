package gatech.adam.peersketch;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by hendon on 3/23/15.
 * This class is a Singleton for network connectivity,
 * used right now for logging into the ES server.
 */
public class LoginController {

    private static LoginController mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private LoginController(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized LoginController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LoginController(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
