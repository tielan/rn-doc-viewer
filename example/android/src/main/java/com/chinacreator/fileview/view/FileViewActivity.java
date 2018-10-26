package com.chinacreator.fileview.view;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinacreator.fileview.R;
import com.chinacreator.fileview.photoview.PhotoView;

import java.io.File;

public class FileViewActivity extends Activity {

    public static final String URL_STR = "URL_STR";
    public static final String FileName_STR = "FileName_STR";
    public static final String Cache_STR = "Cache_STR";

    private SuperFileView superFileView;
    private ImageView backBtn;
    private TextView titleView;
    private PhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileview_activity);
        superFileView = (SuperFileView) findViewById(R.id.fileview);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        titleView = (TextView) findViewById(R.id.titleView);
        photoView = (PhotoView) findViewById(R.id.photoView);

        String url = getIntent().getStringExtra(URL_STR);
        String fileName = getIntent().getStringExtra(FileName_STR);
        boolean cache = getIntent().getBooleanExtra(Cache_STR, true);

        superFileView.setOnLoad(new SuperFileView.OnLoad() {
            @Override
            public boolean onLoadSuccess(String fileName) {

                File file = new File(fileName);
                titleView.setText(file.getName());
                if(isPicture(fileName)){
                    photoView.setVisibility(View.VISIBLE);
                    photoView.setImageURI(Uri.fromFile(file));
                    return true;
                }else{
                    return false;
                }


            }
        });
        superFileView.loadUrl(url, null, null, fileName, cache);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
