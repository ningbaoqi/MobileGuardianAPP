package ningbaoqi.com.locationdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

/**
 * 获取经纬度
 * <p>
 * 一、网络定位：根据IP地址进行定位，根据IP地址和实际地址映射数据库查询 缺点：IP地址动态分配会导致不准确，定位范围太大
 * 二、基站定位：定位范围几百米到几公里不等，范围比较广
 * 三、GPS定位：美国的卫星定位系统，GPS定位不需要网络，定位精度准确几米到几十米不等；缺点：容易收到云层、建筑等干扰
 * <p>
 * Android手机采用A-GPS类型定位，网络和GPS共同定位 范围几米到几十米
 */
public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private MyLocationListener locationListener;
    private double longitude;
    private double latitude;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        /**
         * passive:由LocationManager.PASSIVE_PROVIDER常量表示
         * gps:由LocationManager.GPS_PROVIDER常量表示，代表通过GPS获取定位信息的LocationProvider对象
         * network:由LocationManager.NETWORK_PROVIDER常量表示，代表通过移动通信网络获取定位信息的LocationProvider对象
         * */
        List<String> allProvider = locationManager.getAllProviders();
        for (String s : allProvider) {
            Log.d("nbq", s);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    class MyLocationListener implements LocationListener {
        /**
         * 位置发生变化时回调
         */
        @Override
        public void onLocationChanged(Location location) {
            /**
             * 获取精度
             * */
            longitude = location.getLongitude();

            /**
             * 获取纬度
             * */
            latitude = location.getLatitude();
            /**
             * 获取精度
             * */
            long accuracy = (long) location.getAccuracy();

            /**
             * 获取海拔
             * */
            long altitude = (long) location.getAltitude();
            textView.setText(longitude + ";" + latitude + ";" + accuracy + ";" + altitude);
        }

        /**
         * 状态改变时回调
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**
         * 当用户打开时回调
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 当用户关闭时回调
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    /**
     * 因为启动监听底层会调用服务，所以需要手动的删除监听
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }

    /**
     * 火星坐标转换
     * */
    private void change() throws Exception {
        ModifyOffset instance = ModifyOffset.getInstance(ModifyOffset.class.getResourceAsStream("axisoffset.dat"));
        PointDouble pointDouble = instance.s2c(new PointDouble(longitude, latitude));//获得转换后的火星坐标
    }
}
