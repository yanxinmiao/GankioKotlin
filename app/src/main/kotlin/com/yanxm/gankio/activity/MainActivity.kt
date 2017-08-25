package com.yanxm.gankio.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.text.TextUtils
import com.yanxm.gankio.R
import com.yanxm.gankio.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "干货集中营"
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title)//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.inflateMenu(R.menu.base_toolbar_menu)//toolbar 添加menu
        //  添加右上角菜单点击事件
        toolbar.setOnMenuItemClickListener { item ->
            val menuItemId = item.itemId
            if (menuItemId == R.id.action_item1) {
                startActivity<AppSettingAcviity>()
            } else if (menuItemId == R.id.action_item2) {
                startActivity<AboutUsAcvitiy>()
            }
            true
        }
        val searchView = toolbar.findViewById(R.id.action_search) as SearchView
        searchView.queryHint = "输入关键字"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (TextUtils.isEmpty(query)){
                    toast("请输入关键字")
                    return false
                }
                searchView.clearFocus()
                startActivity<SearchActivity>("key" to query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        var fragment: MainFragment = MainFragment()

        supportFragmentManager.beginTransaction()
                .add(R.id.container,fragment)
                .commit()
    }
}
