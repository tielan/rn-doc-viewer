package com.chinacreator.fileview.view;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieManager;

import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuperFileManager {

    public static final String FilePATH = "fileManager";
    private static final String TAG = "SuperFileView";

    public static final String ERROR_FILE_NOT_FOUND = "文件不存在！";
    public static final String ERROR_UNKNOWN_ERROR = "未知错误！";

    private File parentFileFolder;
    private static final SuperFileManager INSTANCE = new SuperFileManager();
    private Context mContext;
    private FileDownloaderAsyncTask fileDownloaderAsyncTask;

    private SuperFileManager() {

    }

    public static SuperFileManager getInstance() {
        return INSTANCE;
    }

    /**
     * 根据文件夹获取文件列表
     * @param folder
     * @return
     */
    public List<File> getFileList(String folder){
        List<File> list = new ArrayList<>();
        File folderFile;
        if(folder != null){
            folderFile = parentFileFolder;
        }else{
            folderFile =  new File(parentFileFolder,folder);
        }
        for (File file: folderFile.listFiles()) {
            if(!file.isHidden() && !file.isDirectory()){
                list.add(file);
            }
        }
        return list;
    };

    /**
     * 获取文件夹列表
     * @return
     */
    public List<File> getFolder(){
        List<File> list = new ArrayList<>();
        for (File file: parentFileFolder.listFiles()) {
            if(!file.isHidden() && file.isDirectory()){
                list.add(file);
            }
        }
        return list;
    }

    public void deleteFile(String folder,String fileName){
        File file;
        if(folder != null){
            file = new File(parentFileFolder,fileName);
        }else{
            File  folderFile =  new File(parentFileFolder,folder);
            file = new File(folderFile,fileName);
        }
        if(file != null && file.exists()){
            file.delete();
        }
    }




    public  interface LoadFileCallback{
        /**
         * 文件存在 返回绝对路径 如果文件不存在 返回null
         * @param path
         */
       void onLoadSuccess(String path);
       void onLoadError(String message);
       void downloadProgress(long currentSize, long totalSize);
    }

    public void loadFile(String url, Map<String,String> head, String folder,String _fileName, boolean cache, final LoadFileCallback callback){

        File localFile;
        File pathFile;

        File file = new File(url);
        String fileName = file.getName();
        if(_fileName != null){
            fileName = _fileName;
        }
        if(folder == null){
            localFile =  new File(parentFileFolder,fileName);
        }else{
            pathFile = new File(parentFileFolder,folder);
            if(!pathFile.exists()){
                pathFile.mkdir();
            }
            localFile =  new File(pathFile,fileName);
        }

        if (cache && localFile.exists()) {

            Log.d(TAG,"加载缓存文件:"+localFile.getAbsolutePath());
            if(callback != null){
                callback.onLoadSuccess(localFile.getAbsolutePath());
            }
            return;
        }
        Log.d(TAG,"下载目标文件:"+url);
        Log.d(TAG,"存放路径:"+localFile.getAbsolutePath());

        fileDownloaderAsyncTask = new FileDownloaderAsyncTask(callback, url, localFile);
        fileDownloaderAsyncTask.execute();
    }

    public void cancelTag(){
        if(fileDownloaderAsyncTask != null)
        fileDownloaderAsyncTask.cancel(true);
    }

    /**
     * 初始化調用
     * @param app
     */
    public  void  init(Application app){
        this.mContext = app;
        initX5(app);
        parentFileFolder = new File(app.getCacheDir(),FilePATH);
        if(!parentFileFolder.exists()){
            parentFileFolder.mkdir();
        }
    }


    private  void initX5(Context mContext){
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("app", " =========onViewInitFinished is " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(mContext, cb);
    }



    private class FileDownloaderAsyncTask extends AsyncTask<Void, Void, File> {
        private final LoadFileCallback callback;
        private final String url;
        private final File pathFile;

        public FileDownloaderAsyncTask(LoadFileCallback callback, String url,File pathFile) {
            super();
            this.callback = callback;
            this.url = url;
            this.pathFile = pathFile;
        }

        @Override
        protected File doInBackground(Void... arg0) {
            return downloadFile(url, pathFile, callback);
        }

        @Override
        protected void onPostExecute(File result) {//执行完成
            if (result == null) {
                return;
            }
            callback.onLoadSuccess(result.getAbsolutePath());
        }
    }

    private File downloadFile(String url, File pathFile,LoadFileCallback callback) {

        try {

            URL url2 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
            int fileLength = conn.getContentLength();

            InputStream reader = conn.getInputStream();
            File f = File.createTempFile("tem", "." + pathFile.getName().substring(pathFile.getName().lastIndexOf("1")+1),
                    pathFile.getParentFile());
            f.setReadable(true, false);
            System.out.println(f.getPath());
            FileOutputStream outStream = new FileOutputStream(f);
            byte[] buffer = new byte[4096];
            long total = 0;
            int readBytes = reader.read(buffer);
            int i = 5;
            while (readBytes > 0) {
                total += readBytes;
                if (fileLength > 0 && i%5 == 0)
                {
                    callback.downloadProgress(total,fileLength);
                }
                outStream.write(buffer, 0, readBytes);
                readBytes = reader.read(buffer);
            }
            reader.close();
            outStream.close();
            if (f.exists()) {
                f.renameTo(pathFile);
            } else {
                System.out.println("File doesn't exist");
            }
            return pathFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            callback.onLoadError(ERROR_FILE_NOT_FOUND);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            callback.onLoadError(ERROR_UNKNOWN_ERROR);
            return null;
        }
    }



}
