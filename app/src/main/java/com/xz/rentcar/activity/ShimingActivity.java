package com.xz.rentcar.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xz.rentcar.R;
import com.xz.rentcar.util.SnackbarUtils;

import butterknife.InjectView;

/**
 * Created by xaozu on 15/8/5.
 */
public class ShimingActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.edit_name)
    MaterialEditText editName;
    @InjectView(R.id.edit_card)
    MaterialEditText editCard;
    @InjectView(R.id.edit_img)
    MaterialEditText editImg;
    @InjectView(R.id.btn_selsect_img)
    Button btnSelsectImg;
    @InjectView(R.id.img_card)
    ImageView imgCard;

    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
        toolbar.setTitle("实名认证");
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_shiming;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                SnackbarUtils.show(this, R.string.reserve_success);
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
