package com.chinacreator.fileview.view;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinacreator.fileview.R;

public class FileViewActivity extends Activity {

    public static final String URL_STR = "URL_STR";
    public static final String FileName_STR = "FileName_STR";
    public static final String Cache_STR = "Cache_STR";

    private SuperFileView superFileView;
    private ImageView backBtn;
    private TextView titleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileview_activity);
        superFileView = (SuperFileView)findViewById(R.id.fileview);
        backBtn = (ImageView)findViewById(R.id.backBtn);
        titleView = (TextView)findViewById(R.id.titleView);

        String url = getIntent().getStringExtra(URL_STR);
        String fileName = getIntent().getStringExtra(FileName_STR);
        boolean cache = getIntent().getBooleanExtra(Cache_STR,true);

        superFileView.setOnLoad(new SuperFileView.OnLoad() {
            @Override
            public void onLoadSuccess(String fileName) {
                titleView.setText(fileName);
            }
        });
        superFileView.loadUrl(url,null,null,fileName,cache);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
