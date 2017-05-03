package com.sky.heartbeat;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.bean.News;
import com.sky.onekeyshare.OnekeyShare;
import com.sky.utils.CommandOrder;
import com.sky.utils.RequestConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：新闻详情页面
 * 创建人：Sky
 * 创建时间：2017/4/30 15:59
 */
public class DetailActivity extends BaseActivity {

    private TextView tv_title,tv_date,tv_content;

    private ImageView img;

    private News news = null;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void findId() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        tv_title = (TextView) findViewById(R.id.detail_tv_title);
        tv_date = (TextView) findViewById(R.id.detail_tv_date);
        tv_content = (TextView) findViewById(R.id.detail_tv_content);
        img = (ImageView) findViewById(R.id.detail_img);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        news = (News) getIntent().getSerializableExtra("news");
        if(news != null){
            tv_title.setText(news.getTitle());
            tv_date.setText(news.getDate());
            tv_content.setText(news.getContent());
            Glide.with(this).load(news.getPicture_url()).fitCenter().placeholder(R.mipmap.login_head).into(img);
        }
    }

    @Override
    protected void processLogic() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_detail_love:
                //收藏
                JSONObject json = new JSONObject();
                try {
                    json.put("command", CommandOrder.COMMAND_NEWS_LOVE);
                    json.put("user_id",RequestConstant.USER_ID);
                    json.put("news_id",news.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestServer(RequestConstant.REQUEST_NEWS, json, new DataCallback() {
                    @Override
                    public void onSuccess(String t) {
                        alert(t,null);
                    }
                });
                break;
            case R.id.menu_detail_share:
                //分享
                showShare();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_detail_news,menu);
        return true;
    }

    /**
     * 一键分享
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(news.getTitle());
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(news.getContent());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(news.getPicture_url());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("https://www.pgyer.com/ding_dong");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("https://www.pgyer.com/ding_dong");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://www.pgyer.com/ding_dong");

        // 启动分享GUI
        oks.show(this);
    }
}
