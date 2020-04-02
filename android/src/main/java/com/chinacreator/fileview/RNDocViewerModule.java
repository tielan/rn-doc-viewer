package com.chinacreator.fileview;

import com.chinacreator.fileview.videoview.VideoPlayerActivity;
import com.chinacreator.fileview.view.FileViewActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;

import android.content.Intent;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdVideo;


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
  public void openDoc(ReadableMap arg_object, Callback callback) {
      try {
        if (arg_object.getString("url") != null) {
            final String url = arg_object.getString("url");
            final String fileName = arg_object.hasKey("fileName") ? arg_object.getString("fileName") : null;
            final Boolean cache = arg_object.hasKey("cache") ? arg_object.getBoolean("cache") : null;
            Intent intent = new Intent(getCurrentActivity(),FileViewActivity.class);
            intent.putExtra(FileViewActivity.URL_STR,url);
            intent.putExtra(FileViewActivity.File_Type,1);
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

    @ReactMethod
    public void openImg(ReadableArray args, Callback callback) {
        final ReadableMap arg_object = args.getMap(0);
        try {
            if (arg_object.getString("url") != null) {
                final String url = arg_object.getString("url");
                final String fileName = arg_object.hasKey("fileName") ? arg_object.getString("fileName") : null;
                final Boolean cache = arg_object.hasKey("cache") ? arg_object.getBoolean("cache") : null;
                Intent intent = new Intent(getCurrentActivity(),FileViewActivity.class);
                intent.putExtra(FileViewActivity.URL_STR,url);
                intent.putExtra(FileViewActivity.FileName_STR,fileName);
                intent.putExtra(FileViewActivity.File_Type,0);
                intent.putExtra(FileViewActivity.Cache_STR,cache);
                getCurrentActivity().startActivity(intent);
            }else{
                callback.invoke(false);
            }
        } catch (Exception e) {
            callback.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void openVideo(ReadableMap args, Callback callback) {
        String url = args.getString("url");
        final String fileName = args.hasKey("fileName") ? args.getString("fileName") : null;
        Intent intent = new Intent(getCurrentActivity(), VideoPlayerActivity.class);
        intent.putExtra(FileViewActivity.URL_STR,url);
        intent.putExtra(FileViewActivity.FileName_STR,fileName);
        getCurrentActivity().startActivity(intent);
    }


}
