package android.com.msx7.josn.ruibo_mediacenter.activity;

import android.app.Activity;
import android.com.msx7.josn.ruibo_mediacenter.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 文件名: MainActivity
 * 描  述:
 * 作  者：Josn
 * 时  间：2016/2/16
 */
public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),LoginManager.class));
            }
        });
    }
}
