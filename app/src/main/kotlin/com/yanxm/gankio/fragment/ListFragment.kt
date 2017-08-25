package com.yanxm.gankio.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.orhanobut.logger.Logger
import com.weavey.loading.lib.LoadingLayout
import com.yanxm.gankio.BuildConfig
import com.yanxm.gankio.MyApplication
import com.yanxm.gankio.R
import com.yanxm.gankio.activity.WebViewActivity
import com.yanxm.gankio.adapter.ClassifyAdapter
import com.yanxm.gankio.bean.BaseBean
import com.yanxm.gankio.bean.ClassifyData
import com.yanxm.gankio.http.RetrofitCallback
import com.yanxm.gankio.http.RetrofitManager
import com.yanxm.gankio.utils.RvItemClickListener
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_list.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import java.util.concurrent.TimeUnit

/**
 * Created by dell on 2017/8/23.
 */
class ListFragment : Fragment() , BaseQuickAdapter.RequestLoadMoreListener , SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        page = 1
        getData()
    }

    var v: View? = null

    lateinit var key: String

//    var retrofit: Retrofit? = null

    lateinit var classifyAdapter: ClassifyAdapter

    var page = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (v == null){
            v = View.inflate(activity, R.layout.fragment_list,null)
        }
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getData()
    }

    private fun initView() {
        key = arguments.getString("key")
        if (TextUtils.isEmpty(key)){
            return
        }
//        retrofit = Retrofit.Builder()
//                .baseUrl(Constant.RETROFIT_HTTP_BASE_URL)
//                .client(getNewClient())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()

        loadinglayout.status = LoadingLayout.Loading
        classifyAdapter = ClassifyAdapter(R.layout.classify_item_layout,null)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = classifyAdapter
        recyclerview.addItemDecoration( HorizontalDividerItemDecoration.Builder(activity)
                .color(resources.getColor(R.color.color_f5f5f5))
                .sizeResId(R.dimen.margin_10)
                .build())
        classifyAdapter.setOnLoadMoreListener(this,recyclerview)
        recyclerview.addOnItemTouchListener(object : RvItemClickListener(){
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

                startActivity<WebViewActivity>("data" to classifyAdapter.data[position])
            }

            override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                super.onItemLongClick(adapter, view, position)

            }
        })
        swipeLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipeLayout.setOnRefreshListener(this)
    }
    var call: Call<BaseBean<MutableList<ClassifyData>>>? = null
    private fun getData() {

        val apiService = RetrofitManager.createService()

        call = apiService?.getClassifyData(key,page)

        call?.enqueue(object : RetrofitCallback<BaseBean<List<ClassifyData>>>() {
            override fun onSuccess(bean: BaseBean<List<ClassifyData>>) {
                Logger.d("onSuccess")
                Logger.d("onSuccess" + bean.toString())
                if (swipeLayout.isRefreshing){
                    swipeLayout.isRefreshing = false
                }
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
                Logger.d("onFail")
                if (swipeLayout.isRefreshing){
                    swipeLayout.isRefreshing = false
                }
                if (page == 1){
                    loadinglayout.status = LoadingLayout.Error
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                if (page == 1){
                    loadinglayout.status = LoadingLayout.No_Network
                }
            }
        })
    }

    override fun onLoadMoreRequested() {
        getData()
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

    override fun onDestroy() {
        super.onDestroy()
        if (call != null){
            call?.cancel()
        }
    }

}