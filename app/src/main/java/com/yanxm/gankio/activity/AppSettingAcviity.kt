package com.yanxm.gankio.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.yanxm.gankio.R
import kotlinx.android.synthetic.main.activity_app_setting_acviity.*
import org.jetbrains.anko.startActivity

class AppSettingAcviity : AppCompatActivity() ,View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_setting_acviity)

        toolbar.title = "更多"
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title)//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.setNavigationOnClickListener { finish() }
        tv_collection.setOnClickListener (this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_collection -> startActivity<CollectionActivity>()
        }
    }
}
