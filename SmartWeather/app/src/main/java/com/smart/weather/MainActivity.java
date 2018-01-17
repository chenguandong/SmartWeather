package com.smart.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.smart.weather.base.BaseActivity;
import com.smart.weather.fragment.WeatherFragment;
import com.smart.weather.module.journal.JournalFragment;
import com.smart.weather.module.map.MapFragment;
import com.smart.weather.module.mine.MineFragment;
import com.smart.weather.tools.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author guandongchen
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.navigation_view)
    BottomNavigationView navigationView;


    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Fragment journalFragment;
    private Fragment mapFragment;
    private Fragment mineFragment;
    private Fragment weatherFragment;

    private String[] titles = new String[]{"日记","地图","天气","我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initSimpleToolbarWithNoBack(titles[0]);
        initView();
        initData();
    }

    @Override
    protected void initView() {

        BottomNavigationViewHelper.disableShiftMode(navigationView);

        journalFragment = JournalFragment.newInstance("","");

        mapFragment = MapFragment.newInstance("","");

        mineFragment = MineFragment.newInstance("","0");

        weatherFragment = WeatherFragment.newInstance("","");

        fragmentList.add(journalFragment);
        fragmentList.add(mapFragment);
        fragmentList.add(weatherFragment);
        fragmentList.add(mineFragment);

        viewPager.setOffscreenPageLimit(fragmentList.size());

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

        };

        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                navigationView.getMenu().getItem(position).setChecked(true);

                setTitle(titles[position]);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                String title = "";
                switch (item.getItemId()) {

                    case R.id.item1:
                        viewPager.setCurrentItem(0,false);
                        title = titles[0];
                        break;
                    case R.id.item2:
                        viewPager.setCurrentItem(1,false);
                        title = titles[1];
                        break;
                    case R.id.item3:
                        viewPager.setCurrentItem(2,false);
                        title = titles[2];
                        break;
                    case R.id.item4:
                        viewPager.setCurrentItem(3,false);
                        title = titles[3];
                        break;


                }
                setToolbarTitle(title);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
