package com.chinacreator.fileview.view;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chinacreator.fileview.R;
import com.chinacreator.fileview.photoview.OnViewTapListener;
import com.chinacreator.fileview.photoview.PhotoView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnTapListener;

import java.io.File;

public class FileViewActivity extends Activity {

    public static final String URL_STR = "URL_STR";
    public static final String FileName_STR = "FileName_STR";
    public static final String Cache_STR = "Cache_STR";

    private PDFView pdfView;
    private PhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileview_activity);
        pdfView =  findViewById(R.id.pdfView);
        photoView = findViewById(R.id.photoView);

        String url = getIntent().getStringExtra(URL_STR);
        String fileName = getIntent().getStringExtra(FileName_STR);
        boolean cache = getIntent().getBooleanExtra(Cache_STR, true);
        SuperFileManager.getInstance().loadFile(url,null,null,fileName,cache,new SuperFileManager.LoadFileCallback(){

            @Override
            public void onLoadSuccess(String fileName) {
                showFile(fileName);
            }
            @Override
            public void onLoadError(String message) {

            }

            @Override
            public void downloadProgress(long currentSize, long totalSize) {

            }
        });
        photoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });
    }
    private OnTapListener onTapListener = new OnTapListener() {
        @Override
        public boolean onTap(MotionEvent e) {
            finish();
            return false;
        }
    };
    public void showFile(final String fileName){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                File file = new File(fileName);
                if(isPicture(fileName)){
                    photoView.setVisibility(View.VISIBLE);
                    photoView.setImageURI(Uri.fromFile(file));
                }else{
                    photoView.setVisibility(View.GONE);
                    pdfView.fromFile(file).onTap(onTapListener).load();
                }
            }
        });
    }

    public static boolean isPicture(String pInput) {
        String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, pInput.length());
        String imgeArray[][] = {
                {"bmp", "0"}, {"dib", "1"}, {"gif", "2"},
                {"jfif", "3"}, {"jpe", "4"}, {"jpeg", "5"},
                {"jpg", "6"}, {"png", "7"}, {"tif", "8"},
                {"tiff", "9"}, {"ico", "10"}
        };
        for (int i = 0; i < imgeArray.length; i++) {
            if (imgeArray[i][0].equals(tmpName.toLowerCase())) {
                return true;
            }
        }
        return false;

    }
}
