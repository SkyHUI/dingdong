package com.sky.heartbeat;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.beiing.leafchart.LeafLineChart;
import com.beiing.leafchart.bean.Axis;
import com.beiing.leafchart.bean.AxisValue;
import com.beiing.leafchart.bean.Line;
import com.beiing.leafchart.bean.PointValue;
import com.beiing.leafchart.constant.Constant;
import com.sky.threadpool.ThreadPool;
import com.sky.utils.FileUtils;
import com.sky.utils.RequestConstant;
import com.sky.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据过多时，path显示不出来
 * 需要关闭硬件加速即可
 */
public class LeafChartActivity extends BaseActivity {

    private String TAG = "LeafChartActivity";

    private String fileName = null;//  要显示数据的文件名

    private List<Integer> pulseList = new ArrayList<>();    //存储脉搏波数据

    private LeafLineChart leafLineChart;

    private int START = 101;    //开始绘图

    private int size = 0;   //数据的大小

    private TextView tv_dbp,tv_sbp;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == START){
                initLineChart();
            }
        }
    };

    @Override
    protected void setContentView() {
        Constant.getInstance().CHART_POWER = 5;
        setContentView(R.layout.activity_leaf_chart);
        initToolBar();
        fileName = getIntent().getStringExtra("fileName");
        Log.e(TAG,"fileName:"+fileName);
    }

    @Override
    protected void findId() {
        leafLineChart = (LeafLineChart) findViewById(R.id.leaf_chart);
        tv_dbp = (TextView) findViewById(R.id.chart_tv_dbp);
        tv_sbp = (TextView) findViewById(R.id.chart_tv_sbp);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        //初始化脉搏波数据
        initChart();
    }

    @Override
    protected void processLogic() {

    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.chart_toolbar);
        toolbar.setTitle(getResources().getString(R.string.chart_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initChart() {

        //利用线程池读取脉搏波数据
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if(fileName != null){
                    pulseList = FileUtils.getPulse(fileName);
                    size = pulseList.size();
                    Log.i(TAG,"pulseList:"+pulseList.size());
                    //读取完数据开始绘图
                    if(size != 0){
                        handler.sendEmptyMessage(START);
                    }else{
                        Toast.makeText(LeafChartActivity.this,"数据获取失败",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    private void initLineChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(false);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(false).setShowText(true);
        leafLineChart.setAxisX(axisX);
        leafLineChart.setAxisY(axisY);

        List<Line> lines = new ArrayList<>();
        lines.add(getFoldLine());
        leafLineChart.setChartData(lines);

        leafLineChart.showWithAnimation(3000);

    }

    private List<AxisValue> getAxisValuesX(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < size ; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(i + "");
            axisValues.add(value);
        }
        return axisValues;
    }

    private List<AxisValue> getAxisValuesY(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i <7; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * 50));
            axisValues.add(value);
        }
        return axisValues;
    }

    private Line getFoldLine(){
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 0f) / size);
            int var = pulseList.get(i);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 300f);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#33B5E5"))
                .setLineWidth(1)
                .setPointColor(Color.YELLOW)
                .setCubic(true)
                .setPointRadius(3)
                .setFill(true)
                .setFillColr(Color.parseColor("#33B5E5"))
                .setOpenMoveSelect(true)
                .setMoveLineColor(Color.GREEN)
                .setHasLabels(false)
                .setLabelColor(Color.parseColor("#33B5E5"));
        return line;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //返回按钮
                finish();
                break;
            case R.id.menu_match:
                //匹配
                //1.获取所有主峰的X坐标
                List<Integer> XList = getMountains();
                //2.主峰前后各截取55个点，共111个点，并求各个点的平均值
                String points = getPoints(XList);
                //3.将这111个点发送到服务器进行用户最佳模型匹配
                doMatch(points);

                break;
            case R.id.menu_proofread:
                //校对
                if(SPUtil.isMatchSuccessful(this)){

                }else{
                    alert("请先点击匹配按钮，匹配用户最佳模型！",null);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 用户最佳模型匹配
     * @param points
     */
    private void doMatch(String points) {
        JSONObject json = new JSONObject();
        try {
            json.put("points",points);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestServer(RequestConstant.REQUEST_MATCH, json, new DataCallback() {
            @Override
            public void onSuccess(String t) {
                alert(t,null);
            }
        });
    }

    /**
     * 获取111个点
     * 主峰X的前后各取55个点
     * @param XList
     * @return
     */
    private String getPoints(List<Integer> XList) {
        StringBuffer buffer = new StringBuffer();

        for(int j = -55 ;j <= 55;j++){
            int y = 0;
            for(int i = 0;i < XList.size();i++){
                y += pulseList.get(XList.get(i) + j);
            }
            buffer.append(y / XList.size()+",");
            Log.i("TAG","y:" + y / XList.size());
        }
        return buffer.toString();
    }

    /**
     * 获取主峰的X坐标点
     * @return
     */
    private List<Integer> getMountains() {
        List<Integer> XList = new ArrayList<>();
        int size = pulseList.size();
        if(size > 2){
            int min = 0;//极小值点
            for(int i = 1;i < size - 2;i++){
                int a = pulseList.get(i) - pulseList.get(i - 1);
                int b = pulseList.get(i + 1) - pulseList.get(i);
                if(a > 0 && b < 0) {
                    /**
                     * 极大值点
                     * 1.保证主峰前后能各取到55个点
                     * 2.过滤掉一些非主峰点
                     */
                    if(i > 54 && i < size -54 && pulseList.get(i) - min > 50){
                        XList.add(i);   //将主峰的X坐标添加进去
                    }
                }else if(a < 0 && b > 0){
                    //极小值点
                    min = pulseList.get(i);
                }
            }
        }

        return XList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_chart,menu);
        setIconsVisible(menu,true);
        return true;
    }

    /**
     * 解决不显示menu icon的问题
     * @param menu
     * @param flag
     */
    private void setIconsVisible(Menu menu, boolean flag) {
        //判断menu是否为空
        if(menu != null) {
            try {
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //暴力访问该方法
                method.setAccessible(true);
                //调用该方法显示icon
                method.invoke(menu, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
