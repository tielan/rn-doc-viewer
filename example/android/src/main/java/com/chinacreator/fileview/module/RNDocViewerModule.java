package com.chinacreator.fileview.module;

import com.chinacreator.fileview.view.FileViewActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;



import android.content.Intent;




public class RNDocViewerModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNDocViewerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNDocViewer";
  }

  @ReactMethod
  public void openDoc(ReadableArray args, Callback callback) {
      final ReadableMap arg_object = args.getMap(0);
      try {
        if (arg_object.getString("url") != null) {

            final String url = arg_object.getString("url");
            final String fileName = arg_object.hasKey("fileName") ? arg_object.getString("fileName") : null;
            final Boolean cache = arg_object.hasKey("cache") ? arg_object.getBoolean("cache") : null;


            Intent intent = new Intent(getCurrentActivity(),FileViewActivity.class);
            intent.putExtra(FileViewActivity.URL_STR,url);
            intent.putExtra(FileViewActivity.FileName_STR,fileName);
            intent.putExtra(FileViewActivity.Cache_STR,cache);

            getCurrentActivity().startActivity(intent);
        }else{
            callback.invoke(false);
        }
       } catch (Exception e) {
            callback.invoke(e.getMessage());
       }
  }


}
