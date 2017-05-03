package com.sky.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;

import com.sky.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {
    private final String TAG = "DownLoaderTask";  
    private URL mUrl;  
    private File mFile;  
    private ProgressDialog mDialog;  
    private long mProgress = 0L; 
    private long length = 1L;
    private String FILE_NAME = "";
    private ProgressReportingOutputStream mOutputStream;  
    private Context mContext;  
    public DownLoaderTask(String url,String out,Context context){  
        super();  
        if(context!=null){  
            mDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);  
            mContext = context;  
        }  
        else{  
            mDialog = null;  
        }  
          
        try {  
            mUrl = new URL(url);  
            File file = new File(FileUtils.PATH_ROOT);
            if(!file.exists()){
            	file.mkdirs();
            }
            FILE_NAME = new File(mUrl.getFile()).getName();  
            mFile = new File(out, FILE_NAME);  
            Log.d(TAG, "out="+out+", name="+FILE_NAME+",mUrl.getFile()="+mUrl.getFile());  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
    }  
      
    @Override  
    protected void onPreExecute() {  
        if(mDialog!=null){  
//            mDialog.setTitle("");  
            mDialog.setMessage("数据模型下载中...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if(keyCode == KeyEvent.KEYCODE_BACK){
						return false;
					}
					return false;
				}
			});
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
            mDialog.setOnCancelListener(new OnCancelListener() {  
                  
                @Override  
                public void onCancel(DialogInterface dialog) {  
                    cancel(true);  
                }  
            });  
            mDialog.show();  
        }  
    }  
  
    @Override  
    protected Long doInBackground(Void... params) {  
        // TODO Auto-generated method stub  
        return download();  
    }  
  
    @Override  
    protected void onProgressUpdate(Integer... values) {  
        //super.onProgressUpdate(values);  
        if(mDialog==null)  
            return;  
//        if(values.length>1){  
//            int contentLength = values[1];  
//            if(contentLength==-1){  
//                mDialog.setIndeterminate(true);  
//            }  
//            else{  
//                mDialog.setMax(contentLength);  
//            }  
//        }  
//        else{  
//            mDialog.setProgress(values[0].intValue());  
        if(values[0] != 0){
        	mDialog.setMessage(values[0]  + "%");
        }
//        }  
    }  
  
    @Override  
    protected void onPostExecute(Long result) {  
        // TODO Auto-generated method stub  
        //super.onPostExecute(result);  
        if(mDialog!=null&&mDialog.isShowing()){  
            mDialog.dismiss();  
        }  
        if(isCancelled())  
            return;
        ZipExtractorTask zipExtractorTask = new ZipExtractorTask(FileUtils.PATH_ROOT + FILE_NAME,FileUtils.PATH_ROOT,mContext,true);
        zipExtractorTask.execute();
    }  
  
    private long download(){  
        URLConnection connection = null;  
        int bytesCopied = 0;  
        try {  
            connection = mUrl.openConnection();  
            length = connection.getContentLength();  
            Log.e("Main", "length--->"+length);
            if(mFile.exists()){  
                mFile.delete();  
            }  
            mOutputStream = new ProgressReportingOutputStream(mFile);  
//            publishProgress(0,length);  
            bytesCopied =copy(connection.getInputStream(),mOutputStream);  
            if(bytesCopied!=length&&length!=-1){  
                Log.e(TAG, "Download incomplete bytesCopied="+bytesCopied+", length"+length);  
            }  
            mOutputStream.close();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return bytesCopied;  
    }  
    private int copy(InputStream input, OutputStream output){  
        byte[] buffer = new byte[1024*8];  
        BufferedInputStream in = new BufferedInputStream(input, 1024*8);  
        BufferedOutputStream out  = new BufferedOutputStream(output, 1024*8);  
        int count =0,n=0;  
        try {  
            while((n=in.read(buffer, 0, 1024*8))!=-1){  
                out.write(buffer, 0, n);  
                count+=n;  
            }  
            out.flush();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }finally{  
            try {  
                out.close();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
            try {  
                in.close();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
        return count;  
    }  
    private final class ProgressReportingOutputStream extends FileOutputStream{  
  
        public ProgressReportingOutputStream(File file)  
                throws FileNotFoundException {  
            super(file);  
            // TODO Auto-generated constructor stub  
        }  
  
        @Override  
        public void write(byte[] buffer, int byteOffset, int byteCount)  
                throws IOException {  
            super.write(buffer, byteOffset, byteCount);  
            mProgress += byteCount;  
//            Log.e("Main", "mProgress--->"+mProgress);
//            Log.e("TAG", "下载进度："+ mProgress * 100 / length + "%");
            publishProgress((int) (mProgress * 100 / length) );  
        }

    }


}