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
import android.app.PictureInPictureParams.Builder;
import android.app.Activity;

public class RNAndroidPipModule extends ReactContextBaseJavaModule {

  private static final String MODULE_NAME = "RNAndroidModule";

  private final ReactApplicationContext reactContext;

  public RNAndroidPipModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return MODULE_NAME;
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  public boolean isPictureInPictureSupported() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
  }

  public void enterPictureInPictureMode() {
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
      throw new IllegalStateException("Picture-in-Picture not supported");
    }
    Activity currentActivity = getCurrentActivity();
    if(currentActivity == null) {
      throw new IllegalStateException("No current Activity!");
    }

    Log.i("RNAndroidModule", "Entering Picture-in-Picture");

    Builder builder = new Builder().setAspectRatio(new android.util.Rational(1, 1));

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
  public void enterPictureInPicture(Promise promise) {
    try {
      enterPictureInPictureMode();
      promise.resolve(true);
    } catch (RuntimeException re) {
      promise.reject(re);
    }
  }


}
