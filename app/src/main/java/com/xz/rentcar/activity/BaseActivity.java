package com.xz.rentcar.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ZoomControls;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xz.rentcar.R;
import com.xz.rentcar.util.PreferenceUtils;
import com.xz.rentcar.util.ThemeUtils;

import butterknife.ButterKnife;

/**
 * Created by xaozu on 15/8/3.
 * 所有页面的父类
 * 抽象类
 */
public abstract class  BaseActivity extends AppCompatActivity {
    public ActionBar mActionBar;
    public PreferenceUtils preferenceUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceUtils=PreferenceUtils.getInstance(this);
        initTheme();
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(getLayoutView());
        //初始化控件获取插件
        ButterKnife.inject(this);
        initToolbar();
    }

    /**
     * 初始主题
     */
    private void initTheme() {
        ThemeUtils.Theme theme = getCurrentTheme();
        ThemeUtils.changTheme(this, theme);
    }

    /**
     * 当前主题
     * @return
     */
    public ThemeUtils.Theme getCurrentTheme() {
        return ThemeUtils.Theme.mapValueToTheme(3);
    }

    /**
     * 初始化状态栏
     */
    @TargetApi(19)
    private void initWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //底部透明
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getColorPrimary());
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    /**
     * 得到主题颜色
     * @return
     */
    public int getColorPrimary(){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 初始化标题栏
     * @param toolbar
     */
    protected void initToolbar(Toolbar toolbar){
        if (toolbar == null)
            return;
        toolbar.setBackgroundColor(getColorPrimary());
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getColor(R.color.action_bar_title_color));
        toolbar.collapseActionView();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mActionBar=getSupportActionBar();
        }
    }
    /**
     * 增加了默认的返回finish事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    protected int getColor(int res){
        if (res <= 0)
            throw new IllegalArgumentException("resource id can not be less 0");
        return getResources().getColor(res);
    }
    public void toActivity(Class c){
        Intent i=new Intent(this,c);
        startActivity(i);
    }

    //供所有页面实现的抽象方法，每个页面都需要初始化状态栏，不同的标题
    protected abstract void initToolbar();
    //得到页面布局
    protected abstract int getLayoutView();

    /**
     * 配置相关，可根据具体需求修改
     */
    public void initLocationOption(LocationClient client) {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式为高精度
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度类型
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms，可根据具体需求修改
        option.setIsNeedAddress(true);
        client.setLocOption(option);
    }

    //处理百度地图界面元素
    public void handlMap(MapView mapView) {
        // 隐藏缩放控件
        int childCount = mapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
//        zoom.set
        zoom.setVisibility(View.INVISIBLE);
        // 隐藏比例尺控件
        int count = mapView.getChildCount();
        View scale = null;
        for (int i = 0; i < count; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                scale = child;
                break;
            }
        }
        scale.setVisibility(View.INVISIBLE);
        // 删除百度地图logo
        mapView.removeViewAt(1);
    }
}
