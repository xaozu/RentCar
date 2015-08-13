package com.xz.rentcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xz.rentcar.R;

import butterknife.InjectView;

/**
 * Created by xaozu on 15/8/5.
 */
public class RegisterActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
        toolbar.setTitle("注册");

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_register;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                startActivity(new Intent(this,MainActivity.class));
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
