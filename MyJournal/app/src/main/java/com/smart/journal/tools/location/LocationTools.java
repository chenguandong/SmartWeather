package com.smart.journal.tools.location;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.smart.journal.app.MyApp;
import com.smart.journal.tools.location.bean.LocationBean;
import com.smart.journal.tools.logs.LogTools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author guandongchen
 * @date 2018/1/5
 */
public class LocationTools {
  private static final LocationTools ourInstance = new LocationTools();
  //声明AMapLocationClient类对象
  private static AMapLocationClient mLocationClient = null;
  //声明AMapLocationClientOption对象
  private static AMapLocationClientOption mLocationOption = null;

  private LocationTools() {
  }

  private static LocationBean locationBean;

  public static LocationTools getInstance() {
    //初始化定位
    if (mLocationClient == null) {
      mLocationClient = new AMapLocationClient(MyApp.Companion.getInstance());
    }
    if (locationBean == null) {
      locationBean = new LocationBean();
    }
    //设置定位回调监听
    mLocationClient.setLocationListener(new AMapLocationListener() {
      @Override
      public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
          if (amapLocation.getErrorCode() == 0) {
            //定位成功回调信息，设置相关消息
            amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
            amapLocation.getLatitude();//获取纬度
            amapLocation.getLongitude();//获取经度
            amapLocation.getAccuracy();//获取精度信息
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(amapLocation.getTime());
            df.format(date);//定位时间
            amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            amapLocation.getCountry();//国家信息
            amapLocation.getProvince();//省信息
            amapLocation.getCity();//城市信息
            amapLocation.getDistrict();//城区信息
            amapLocation.getStreet();//街道信息
            amapLocation.getStreetNum();//街道门牌号信息
            amapLocation.getCityCode();//城市编码
            amapLocation.getAdCode();//地区编码
            amapLocation.getPoiName();//获取当前定位点的AOI信息

            //LogTools.d(amapLocation.toStr());

            locationBean.setAdCode(amapLocation.getAdCode());
            locationBean.setLatitude(amapLocation.getLatitude());
            locationBean.setLongitude(amapLocation.getLongitude());
            locationBean.setAdress(amapLocation.getAddress());
          } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Log.e("AmapError",
                "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
          }
        }
      }
    });

    if (mLocationOption == null) {
      mLocationOption = new AMapLocationClientOption();
    }

    /**
     * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
     */
    mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
    //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

    //获取最近3s内精度最高的一次定位结果：
    //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)
      // 接口也会被设置为true，反之不会，默认为false。
    mLocationOption.setOnceLocationLatest(true);
    //设置是否返回地址信息（默认返回地址信息）
    mLocationOption.setNeedAddress(true);
    if (null != mLocationClient) {
      mLocationClient.setLocationOption(mLocationOption);
      //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
      mLocationClient.stopLocation();
      mLocationClient.startLocation();
    }

    return ourInstance;
  }

  public static LocationBean getLocationBean() {
    return locationBean;
  }
}
