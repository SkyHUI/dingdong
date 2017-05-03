package com.sky.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.sky.utils.FileUtils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipException;

public class ZipExtractorTask extends AsyncTask<Void, Integer, Long> {
	private final String TAG = "ZipExtractorTask";
	private final File mInput;
	private final File mOutput;
	private final ProgressDialog mDialog;
	private long uncompressedSize = 1L;
	private long mProgress = 0L;
	private final Context mContext;
	private boolean mReplaceAll;

	public ZipExtractorTask(String in, String out, Context context, boolean replaceAll) {
		super();
		mInput = new File(in);
		mOutput = new File(out);
		if (!mOutput.exists()) {
			if (!mOutput.mkdirs()) {
				Log.e(TAG, "Failed to make directories:" + mOutput.getAbsolutePath());
			}
		}
		if (context != null) {
			mDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		} else {
			mDialog = null;
		}
		mContext = context;
		mReplaceAll = replaceAll;
	}

	@Override
	protected Long doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return unzip();
	}

	@Override
	protected void onPostExecute(Long result) {
		// TODO Auto-generated method stub
		// super.onPostExecute(result);
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		File file = new File(FileUtils.PATH_ROOT + "model.zip");
		if (file.exists()) {
			file.delete();
		}
		if (isCancelled())
			return;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		// super.onPreExecute();
		if (mDialog != null) {
//			mDialog.setTitle("Extracting");
			mDialog.setMessage("解压中...");
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					cancel(true);
				}
			});
			mDialog.show();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		// super.onProgressUpdate(values);
		if (mDialog == null)
			return;
//		if (values.length > 1) {
//			int max = values[1];
//			mDialog.setMax(max);
//		} else
//			mDialog.setProgress(values[0].intValue());
		if(values[0] != 0){
        	mDialog.setMessage(values[0]  + "%");
        }
	}

	private long unzip() {
		long extractedSize = 0L;
		Enumeration<ZipEntry> entries;
		/**
		 * 不能使用java.util.zip.ZipFile，会有中文乱码问题（文件名）
		 * 使用Apache的ant.jar
		 * 指定编码格式为“GBK”
		 * 完美解决乱码问题
		 */
		ZipFile zip = null;
		try {
			zip = new ZipFile(mInput,"GBK");
			uncompressedSize = getOriginalSize(zip);
			publishProgress(0, (int) uncompressedSize);
			entries = (Enumeration<ZipEntry>) zip.getEntries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					continue;
				}
				File destination = new File(mOutput, entry.getName());
				if (!destination.getParentFile().exists()) {
					Log.e(TAG, "make=" + destination.getParentFile().getAbsolutePath());
					destination.getParentFile().mkdirs();
				}
				if (destination.exists() && mContext != null && !mReplaceAll) {
				}
				ProgressReportingOutputStream outStream = new ProgressReportingOutputStream(destination);
				extractedSize += copy(zip.getInputStream(entry), outStream);
				outStream.close();
			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("TAG","error1:"+e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(zip != null){
					zip.close();
				}
			} catch (IOException e) {
				Log.i("TAG","error2:"+e.getMessage());
				e.printStackTrace();
			}
		}
		return extractedSize;
	}

	private long getOriginalSize(ZipFile file) {
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.getEntries();
		long originalSize = 0l;
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entry.getSize() >= 0) {
				originalSize += entry.getSize();
			}
		}
		return originalSize;
	}

	private int copy(InputStream input, OutputStream output) {
		byte[] buffer = new byte[1024 * 8];
		BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
		BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
		int count = 0, n = 0;
		try {
			while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
				out.write(buffer, 0, n);
				count += n;
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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

	private final class ProgressReportingOutputStream extends FileOutputStream {
		public ProgressReportingOutputStream(File file) throws FileNotFoundException {
			super(file);
		}

		@Override
		public void write(byte[] buffer, int byteOffset, int byteCount) throws IOException {
			// TODO Auto-generated method stub
			super.write(buffer, byteOffset, byteCount);
			mProgress += byteCount;
//			Log.e("TAG", "下载进度："+ mProgress * 100 / uncompressedSize);
			publishProgress( (int) (mProgress * 100 / uncompressedSize));
		}
	}
}
