/* Copyright 2011 Google Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * Project home page: http://code.google.com/p/usb-serial-for-android/
 */

package com.sky.hkg;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.sky.heartbeat.BaseActivity;
import com.sky.heartbeat.LeafChartActivity;
import com.sky.heartbeat.R;
import com.sky.threadpool.ThreadPool;
import com.sky.utils.FileUtils;
import com.sky.view.MustDoThingDailog;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Monitors a single {@link UsbSerialDriver} instance, showing all data
 * received.
 *
 * @author mike wakerly (opensource@hoho.com)
 */
public class SerialConsoleActivity extends BaseActivity implements OnClickListener{

    private final byte START_FLAG = 0x20;   //开始标志位
    
    private final byte START_SAMPLING = 0x32;   //开始取样
    
    private final byte END_SAMPLING = 0x33;     //结束取样
    
    private byte[] bytes = new byte[2];     //存放命令信息(16进制)
    
//    private boolean isSampling = false;     //是否正在采样
    
//    private final int SAMPLING = 1;     //采样信号
    
    private String fileName = "2017年1月1日0时0分0秒.txt"; //保存的文件名字
    
    private final String TAG = SerialConsoleActivity.class.getSimpleName();

    /**
     * Driver instance, passed in statically via
     * {@link #show(Context, UsbSerialDriver)}.
     *
     * <p/>
     * This is a devious hack; it'd be cleaner to re-create the driver using
     * arguments passed in with the {@link #startActivity(Intent)} intent. We
     * can get away with it because both activities will run in the same
     * process, and this is a simple demo.
     */
    private static UsbSerialDriver sDriver = null;

    private Button bt_sampling;
    
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private SerialInputOutputManager mSerialIoManager;

    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

        @Override
        public void onRunError(Exception e) {
            Log.d(TAG, "Runner stopped.");
        }

        
        /**
         * 在此接受到脉搏数据
         *  先将byte[]转换为 16进制字符串
         *  再将16进制字符串转换为整数
         */
        @Override
        public void onNewData(final byte[] data) {
            
            ThreadPool.getInstance().execute(new Runnable() {
                
                @Override
                public void run() {
                    int pulse = Integer.parseInt(HexDump.toHexString(data),16); 
                    if(pulse >0&& pulse<255){   //过滤掉脉搏的非正常值
                        FileUtils.savePulse(""+pulse,fileName);
                    }
                }
            });
            
        }
    };

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_device_console);
        initToolBar();
    }

    @Override
    protected void findId() {
        bt_sampling = (Button) findViewById(R.id.bt_sampling);

    }

    @Override
    protected void setListener() {
        bt_sampling.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopIoManager();
        if (sDriver != null) {
            try {
                sDriver.close();
            } catch (IOException e) {
                // Ignore.
            }
            sDriver = null;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed, sDriver=" + sDriver);
        if (sDriver == null) {
            alert("未发现设备，请先连接设备", new MustDoThingDailog.NeedDoThing() {
                @Override
                public void mustDoThings() {
                    finish();
                }
            });
        } else {
            try {
                sDriver.open();
                sDriver.setParameters(9600, 8, UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                alert("端口打开失败，错误信息：" + e.getMessage(), new MustDoThingDailog.NeedDoThing() {
                    @Override
                    public void mustDoThings() {
                        finish();
                    }
                });
                try {
                    sDriver.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                sDriver = null;
                return;
            }
        }
        onDeviceStateChange();
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sDriver != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sDriver, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    /**
     * Starts the activity, using the supplied driver instance.
     *
     * @param context
     * @param driver
     */
    static void show(Context context, UsbSerialDriver driver) {
        sDriver = driver;
        final Intent intent = new Intent(context, SerialConsoleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()) {
        case R.id.bt_sampling:
            bt_sampling.setEnabled(false);
            showProgressDialog("正在采样中...");
            bytes[0] = START_FLAG;
            //正在采样
            fileName = FileUtils.getFormatTime();   //获取当前时间，作为文件名字
            FileUtils.createFile(fileName); //创建文件
            bytes[1] = START_SAMPLING;
            try {
                if(sDriver != null){
                    sDriver.write(bytes, 300);
                    Toast.makeText(this, "开始采样", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //30秒后自动停止采样
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    closeProgressDialog();
                    bt_sampling.setEnabled(true);
                    bytes[1] = END_SAMPLING;
                    try {
                        if(sDriver != null){
                            sDriver.write(bytes, 300);
                            Toast.makeText(SerialConsoleActivity.this, "结束采样", Toast.LENGTH_SHORT).show();
                        }
                        toLeafeChart();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 30000);
            break;

        default:
            break;
    }
        
    }

    /**
     * 跳转到脉搏波曲线图页面
     */
    private void toLeafeChart(){
        Intent intent = new Intent(this, LeafChartActivity.class);
        if(!TextUtils.isEmpty(fileName)){
            intent.putExtra("fileName",fileName);
            startActivity(intent);
        }
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.collect_toolbar);
        toolbar.setTitle(getResources().getString(R.string.collect_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //设置返回键可用
    }

    /**
     * ToolBar上方的返回键
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}
