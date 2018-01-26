package com.smart.weather.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.smart.weather.R;

/**
 * @author guandongchen
 * @date 2018/1/16
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Context context;
    protected Toolbar toolbar;
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

    }

    /**
     * 设置普通带back 的导航栏
     * @param titleString 名称
     */
    protected void initSimpleToolbar(String titleString){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(titleString);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowTitleEnabled(false);
        }

        //toolbar.setNavigationIcon(ContextCompat.getDrawable(context, R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected  void  initSimpleToolbarWithNoBack(String titleString){

        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle(titleString);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null){
           // actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    protected void setToolbarTitle(String titleString){
        toolbar.setTitle(titleString);
    }

    protected void showToolbar(){

        if (toolbar!=null){
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    protected void hiddenToolbar(){

        if (toolbar!=null){
            toolbar.setVisibility(View.GONE);
        }
    }


    protected abstract void initView();
    protected abstract void initData();

    protected void init(){
        initView();
        initData();
    }
}