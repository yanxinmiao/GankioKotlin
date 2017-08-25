package com.yanxm.gankio.activity

import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.weavey.loading.lib.LoadingLayout
import com.yanxm.gankio.R
import com.yanxm.gankio.adapter.CollectAdapter
import com.yanxm.gankio.bean.ClassifyData
import com.yanxm.gankio.db.ClassifyDbHelper
import com.yanxm.gankio.db.DbUtil
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_collection.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 我的收藏
 */
class CollectionActivity : AppCompatActivity() {

    lateinit var dbHelper: ClassifyDbHelper

    lateinit var collectAdapter: CollectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        toolbar.title = "我的收藏"
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title)//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.inflateMenu(R.menu.collect_meun)
        toolbar.setNavigationOnClickListener { finish() }

        toolbar.setOnMenuItemClickListener { item ->

            if (item.itemId == R.id.action_clear){
                doAsync {
                    dbHelper.deleteAll()
                    uiThread {
                        loadinglayout.status = LoadingLayout.Empty
                    }
                }
            }
            true
        }
        dbHelper = DbUtil.getHomeDataHelperHelper()
        loadinglayout.status = LoadingLayout.Loading

        collectAdapter = CollectAdapter(R.layout.classify_item_layout,null)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = collectAdapter
        recyclerview.addItemDecoration( HorizontalDividerItemDecoration.Builder(this)
                .color(resources.getColor(R.color.color_f5f5f5))
                .sizeResId(R.dimen.margin_10)
                .build())

        collectAdapter.enableSwipeItem()
        collectAdapter.setOnItemSwipeListener(object : OnItemSwipeListener{
            override fun clearView(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                collectAdapter.remove(pos)
            }

            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSwipeMoving(canvas: Canvas?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, isCurrentlyActive: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
        var ls = dbHelper.queryAll() as MutableCollection<ClassifyData>
        if (ls == null || ls.size == 0){
            loadinglayout.status = LoadingLayout.Empty
            return
        }
        collectAdapter.addData(ls)
        loadinglayout.status = LoadingLayout.Success
    }
}
