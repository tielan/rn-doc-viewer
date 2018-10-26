package com.chinacreator.fileview.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.util.Map;

public class SuperFileView extends FrameLayout  implements TbsReaderView.ReaderCallback{

    private static final String TAG = "SuperFileView";
    private Context mContext;
    private TbsReaderView mTbsReaderView;
    private TextView tvInformation;
    private String url;

    public SuperFileView(Context context) {
        this(context, null, 0);
    }

    public SuperFileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperFileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tvInformation = new TextView(context);
        tvInformation.setGravity(Gravity.CENTER);
        this.addView(tvInformation, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        mTbsReaderView = new TbsReaderView(context, this);
        this.addView(mTbsReaderView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        this.mContext = context;
    }
    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };

    interface OnLoad{
        boolean onLoadSuccess(String filePath);
    }
    private OnLoad onLoad;

    public void setOnLoad(OnLoad onLoad){
        this.onLoad = onLoad;
    }

    public void loadUrl(String url, Map<String,String> head, String folder,String fileName, boolean cache) {
        if(TextUtils.isEmpty(url)) return;
        this.url = url;
        SuperFileManager.getInstance().loadFile(url,head, folder,fileName,cache, new SuperFileManager.LoadFileCallback() {
            @Override
            public void onLoadSuccess(String filePath) {
               if(!onLoad.onLoadSuccess(filePath)){
                   openFile(filePath);
               }

            }

            @Override
            public void onLoadError(String message) {
                tvInformation.setVisibility(View.VISIBLE);
                tvInformation.setText(message);
            }

            @Override
            public void downloadProgress(long currentSize, long totalSize) {
                tvInformation.setVisibility(View.VISIBLE);
                tvInformation.setText((currentSize/1024)+"KB/"+totalSize/1024+"KB");
            }
        });
    }

    private void openFile(String absPath) {
        if (absPath != null) {
            tvInformation.setVisibility(View.GONE);
            //显示文件
            final Bundle bundle = new Bundle();
            bundle.putString("filePath", absPath);
            bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
            String pathFormat = parseFormat(absPath);
            boolean result = mTbsReaderView.preOpen(pathFormat, false);
            if (result) {
                mTbsReaderView.openFile(bundle);
            } else {
                tvInformation.setVisibility(View.VISIBLE);
                tvInformation.setText("不支持的文件格式");
            }
        } else {
            tvInformation.setVisibility(View.VISIBLE);
            tvInformation.setText("文件不存在");
        }
    }
    /**
     * 遍历所有view
     *
     * @param viewGroup
     */
    public void traversalView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                traversalView((ViewGroup) view);
            } else {
                doView(view);
            }
        }
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        super.updateViewLayout(view, params);

    }

    /**
     * 处理view
     *
     * @param view
     */
    private void doView(View view) {
        if(view instanceof  TextView){
            TextView text = (TextView)view;
            if("最近文件".equals(text.getText().toString())){
                View  pView = (View)text.getParent().getParent();
                pView.setVisibility(View.GONE);
            }
        }
    }


    //获取文件的格式判断文件是否支持 这里文件是不需要带.的 ******
    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    @Override
    public void onCallBackAction(Integer integer, Object arg1, Object o1) {
        if(integer == 19){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    traversalView(mTbsReaderView);
                }
            },10);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SuperFileManager.getInstance().cancelTag();
        mTbsReaderView.onStop();
        removeView(mTbsReaderView);
    }
}
