package xunqaing.bwie.com.permissionsdisdispatcher60;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// 适配6.0动态检测  》=23 现在是 targetSdkVersion 25   960521

public class MainActivity extends Activity {

    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.bt1, R.id.bt2})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt1:

                //        checkSelfPermission 检测有没有 权限
                //        PackageManager.PERMISSION_GRANTED 有权限
                //        PackageManager.PERMISSION_DENIED  拒绝权限

                //判断，如果没有请求授权 就去请求授权
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                    //权限发生了改变 true  //  false 小米
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {


                        new AlertDialog.Builder(this).setTitle("是否请求授权")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 请求授权
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);

                                    }
                                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();


                    } else {

                        //请求权限
                        /**上下文
                         * 请求的数组（可多个）
                         * 结果码  onRequestPermissionsResult 请求回调中用
                         */
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);

                    }

                    //授权了直接打来相机
                } else {

                    camer();

                }
                break;

            case R.id.bt2:

                Intent i = new Intent(MainActivity.this,PermissionsDispatcherActivity.class);
                startActivity(i);

                break;
        }
    }

    //请求回调

    /**
     * @param requestCode
     * @param permissions  请求的权限
     * @param grantResults 请求权限返回的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 判断请求码 如果是1 ，则是 camear 权限回调
        if (requestCode == 1) {

            // 表示用户授权
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // 表示用户授权
                Toast.makeText(this, " user Permission", Toast.LENGTH_SHORT).show();

                camer();


                //用户拒绝权限
            } else {
                Toast.makeText(this, " no Permission", Toast.LENGTH_SHORT).show();
            }


        }

    }


    //调起系统相机
    public void camer() {


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,1);

//                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivityForResult(i, Activity.DEFAULT_KEYS_DIALER);
    }

}
