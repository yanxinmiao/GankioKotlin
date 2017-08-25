package com.yanxm.gankio.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.weavey.loading.lib.LoadingLayout
import com.yanxm.gankio.BuildConfig
import com.yanxm.gankio.MyApplication
import com.yanxm.gankio.R
import com.yanxm.gankio.activity.PicViewActivity
import com.yanxm.gankio.adapter.PicAdapter
import com.yanxm.gankio.bean.BaseBean
import com.yanxm.gankio.bean.ClassifyData
import com.yanxm.gankio.bean.Result
import com.yanxm.gankio.http.ApiService
import com.yanxm.gankio.http.RetrofitCallback
import com.yanxm.gankio.utils.Constant
import com.yanxm.gankio.utils.RvItemClickListener
import com.yanxm.gankio.view.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_fuli.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by dell on 2017/8/23.
 *
 * 图片 列表
 *
 */
class FuLiFragment : Fragment() ,BaseQuickAdapter.RequestLoadMoreListener ,OnRefreshListener{

    var v: View? = null

    var page = 1

    lateinit var picAdapter: PicAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (v == null){
            v = View.inflate(activity, R.layout.fragment_fuli,null)
        }
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        getData()
    }
    private fun initView() {
        swipeLayout.setOnRefreshListener(this)
        picAdapter = PicAdapter(R.layout.image_item_layout,null)
        recyclerview.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerview.adapter = picAdapter
        val itemDecoration = SpaceItemDecoration(20)
        recyclerview.addItemDecoration( itemDecoration)
        loading.status = LoadingLayout.Loading
        picAdapter.setOnLoadMoreListener(this,recyclerview)

        recyclerview.addOnItemTouchListener(object : RvItemClickListener(){
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                super.onItemClick(adapter, view, position)

                startActivity<PicViewActivity>("url" to picAdapter.data[position].url)
            }
        })
    }

    private fun getData() {

        var retrofit = Retrofit.Builder()
                .baseUrl(Constant.RETROFIT_HTTP_BASE_URL)
                .client(getNewClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getClassifyData("福利",page)
        call.enqueue(object : RetrofitCallback<BaseBean<List<ClassifyData>>>() {
            override fun onSuccess(t: BaseBean<List<ClassifyData>>?) {
                if (swipeLayout.isRefreshing){
                    swipeLayout.finishRefresh()
                }
                if (!t!!.error){
                    var list = t.results
                    if (list == null || list.size == 0){
                        if (page == 1){
                            loading.status = LoadingLayout.Empty
                        }else{
                            picAdapter.loadMoreEnd()
                        }
                        return
                    }
                    //下拉刷新
                    if (page == 1){
                        if (picAdapter.data != null && picAdapter.data.size > 0){
                            picAdapter.data.clear()
                        }
                    }
                    if (list.size < 10){
                        picAdapter.loadMoreEnd()
                    }
                    picAdapter.addData(list)
                    picAdapter.loadMoreComplete()
                    loading.status = LoadingLayout.Success
                    page ++
                }
            }
            override fun onFail(message: String?) {
                if (swipeLayout.isRefreshing){
                    swipeLayout.finishRefresh()
                }
                if (page == 1){
                    loading.status = LoadingLayout.Error
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                if (page == 1){
                    loading.status = LoadingLayout.No_Network
                }
            }
        })
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        getData()
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
}