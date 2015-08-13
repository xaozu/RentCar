package com.xz.rentcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xz.rentcar.R;

import butterknife.InjectView;

/**
 * Created by xaozu on 15/8/5.
 */
public class LoginActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.edit_star_time)
    MaterialEditText editStarTime;
    @InjectView(R.id.edit_time)
    MaterialEditText editTime;


    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
        toolbar.setTitle("登录");
//        if(toolbar!=null)
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_login;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                startActivity(new Intent(this, MainActivity.class));
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
