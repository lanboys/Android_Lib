package com.bing.lan.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int BASE_PERMISSION_REQUEST_CODE = 8910;
    private TextView mTextMessage;
    private Intent mIntent;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    callPhone("110");

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
    private AlertDialog mPermissionsTipAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    // 拨打电话
    public void callPhone(String phone) {
        mIntent = new Intent(Intent.ACTION_CALL);
        Uri data1 = Uri.parse("tel:" + phone);
        mIntent.setData(data1);

        // 检查manifest中是否申请，无实际意义
        // manifest有申请就一定是 true  所以没必要申请运行时，系统会自动弹窗
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsImpl(BASE_PERMISSION_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE});

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startActivity(mIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        List<String> failed = new ArrayList<>();
        List<String> success = new ArrayList<>();

        // 检查权限请求结果
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                failed.add(permissions[i]);
            } else {
                success.add(permissions[i]);
            }
        }

        // 报告每个请求失败的权限
        if (failed.isEmpty()) {
            startActivity(mIntent);
        } else {
            checkDeniedPermissions(requestCode, permissions);
        }
    }

    /*  检查是否有被拒绝过的权限 */
    private void checkDeniedPermissions(int requestCode, final String[] permissions) {
        List<String> shouldShowPermission = new ArrayList<>();
        List<String> notShowPermission = new ArrayList<>();
        for (String permission : permissions) {

            // false: 1.没有被拒绝过(第一次申请)
            //        2.被拒绝,并且在权限申请对话框中设置了,不再弹窗
            // true: 1.被拒绝过,弹窗向用户解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                shouldShowPermission.add(permission);
            } else {
                notShowPermission.add(permission);
            }
        }
        if (shouldShowPermission.size() > 0) {
            //被拒绝过需要解释
            if (requestCode == BASE_PERMISSION_REQUEST_CODE) {
                showRationaleDialog(requestCode, permissions, true);
            } else {
                showRationaleDialog(requestCode, permissions, false);
            }
        } else {
            //未被拒绝(第一次申请)
            //永久拒绝(不弹窗了)
            // requestPermissionsImpl(requestCode, permissions);
            Object o = new Object();
        }
    }

    /* 真正请求权限的操作 */
    private void requestPermissionsImpl(final int requestCode, final String[] permissions) {
        //请求权限
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //弹系统请求权限对话框,只弹出没有勾选 不再显示 的对话框
                ActivityCompat.requestPermissions(MainActivity.this, permissions, requestCode);
            }
        });
    }    /* 解释弹窗 */

    private void showRationaleDialog(final int requestCode, final String[] permissions, final boolean needFinish) {

        mPermissionsTipAlertDialog = new AlertDialog.Builder(this)
                .setTitle("授权温馨提示:")
                .setMessage("亲爱的用户,您好,麻烦您授权一下,不然无法进行下一步操作,谢谢您的配合")
                .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPermissionsTipAlertDialog.dismiss();
                        //真正请求权限
                        requestPermissionsImpl(requestCode, permissions);
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //请求权限失败
                        mPermissionsTipAlertDialog.dismiss();
                        //拒绝直接关闭
                        if (needFinish) {
                            finish();
                        }
                    }
                })
                .create();
        mPermissionsTipAlertDialog.setCanceledOnTouchOutside(false);
        mPermissionsTipAlertDialog.show();
    }
}
