package com.xz.rentcar.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
import com.xz.rentcar.R;

import butterknife.InjectView;

/**
 * Created by xaozu on 15/8/10.
 * 支付界面
 */
public class ChargingActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.bd_mapView)
    MapView bdMapView;

    private BaiduMap bdMap = null;
    public static BDLocation cntLocation;
    public static LocationClient bdClient;
    private boolean isFirstLoc = true;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private InfoWindow mInfoWindow;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.company_icon_on);
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.drawable.company_icon_on);
    BitmapDescriptor bdC = BitmapDescriptorFactory
            .fromResource(R.drawable.company_icon_on);
    BitmapDescriptor bdD = BitmapDescriptorFactory
            .fromResource(R.drawable.company_icon_on);
    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
        toolbar.setTitle("附近充电桩");
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_charging;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMap();
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
                    initMarkerPop(marker, "充电桩A");
                else if (marker == mMarkerB)
                    initMarkerPop(marker, "闸北彭江店-服务点B");
                else if (marker == mMarkerC)
                    initMarkerPop(marker, "充电桩C");
                else if (marker == mMarkerD)
                    initMarkerPop(marker, "充电桩D");
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

        OverlayOptions ooc = new MarkerOptions().position(llC).icon(bdC)
                .zIndex(0).draggable(true);
        mMarkerC = (Marker) (bdMap.addOverlay(ooc));

        OverlayOptions ood = new MarkerOptions().position(llD).icon(bdD)
                .zIndex(0);
        mMarkerD = (Marker) (bdMap.addOverlay(ood));
        initMarkerPop(mMarkerD, "充电桩D");

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
        mInfoWindow = new InfoWindow(button, ll, -48);
        bdMap.showInfoWindow(mInfoWindow);
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
}

