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
    @InjectView(R.id.toAdmin)
    View toAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Inject.inject(this);
        setContentView(R.layout.activity_home);
        toAdmin = findViewById(R.id.toAdmin);
        toAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
