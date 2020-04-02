package com.chinacreator.fileview.videoview;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.chinacreator.fileview.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


/**
 * 适配了普通的Activity，如果不适配并且不继承AppCompatActivity的话会出现Context空指针的情况
 * Created by Nathen on 2017/9/19.
 */
public class VideoPlayerActivity extends Activity {
    public static final String URL_STR = "URL_STR";
    public static final String FileName_STR = "FileName_STR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        JzvdStd jzvdStd = findViewById(R.id.videoplayer);
        String url = getIntent().getStringExtra(URL_STR);
        String fileName = getIntent().getStringExtra(FileName_STR);

        jzvdStd.setUp(url, TextUtils.isEmpty(fileName) ? "" : fileName, JzvdStd.SCREEN_NORMAL);
        jzvdStd.startVideo();
        jzvdStd.setOnLister(new JzvdStd.OnLister() {
            @Override
            public void onBackClick() {
                VideoPlayerActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
