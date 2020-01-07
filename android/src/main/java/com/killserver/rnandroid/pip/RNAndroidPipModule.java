package com.killserver.rnandroid.pip;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableNativeArray;

import java.util.Map;

import android.os.Build;
import com.facebook.react.bridge.Promise;
import android.util.Log;

public class RNAndroidModule extends ReactContextBaseJavaModule {

  private static final String MODULE_NAME = "RNAndroidModule";

  public RNAndroidModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return MODULE_NAME;
  }

  @ReactMethod
  public boolean isPictureInPictureSupported() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
  }

  public void enterPictureInPictureMode() {
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      throw new IllegalStateException("Picture-in-Picture not supported");
    }
    Activity currentActivity = getCurrentActivity();
    if(currentActivity == null) {
      throw new IllegalStateException("No current Activity!");
    }

    Log.i(TAG + " Entering Picture-in-Picture");

    android.app.PictureInPictureParams.Builder builder = new android.app.PictureInPictureParams.Builder().setAspectRatio(new Rational(1, 1));

    // https://developer.android.com/reference/android/app/Activity.html#enterPictureInPictureMode(android.app.PictureInPictureParams)
    //
    // The system may disallow entering picture-in-picture in various cases,
    // including when the activity is not visible, if the screen is locked
    // or if the user has an activity pinned.
    if (!currentActivity.enterPictureInPictureMode(builder.build())) {
      throw new RuntimeException("Failed to enter Picture-in-Picture");
    }
  }

  @ReactMethod
  public void enterPictureInPicture() {
    try {
      enterPictureInPictureMode();
      promise.resolve(true);
    } catch (RuntimeException re) {
      promise.reject(re);
    }
  }


}
