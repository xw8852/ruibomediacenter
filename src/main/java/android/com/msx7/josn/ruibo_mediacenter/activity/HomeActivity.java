package android.com.msx7.josn.ruibo_mediacenter.activity;

import android.app.Activity;
import android.com.msx7.josn.ruibo_mediacenter.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.msx7.lib.annotations.Inject;
import com.msx7.lib.annotations.InjectActivity;
import com.msx7.lib.annotations.InjectView;

/**
 * 文件名: HomeActivity
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/20
 */
@InjectActivity(R.layout.activity_home)
public class HomeActivity extends Activity {
    /**
     * 跳转至管理中心
     */
    @InjectView(R.id.toAdmin)
    View toAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Inject.inject(this);

        toAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 未登录默认显示
     */
    @InjectView(R.id.loginbg)
    View mLoginView;

    /**
     * 用户信息根节点
     */
    @InjectView(R.id.loginRoot)
    View mLoginRootView;
    /**
     * 登出
     */
    @InjectView(R.id.loginOut)
    View mLoginOutBtn;

    /**
     * 重设密码
     */
    @InjectView(R.id.resetPasswd)
    View mResetPasswdBtn;

    /**
     * 交易记录
     */
    @InjectView(R.id.record)
    View mRecordBtn;
}
