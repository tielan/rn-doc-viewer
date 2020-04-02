package com.chinacreator.fileview.view;

import android.app.Activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

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
    public static final String File_Type = "File_Type";
    private View lyBg;
    private PDFView pdfView;
    private PhotoView photoView;
    public ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileview_activity);
        pdfView =  findViewById(R.id.pdfView);
        photoView = findViewById(R.id.photoView);
        lyBg = findViewById(R.id.ly_bg);
        loadingProgressBar = findViewById(R.id.loading);
        String url = getIntent().getStringExtra(URL_STR);
        String fileName = getIntent().getStringExtra(FileName_STR);
        int fileType = getIntent().getIntExtra("File_Type",0);//0 image 1 pdf
        boolean cache = getIntent().getBooleanExtra(Cache_STR, true);
        photoView.setVisibility(View.GONE);
        photoView.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        if(fileType == 1){
            lyBg.setBackgroundColor(Color.WHITE);
            loadingProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.jz_loading_dark));
        }else{
            lyBg.setBackgroundColor(Color.BLACK);
        }
        SuperFileManager.getInstance().init(this);
        SuperFileManager.getInstance().loadFile(url,null,null,fileName,cache,new SuperFileManager.LoadFileCallback(){
            @Override
            public void onLoadSuccess(String fileName) {
                loadingProgressBar.setVisibility(View.GONE);
                showFile(fileName);
            }
            @Override
            public void onLoadError(String message) {

            }
            @Override
            public void downloadProgress(long currentSize, long totalSize) {

            }
        });
        lyBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    pdfView.setVisibility(View.VISIBLE);
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
