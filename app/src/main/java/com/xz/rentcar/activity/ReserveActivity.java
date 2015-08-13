package com.xz.rentcar.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.xz.rentcar.R;

import butterknife.InjectView;

/**
 * Created by xaozu on 15/8/5.
 * 预约信息填写
 */
public class ReserveActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.edit_time)
    EditText editText;

    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
        toolbar.setTitle("预约详情");
    }
    @Override
    protected int getLayoutView() {
        return R.layout.activity_reserve;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reserve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:

//                SnackbarUtils.show(this,R.string.reserve_success);
                preferenceUtils.saveParam(MainActivity.CAR_STATE_KEY,MainActivity.CAR_STATE_RESERVE_ING);
                finish();
                return true;
            case android.R.id.home:
//                if (doneMenuItem.isVisible()){
//                    showNotSaveNoteDialog();
//                    return true;
//                }
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
