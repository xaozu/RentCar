package com.xz.rentcar.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.melnykov.fab.FloatingActionButton;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.xz.rentcar.R;
import com.xz.rentcar.activity.fragment.FragmentComeCar;
import com.xz.rentcar.activity.fragment.FragmentList;
import com.xz.rentcar.activity.fragment.FragmentUsercar;
import com.xz.rentcar.adapter.DrawerListAdapter;
import com.xz.rentcar.adapter.FragmentAdapter;
import com.xz.rentcar.adapter.SimpleListAdapter;
import com.xz.rentcar.util.SnackbarUtils;
import com.xz.rentcar.widget.ArcProgress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.left_drawer_listview)
    ListView leftDrawerListview;
    @InjectView(R.id.left_drawer)
    LinearLayout leftDrawer;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @InjectView(R.id.edit_note_type)
    Button btn_exit;
    @InjectView(R.id.tabs)
    TabLayout tabs;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.bd_mapView)
    MapView bdMapView;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.btn_dian)
    Button btnDian;
    @InjectView(R.id.btn_person)
    Button btnPerson;
    @InjectView(R.id.rela_map)
    RelativeLayout relaMap;
    @InjectView(R.id.rela_reserve)
    RelativeLayout relaReserve;
    @InjectView(R.id.arc_store)
    ArcProgress arcStore;
    @InjectView(R.id.rela_ing)
    RelativeLayout relaIng;
    @InjectView(R.id.btn_reserve_cancel)
    Button btnReserveCancel;
    @InjectView(R.id.btn_reserve_pay)
    Button btnReservePay;
    @InjectView(R.id.btn_rent_user)
    Button btnRentUser;
    @InjectView(R.id.btn_end)
    Button btnEnd;
    @InjectView(R.id.btn_charging)
    Button btnCharging;
    @InjectView(R.id.main_content)
    CoordinatorLayout mainContent;
    @InjectView(R.id.btn_back)
    Button btnBack;
    @InjectView(R.id.rela_pay)
    RelativeLayout relaPay;
    @InjectView(R.id.btn_wait_end)
    Button btnWaitEnd;
    @InjectView(R.id.rela_wait)
    RelativeLayout relaWait;
    private BaiduMap bdMap = null;
    public static BDLocation cntLocation;
    public static LocationClient bdClient;
    private boolean isFirstLoc = true;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;

    private Marker mMarkerAa;
    private Marker mMarkerBb;
    private Marker mMarkerCc;
    private Marker mMarkerDd;
    private InfoWindow mInfoWindow;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.map_icon_car_b);
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.drawable.map_icon_car_b);
    BitmapDescriptor bdC = BitmapDescriptorFactory
            .fromResource(R.drawable.map_icon_car_b);
    BitmapDescriptor bdD = BitmapDescriptorFactory
            .fromResource(R.drawable.map_icon_car_b);

    BitmapDescriptor bdAa = BitmapDescriptorFactory
            .fromResource(R.drawable.map_icon_driver_b);
    BitmapDescriptor bdBb = BitmapDescriptorFactory
            .fromResource(R.drawable.map_icon_driver_b);
    BitmapDescriptor bdCc = BitmapDescriptorFactory
            .fromResource(R.drawable.map_icon_driver_b);
    BitmapDescriptor bdDd = BitmapDescriptorFactory
            .fromResource(R.drawable.map_icon_driver_b);

    private List<String> drawListDates;//抽屉的数据
    private static final String[] DRAW_STR = {"租了个车", "实名认证", "我的信息", "我的订单", "关于我们"};
    private static final String[] CAR_TYPE = {"门店取车", "还车用户", "送车到家"};//租车分类
    private int mCurrentNoteType = 0;//当前选择的抽屉分类
    private ActionBarDrawerToggle mDrawerToggle;
    public static final String CAR_STATE_KEY = "CAR_STATE";
    public static final int CAR_STATE_STAR = 0;//开始显示预约列表
    public static final int CAR_STATE_RESERVE_ING = 1;//预约中，已付押金，等待取车
    public static final int CAR_STATE_ING = 2;//已取车，等待还车
    public static final int CAR_STATE_PAY = 3;//已还车，已支付，结束
    public static final int CAR_STATE_WAIT = 4;//已预约送车上面，等待车来
    public int CAR_STATE = 0;//状态

    //界面布局
    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawerView();
        showProgressWheel(false);//关了进度条

        initTab();
        initMap();
        arcStore.setProgress(58);
        btn_exit.setOnClickListener(this);
        fab.setOnClickListener(this);
        btnDian.setOnClickListener(this);
        btnPerson.setOnClickListener(this);
        btnReservePay.setOnClickListener(this);
        btnReserveCancel.setOnClickListener(this);
        btnRentUser.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        btnCharging.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnWaitEnd.setOnClickListener(this);
    }

    //这里判断状态
    public void initState() {
        CAR_STATE = preferenceUtils.getIntParam(CAR_STATE_KEY);
        if (CAR_STATE == CAR_STATE_STAR) { //预约车
            relaMap.setVisibility(View.GONE);
            relaIng.setVisibility(View.GONE);
            relaReserve.setVisibility(View.GONE);
            relaWait.setVisibility(View.GONE);
            relaPay.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.icon_map);
            appbar.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.VISIBLE);
        } else if (CAR_STATE == CAR_STATE_RESERVE_ING) {//取车
            relaMap.setVisibility(View.GONE);
            relaIng.setVisibility(View.GONE);
            relaReserve.setVisibility(View.VISIBLE);
            relaWait.setVisibility(View.GONE);
            relaPay.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            appbar.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
        } else if (CAR_STATE == CAR_STATE_ING) { //还车
            relaMap.setVisibility(View.GONE);
            relaIng.setVisibility(View.VISIBLE);
            relaReserve.setVisibility(View.GONE);
            relaWait.setVisibility(View.GONE);
            relaPay.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            appbar.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
        } else if (CAR_STATE == CAR_STATE_PAY) {//支付
            relaMap.setVisibility(View.GONE);
            relaIng.setVisibility(View.GONE);
            relaReserve.setVisibility(View.GONE);
            relaWait.setVisibility(View.GONE);
            relaPay.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            appbar.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
        }else if (CAR_STATE == CAR_STATE_WAIT) {//预约送车
            relaMap.setVisibility(View.GONE);
            relaIng.setVisibility(View.GONE);
            relaReserve.setVisibility(View.GONE);
            relaWait.setVisibility(View.VISIBLE);
            relaPay.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            appbar.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
        }



    }

    /**
     * 初始化百度地图
     */
    private void initMap() {
        handlMap(bdMapView);
        //获取地图
        bdMap = bdMapView.getMap();
        //设置初始缩放级别
        //3.0-19.0
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(bdMap.getMaxZoomLevel() - 4);
        bdMap.animateMapStatus(u);
        //普通地图
        bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        bdMap.setMyLocationEnabled(true);
        initOverlay();//画点
        //初始化定位相关
        bdClient = new LocationClient(getApplicationContext());
        bdClient.registerLocationListener(new LocationListener());
        initLocationOption(bdClient);
        //默认在onCreate中启动定位
        bdClient.start();
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = null;//BitmapDescriptorFactory.fromResource(R.drawable.location_icon_driver);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
        bdMap.setMyLocationConfigeration(config);

        bdMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker == mMarkerA)
                    initMarkerPop(marker, "闸北彭江店-服务点A");
                else if (marker == mMarkerAa)
                    initMarkerPop(marker, "张三(50%)");
                else if (marker == mMarkerB)
                    initMarkerPop(marker, "闸北彭江店-服务点B");
                else if (marker == mMarkerBb)
                    initMarkerPop(marker, "李四(60%)");
                else if (marker == mMarkerC)
                    initMarkerPop(marker, "闸北彭江店-服务点C");
                else if (marker == mMarkerCc)
                    initMarkerPop(marker, "王五(40%)");
                else if (marker == mMarkerD)
                    initMarkerPop(marker, "闸北彭江店-服务点D");
                else if (marker == mMarkerDd)
                    initMarkerPop(marker, "赵敏(80%)");
                return true;
            }
        });
    }

    /**
     * 门店覆盖层
     */
    public void initOverlay() {
        bdMap.clear();
        // add marker overlay
        LatLng llA = new LatLng(31.290385, 121.45318);//121.45318,31.290385
        LatLng llB = new LatLng(31.295137, 121.460367);//121.460367,31.295137
        LatLng llC = new LatLng(31.296741, 121.453683);//121.453683,31.296741
        LatLng llD = new LatLng(31.293594, 121.446497);//121.446497,31.293594

        OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(0).draggable(true);
        mMarkerA = (Marker) (bdMap.addOverlay(ooA));


        OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdB)
                .zIndex(0);
        mMarkerB = (Marker) (bdMap.addOverlay(ooB));
        initMarkerPop(mMarkerB, "闸北彭江店-服务点B");

        OverlayOptions ooc = new MarkerOptions().position(llC).icon(bdC)
                .zIndex(0).draggable(true);
        mMarkerC = (Marker) (bdMap.addOverlay(ooc));
        initMarkerPop(mMarkerC, "闸北彭江店-服务点C");

        OverlayOptions ood = new MarkerOptions().position(llD).icon(bdD)
                .zIndex(0);
        mMarkerD = (Marker) (bdMap.addOverlay(ood));
        initMarkerPop(mMarkerD, "闸北彭江店-服务点D");

    }

    /**
     * 用户覆盖层
     */
    private void initUserOverlay() {
        bdMap.clear();

        LatLng llA = new LatLng(31.290508, 121.45636);//121.45636,31.290508
        LatLng llB = new LatLng(31.291804, 121.455965);//121.455965,31.291804
        LatLng llC = new LatLng(31.295384, 121.456612);//121.456612,31.295384
        LatLng llD = new LatLng(31.294612, 121.451653);//121.451653,31.294612

        OverlayOptions ooAa = new MarkerOptions().position(llA).icon(bdAa)
                .zIndex(9).draggable(true);
        mMarkerAa = (Marker) (bdMap.addOverlay(ooAa));
        initMarkerPop(mMarkerAa, "张三(50%)");

        OverlayOptions ooBb = new MarkerOptions().position(llB).icon(bdBb)
                .zIndex(5);
        mMarkerBb = (Marker) (bdMap.addOverlay(ooBb));
        initMarkerPop(mMarkerBb, "李四(60%)");

        OverlayOptions oocc = new MarkerOptions().position(llC).icon(bdCc)
                .zIndex(7).draggable(true);
        mMarkerCc = (Marker) (bdMap.addOverlay(oocc));
        initMarkerPop(mMarkerCc, "王五(40%)");

        OverlayOptions oodd = new MarkerOptions().position(llD).icon(bdDd)
                .zIndex(0);
        mMarkerDd = (Marker) (bdMap.addOverlay(oodd));
        initMarkerPop(mMarkerDd, "赵敏(80%)");


    }

    /**
     * 给覆盖层添加泡泡弹窗
     *
     * @param marker
     * @param message
     */
    private void initMarkerPop(Marker marker, String message) {
        Button button = new Button(getApplicationContext());
        button.setId(marker.getZIndex());
        button.setBackgroundResource(R.drawable.popup_bg_waitdriver);
        button.setTextColor(getResources().getColor(R.color.blue_grey));
        button.setTextSize(12f);
        button.setPadding(0, 0, 0, 0);
        button.setText(" " + message + " ");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toActivity(ReserveActivity.class);
            }
        });
        LatLng ll = marker.getPosition();
        mInfoWindow = new InfoWindow(button, ll, -98);
        bdMap.showInfoWindow(mInfoWindow);
    }


    @Override
    protected void onPause() {
        bdMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        bdMapView.onResume();
        super.onResume();
        initState();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        bdClient.stop();
        // 关闭定位图层
        bdMap.setMyLocationEnabled(false);
        bdMapView.onDestroy();
        bdMapView = null;
        bdA.recycle();
        bdB.recycle();
        bdC.recycle();
        bdD.recycle();
        bdAa.recycle();
        bdBb.recycle();
        bdCc.recycle();
        bdDd.recycle();
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (relaMap.isShown()) {
                    fab.setImageResource(R.drawable.icon_map);
                    appbar.setVisibility(View.VISIBLE);
                    viewpager.setVisibility(View.VISIBLE);
                    relaMap.setVisibility(View.GONE);
                } else {
                    fab.setImageResource(R.drawable.icon_car);
                    appbar.setVisibility(View.GONE);
                    viewpager.setVisibility(View.GONE);
                    relaMap.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_dian://地图，门店
                initOverlay();
                btnDian.setTextColor(getResources().getColor(R.color.dark_grey));
                btnPerson.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.btn_person://地图，个人
                initUserOverlay();
                btnDian.setTextColor(getResources().getColor(R.color.grey));
                btnPerson.setTextColor(getResources().getColor(R.color.dark_grey));
                break;
            case R.id.btn_reserve_pay://支付订金,正在用车
                preferenceUtils.saveParam(CAR_STATE_KEY, CAR_STATE_ING);
                initState();
                break;
            case R.id.btn_reserve_cancel://取消订单，返回主页
                preferenceUtils.saveParam(CAR_STATE_KEY, CAR_STATE_STAR);
                initState();
                break;
            case R.id.btn_end://门店还车，去支付
                preferenceUtils.saveParam(CAR_STATE_KEY, CAR_STATE_PAY);
                initState();
                break;
            case R.id.btn_back://支付成功，返回主页
                preferenceUtils.saveParam(CAR_STATE_KEY, CAR_STATE_STAR);
                initState();
                break;
            case R.id.btn_rent_user://还车个用户
                SnackbarUtils.show(this, R.string.reserve_message);
                break;
            case R.id.btn_charging://查看充电桩
//                SnackbarUtils.show(this, R.string.rent_user_message);
                toActivity(ChargingActivity.class);
                break;
            case R.id.btn_wait_end:
                preferenceUtils.saveParam(CAR_STATE_KEY, CAR_STATE_ING);
                initState();
                break;
            case R.id.edit_note_type:
                toActivity(LoadingActivity.class);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 定位监听，对定位结果进行相关操作
     */
    public class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null) {
                cntLocation = bdLocation;
                LatLng curLocation = new LatLng(cntLocation.getLatitude(), cntLocation.getLongitude());
                LatLng aimLocation = new LatLng(31.293403, 121.452784);//121.452784,31.293403
                double Distance = DistanceUtil.getDistance(curLocation, aimLocation);
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(cntLocation.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(cntLocation.getLatitude())
                        .longitude(cntLocation.getLongitude()).build();
                bdMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(cntLocation.getLatitude(),
                            cntLocation.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    bdMap.animateMapStatus(u);
                }

//                SnackbarUtils.show(MainActivity.this, R.string.app_name);
            } else {
                Toast.makeText(getApplicationContext(), "定位失败，原因：" + bdLocation.getLocType(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化页面内容
     */
    private void initTab() {
        List<String> titles = Arrays.asList(CAR_TYPE);
        tabs.addTab(tabs.newTab().setText(titles.get(0)));
        tabs.addTab(tabs.newTab().setText(titles.get(1)));
        tabs.addTab(tabs.newTab().setText(titles.get(2)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentList());
        fragments.add(new FragmentUsercar());
        FragmentComeCar comeCar=new FragmentComeCar();
        comeCar.setmReserverListener(new FragmentComeCar.ReserveListener() {
            @Override
            public void reserveClick() {
                preferenceUtils.saveParam(CAR_STATE_KEY, CAR_STATE_WAIT);
                initState();
            }
        });
        fragments.add(comeCar);
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(adapter);
        tabs.setupWithViewPager(viewpager);
        tabs.setTabsFromPagerAdapter(adapter);

    }

    /**
     * 抽屉数据初始化
     */
    private void initDrawList() {
        drawListDates = Arrays.asList(DRAW_STR);
        SimpleListAdapter adapter = new DrawerListAdapter(this, drawListDates);
        leftDrawerListview.setAdapter(adapter);
        leftDrawerListview.setItemChecked(mCurrentNoteType, true);
        toolbar.setTitle(drawListDates.get(mCurrentNoteType));
    }

    /**
     * 初始化抽屉
     */
    private void initDrawerView() {
        initDrawList();
        leftDrawerListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                leftDrawerListview.setItemChecked(position, true);
                openOrCloseDrawer();
                mCurrentNoteType = position;
                changeToSelectNoteType(mCurrentNoteType);
//                if (mCurrentNoteType != 0) {
//                    fab.hide();
//                    fab.setVisibility(View.INVISIBLE);
//                } else {
//                    fab.setVisibility(View.VISIBLE);
//                    fab.show();
//                }

            }
        });
        //标题栏监听
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                toolbar.setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                toolbar.setTitle(drawListDates.get(mCurrentNoteType));
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();//箭头动画
        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.setScrimColor(getColor(R.color.drawer_scrim_color));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && drawerLayout.isDrawerOpen(leftDrawer)) {
            drawerLayout.closeDrawer(leftDrawer);
            return true;
        }
        moveTaskToBack(true);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 选择不同抽屉分类后的处理
     *
     * @param type
     */
    private void changeToSelectNoteType(int type) {
        //TODO
        switch (type) {
            case 1://实名
                toActivity(ShimingActivity.class);
                break;
            case 2://我的信息
                break;
            case 3://我的订单
                break;
            case 4://关于我们
                break;
            default:
                break;
        }
    }

    //关闭开启抽屉
    private void openOrCloseDrawer() {
        if (drawerLayout.isDrawerOpen(leftDrawer)) {
            drawerLayout.closeDrawer(leftDrawer);
        } else {
            drawerLayout.openDrawer(leftDrawer);
        }
    }


    //状态栏
    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);

    }

    //进度条控制
    private void showProgressWheel(boolean visible) {
        progressWheel.setBarColor(getColorPrimary());
        if (visible) {
            if (!progressWheel.isSpinning())
                progressWheel.spin();
        } else {
            progressWheel.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressWheel.isSpinning()) {
                        progressWheel.stopSpinning();
                    }
                }
            }, 500);
        }
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
