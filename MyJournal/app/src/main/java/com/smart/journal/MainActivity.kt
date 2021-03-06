package com.smart.journal

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.smart.journal.base.BaseActivity
import com.smart.journal.customview.dialog.PatternLockDialogFragment
import com.smart.journal.db.entity.NoteBookDBBean
import com.smart.journal.module.calendar.Calendar2Fragment
import com.smart.journal.module.journal.JournalFragment
import com.smart.journal.module.journal.SearchEableType
import com.smart.journal.module.journal.activity.GlobalSearchActivity
import com.smart.journal.module.map.MapFragment
import com.smart.journal.module.menu.SlideMenuFragment
import com.smart.journal.module.mine.MineFragment
import com.smart.journal.module.mine.setting.SettingActivity
import com.smart.journal.module.photos.PhotosFragment
import com.smart.journal.module.weather.WeatherFragment
import com.smart.journal.module.write.activity.WriteActivity
import com.smart.journal.tools.PermissionTools
import com.smart.journal.tools.file.MJFileTools
import com.smart.journal.tools.location.LocationTools
import com.smart.journal.tools.user.UserTools
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_center.*
import java.util.*

/**
 * @author guandongchen
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var fragmentPagerAdapter: FragmentPagerAdapter? = null
    private val fragmentList = ArrayList<Fragment>()
    private var journalFragment: Fragment? = null
    private var mapFragment: Fragment? = null
    private var mineFragment: Fragment? = null
    private var weatherFragment: Fragment? = null
    private var calendarFragment: Fragment? = null
    private var photoFragment: Fragment? = null

    private val titles = arrayOf("日记", "图片", "地图", "日历")

    var slideMenuFragment: SlideMenuFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSimpleToolbarWithNoBack(titles[0])
        init()
        MJFileTools.createJournalPath()
        slideMenuFragment = SlideMenuFragment.newInstance("", "")
        slideMenuFragment!!.slideMenuFragmentCallBack= object : SlideMenuFragment.SlideMenuFragmentCallBack{
            override fun onNoteBookItemClick(notebook: NoteBookDBBean) {
                setToolbarTitle(notebook!!.name)
                notebook!!.name?.let {
                    (journalFragment as JournalFragment).doSerarch(it,SearchEableType.NOTE_BOOK)
                }
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            override fun onFavouriteItemClick() {
                setToolbarTitle(resources.getString(R.string.favourite))
                (journalFragment as JournalFragment).doSerarch("",SearchEableType.FAVOURITE)
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.menuFragment, slideMenuFragment!!).commit()

        if (!TextUtils.isEmpty(UserTools.lockCode)) {
            PatternLockDialogFragment.newInstance("", "").show(supportFragmentManager, "")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu!!.findItem(R.id.toolbar_right_action)
        searchView.setIcon(R.drawable.ic_search)
        return super.onCreateOptionsMenu(menu)
    }

    override fun initView() {

        journalFragment = JournalFragment.newInstance("", "")

        mapFragment = MapFragment.newInstance()
        //mapFragment = BaiduMapFragment.newInstance("","")

        mineFragment = MineFragment.newInstance()

        weatherFragment = WeatherFragment.newInstance()

        calendarFragment = Calendar2Fragment.newInstance()

        photoFragment = PhotosFragment.newInstance("", "")

        fragmentList.add(journalFragment as JournalFragment)
        fragmentList.add((photoFragment as PhotosFragment?)!!)
        fragmentList.add((mapFragment as MapFragment?)!!)
        fragmentList.add(calendarFragment as Calendar2Fragment)
        //fragmentList.add((weatherFragment as WeatherFragment?)!!)

        viewPager!!.offscreenPageLimit = fragmentList.size

        navigationView.setOnNavigationItemSelectedListener(object :BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId){
                    R.id.item_journal-> viewPager.setCurrentItem(0,false)
                    R.id.item_photos-> viewPager.setCurrentItem(1,false)
                    R.id.item_map-> viewPager.setCurrentItem(2,false)
                    R.id.item_calendar-> viewPager.setCurrentItem(3,false)
                }
                return true
            }

        })

        fragmentPagerAdapter = object : androidx.fragment.app.FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): androidx.fragment.app.Fragment {

                return fragmentList[position]
            }

            override fun getCount(): Int {
                return fragmentList.size
            }

        }

        viewPager!!.adapter = fragmentPagerAdapter

        viewPager!!.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {


            }

            override fun onPageSelected(position: Int) {
               // navigationView!!.menu.getItem(position).isChecked = true
                title = titles[position]
                toolbar.title = title
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        nav_view.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        initPermission()


        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, WriteActivity::class.java))
        }

        LocationTools.instance

        settingButton.setOnClickListener { startActivity(Intent(context, SettingActivity::class.java)) }
    }

    private fun initPermission() {
        PermissionTools.checkPermission(this@MainActivity, PermissionTools.PermissionType.PERMISSION_TYPE_STORAGE, object : PermissionTools.PermissionCallBack {
            override fun permissionYES() {
                PermissionTools.checkPermission(this@MainActivity, PermissionTools.PermissionType.PERMISSION_TYPE_LOCATION, object : PermissionTools.PermissionCallBack {
                    override fun permissionYES() {

                    }

                    override fun permissionNO() {

                    }
                })
            }

            override fun permissionNO() {

            }
        })
    }

    override fun initData() {
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.toolbar_right_action) {
            GlobalSearchActivity.startActivity(this)
            true
        } else super.onOptionsItemSelected(item)

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id: Int = item.itemId

        when (id) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {// 导入导出数据

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
