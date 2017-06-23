package xunqaing.bwie.com.permissionsdisdispatcher60;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


//https://github.com/hotchemi/PermissionsDispatcher

//// 框架
//compile("com.github.hotchemi:permissionsdispatcher:2.4.0") {
//        // if you don't use android.app.Fragment you can exclude support for them
//        exclude module: "support-v13"
//        }
//        annotationProcessor 'com.github.hotchemi:permissionsdispatcher-processor:2.4.0'


//  1.--- @RuntimePermissions 是必须的注册当前activity或fragment
@RuntimePermissions
public class PermissionsDispatcherActivity extends Activity {

    @BindView(R.id.bt2)
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_dispatcher);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.bt2)
    public void onViewClicked() {

        //加完12345，先运行一下，他会在当前类上出现一个类PermissionsDispatcher 调用2 的 camer 方法

        PermissionsDispatcherActivityPermissionsDispatcher.camerWithCheck(this);

    }


    //2. --- 该方法需要哪些权限，当用户授予了权限之后，会调用使用此注解
    @NeedsPermission(Manifest.permission.CAMERA)
    //调起系统相机
    public void camer() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);

        //                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
        //                startActivityForResult(i, Activity.DEFAULT_KEYS_DIALER);
    }

    //3. --- 如果用户不授予某权限时调用
    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void onDenied() {

        Toast.makeText(this, "onDenied --- 用户不授予某权限", Toast.LENGTH_SHORT).show();
    }


    //4.--- 如果用户选择了让设备“不再询问”
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void onNeverAskAgain() {
        Toast.makeText(this, "onNeverAskAgain --- 用户选择了让设备“不再询问", Toast.LENGTH_SHORT).show();

        //应用信息界面
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    //5.--- 注释一个方法解释为什么需要这个(个)权限并提示用户判断是否允许
    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForCamera(final PermissionRequest request) {

        new AlertDialog.Builder(this).setTitle("是否请求授权")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求授权
                        request.proceed();

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();
            }
        }).create().show();

    }

    //请求授权之后的回调方法

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionsDispatcherActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);

    }


}
