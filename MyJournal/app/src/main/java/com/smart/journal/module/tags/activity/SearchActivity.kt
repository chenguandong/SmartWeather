package com.smart.journal.module.tags.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringDef
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.commit
import com.blankj.utilcode.util.ToastUtils
import com.orhanobut.logger.Logger
import com.smart.journal.R
import com.smart.journal.base.BaseActivity
import com.smart.journal.module.journal.JournalFragment
import com.smart.journal.module.journal.SearchEable
import com.smart.journal.module.journal.SearchEableType
import com.smart.journal.module.menu.fragment.LocationSearchFragment
import com.smart.journal.module.menu.fragment.LocationSearchFragment.LocationSearchFragmentDelegate
import com.smart.journal.module.tags.bean.TagsDbBean
import com.smart.journal.module.tags.fragments.TagFragment
import com.smart.journal.module.tags.fragments.TagSearchFragment
import com.smart.journal.module.tags.fragments.TagSearchWapperFragment

class SearchActivity : BaseActivity() {
   /* @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @StringDef(TAG_SEARCH, LOCATION_SEARCH)
    annotation class TagActivityType {
        companion object {
            *//*const val TAG_SEARCH = "tag_search"
            const val LOCATION_SEARCH = "location_search"*//*
        }
    }*/

    var mSearchView: SearchView? = null

    var tagSearchFragment: TagSearchWapperFragment? = null

    var tagFragment: TagFragment? = null

    var locationSearchFragment: LocationSearchFragment? = null

    override fun initData() {
    }

    override fun initView() {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tag_activity)
        if (savedInstanceState == null) {

            when (intent.getSerializableExtra(ACTIVITY_TYPE)) {
                SearchEableType.TAG -> {
                    initSimpleToolbar(resources.getString(R.string.tag))
                    tagSearchFragment = TagSearchWapperFragment()
                    supportFragmentManager.commit {
                        replace(R.id.container, tagSearchFragment!!)
                    }
                }
                SearchEableType.LOCATION -> {
                    initSimpleToolbar(resources.getString(R.string.location))
                    locationSearchFragment = LocationSearchFragment.newInstance("", "")
                    locationSearchFragment!!.delegate = object : LocationSearchFragmentDelegate {
                        override fun onItemClick(address: String) {
                            supportFragmentManager.commit {
                                replace(R.id.container, JournalFragment.newInstance(JournalFragment.FRAGMENT_TYPE_SEARCH_LOCATION, address))
                                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            }
                        }
                    }

                    supportFragmentManager.commit {
                        replace(R.id.container, locationSearchFragment!!)
                    }
                }
                else -> {
                    initSimpleToolbar(resources.getString(R.string.tag))
                    tagFragment = TagFragment.newInstance()
                    supportFragmentManager.commit {
                        replace(R.id.container, tagFragment!!)
                    }
                }
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_view, menu)
        val searchItem = menu!!.findItem(R.id.toolbar_search_view)
        mSearchView = searchItem.actionView as SearchView
        mSearchView!!.queryHint = "搜索"
        //进入进入展开模式
        mSearchView!!.isIconified = true
        mSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(s: String?): Boolean {

                when (intent.getSerializableExtra(ACTIVITY_TYPE)) {
                    SearchEableType.TAG -> {
                        tagSearchFragment?.let {
                            it.doSerarch(s!!, SearchEableType.TAG)
                        }
                    }
                    SearchEableType.LOCATION -> {
                        locationSearchFragment?.let {
                            it.searchText(s!!)
                        }
                    }
                    else -> {
                        tagFragment?.let {
                            it.searchText(s!!)
                        }
                    }
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    companion object {
        const val ACTIVITY_TYPE = "activity_type"

        @JvmStatic
        fun startActivity(context: Context, @SearchEableType tagAction: String) {
            context.startActivity(
                    Intent(context, SearchActivity::class.java).putExtra(ACTIVITY_TYPE, tagAction)
            )

        }
    }
}
