package com.xz.rentcar.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xz.rentcar.R;

import butterknife.InjectView;

/**
 * Created by xaozu on 15/8/5.
 */
public class LoadingActivity extends BaseActivity implements OnClickListener{
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.btn_register)
    Button btnRegister;
    @InjectView(R.id.btn_login)
    Button btnLogin;


    @Override
    protected void initToolbar() {
        super.initToolbar(toolbar);
        mActionBar.setDisplayHomeAsUpEnabled(false);

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_loading;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                toActivity(RegisterActivity.class);
                break;
            case R.id.btn_login:
                toActivity(LoginActivity.class);
                break;
        }
    }
}
