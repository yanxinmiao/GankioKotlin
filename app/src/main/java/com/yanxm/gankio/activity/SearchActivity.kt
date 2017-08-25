package com.yanxm.gankio.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.weavey.loading.lib.LoadingLayout
import com.yanxm.gankio.BuildConfig
import com.yanxm.gankio.MyApplication
import com.yanxm.gankio.R
import com.yanxm.gankio.adapter.ClassifyAdapter
import com.yanxm.gankio.bean.BaseBean
import com.yanxm.gankio.bean.ClassifyData
import com.yanxm.gankio.http.ApiService
import com.yanxm.gankio.http.RetrofitCallback
import com.yanxm.gankio.utils.Constant
import com.yanxm.gankio.utils.RvItemClickListener
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 搜素
 */
class SearchActivity : AppCompatActivity() , BaseQuickAdapter.RequestLoadMoreListener{
    override fun onLoadMoreRequested() {
        getData()
    }

    var page = 1

    lateinit var key: String

    lateinit var retrofit: Retrofit

    lateinit var classifyAdapter: ClassifyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()

        getData()
    }

    private fun initView() {
        key = intent.getStringExtra("key")

        retrofit = Retrofit.Builder()
                .baseUrl(Constant.RETROFIT_HTTP_BASE_URL)
                .client(getNewClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        toolbar.title = "搜索"
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title)//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.setNavigationOnClickListener{finish()}

        loadinglayout.status = LoadingLayout.Loading
        classifyAdapter = ClassifyAdapter(R.layout.classify_item_layout, null)
        classifyAdapter.setShow(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addItemDecoration(HorizontalDividerItemDecoration.Builder(this)
                .color(resources.getColor(R.color.color_f5f5f5))
                .sizeResId(R.dimen.margin_10)
                .build())
        recyclerview.adapter = classifyAdapter
        recyclerview.addOnItemTouchListener(object : RvItemClickListener() {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

                startActivity<WebViewActivity>("data" to classifyAdapter.data[position] )
            }
        })
        classifyAdapter.setOnLoadMoreListener(this,recyclerview)
    }

    var call: Call<BaseBean<MutableList<ClassifyData>>>? = null

    private fun getData() {

        val apiService = retrofit?.create(ApiService::class.java)

        call = apiService?.search(key,page)

        call?.enqueue(object : RetrofitCallback<BaseBean<List<ClassifyData>>>() {
            override fun onSuccess(bean: BaseBean<List<ClassifyData>>) {
                if (!bean.error){
                    var list = bean.results
                    if (list == null || list.size == 0){
                        if (page == 1){
                            loadinglayout.status = LoadingLayout.Empty
                        }else{
                            classifyAdapter.loadMoreEnd()
                        }
                        return
                    }
                    //下拉刷新
                    if (page == 1){
                        if (classifyAdapter.data != null && classifyAdapter.data.size > 0){
                            classifyAdapter.data.clear()
                        }
                    }
                    if (list.size < 10){
                        classifyAdapter.loadMoreEnd()
                    }
                    classifyAdapter.addData(list)
                    classifyAdapter.loadMoreComplete()
                    loadinglayout.status = LoadingLayout.Success
                    page ++
                }
            }
            override fun onFail(message: String) {
                if (page == 1){
                    loadinglayout.status = LoadingLayout.Error
                }
            }
            override fun onNetworkError() {
                super.onNetworkError()
                toast("请检查网络连接")
                if (page == 1){
                    loadinglayout.status = LoadingLayout.No_Network
                }
            }
        })
    }

    private fun getNewClient(): OkHttpClient {
        /**
         * HttpLoggingInterceptor提供了4中控制打印信息类型的等级，分别是NONE，BASIC，HEADERS，BODY，接下来分别来说一下相应的打印信息类型。

         * NONE  没有任何日志信息
         * Basic  打印请求类型，URL，请求体大小，返回值状态以及返回值的大小
         * Headers 打印返回请求和返回值的头部信息，请求类型，URL以及返回值状态码
         * Body  打印请求和返回值的头部和body信息

         */
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(MyApplication.getApplication().cacheDir, cacheSize.toLong())
        val builder = OkHttpClient.Builder()
                .addInterceptor(logging)
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(StethoInterceptor())
        }
        return builder.build()
    }
}
