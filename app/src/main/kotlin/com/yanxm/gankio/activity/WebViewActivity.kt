package com.yanxm.gankio.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.webkit.*
import com.weavey.loading.lib.LoadingLayout
import com.yanxm.gankio.R
import com.yanxm.gankio.bean.ClassifyData
import com.yanxm.gankio.db.ClassifyDbHelper
import com.yanxm.gankio.db.DbUtil
import com.yanxm.gankio.view.SlowlyProgressBar
import kotlinx.android.synthetic.main.activity_web_view.*
import org.jetbrains.anko.toast




class WebViewActivity : AppCompatActivity() {

    val APP_CACHE_DIRNAME = "/webcache" // web缓存目录

    lateinit var slowlyProgressBar: SlowlyProgressBar

    lateinit var mClipboard: ClipboardManager

    lateinit var url: String

    lateinit var data: ClassifyData

    lateinit var dbHelper: ClassifyDbHelper

    lateinit var menuItem: MenuItem

    var isCollect: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        data = intent.getParcelableExtra<ClassifyData>("data")
        if (TextUtils.isEmpty(data.url)){
            finish()
            return
        }

        url = data.url
        dbHelper = DbUtil.getHomeDataHelperHelper()
        mClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        loadinglayout.status = LoadingLayout.Loading
        toolbar.title = "文章详情"
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title)//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.inflateMenu(R.menu.web_toolbar_meun)

        menuItem = toolbar.menu.getItem(0)
        toolbar.setOnMenuItemClickListener{item ->// 如果接口只有一个方法， 可以用lambda
            if (item.itemId == R.id.action_collection){//收藏
                addCollection()
            }else if (item.itemId == R.id.action_share){//分享
                shareText(this@WebViewActivity,data.desc + data.url)
            }else if (item.itemId == R.id.action_item3){//复制链接
                copyLink()
            }
            true
        }
        val list = dbHelper.query("where url = ?" ,url)
        if (list != null && list.size > 0){
            isCollect = true
            menuItem.title = "已收藏"
        }else{
            isCollect = false
        }

        toolbar.setNavigationOnClickListener{finish()}
        val webSettings = webview.settings
        //启用支持javascript
        webSettings.javaScriptEnabled = true
        webSettings.displayZoomControls = false
        //自适应屏幕
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)

        // 开启DOM storage API 功能
        webSettings.domStorageEnabled = (true)
        val cacheDirPath = filesDir.absolutePath + APP_CACHE_DIRNAME
        webSettings.databasePath = (cacheDirPath) // API 19 deprecated
        // 设置Application caches缓存目录
        webSettings.setAppCachePath(cacheDirPath)
        // 开启Application Cache功能
        webSettings.setAppCacheEnabled(true)
        webSettings.useWideViewPort = (true)//设置此属性，可任意比例缩放
        webSettings.loadWithOverviewMode = (true)

        webview.scrollBarStyle = (WebView.SCROLLBARS_INSIDE_OVERLAY)
        webview.isVerticalScrollBarEnabled = (false) //垂直不显示
        webview.loadUrl(url)

        slowlyProgressBar = SlowlyProgressBar(p, getWindowManager().getDefaultDisplay().getWidth()).setViewHeight(3)

        webview.setWebViewClient(object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadinglayout.status = LoadingLayout.Success
            }
        })

        webview.setWebChromeClient(object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                slowlyProgressBar.setProgress(newProgress)
            }
        })
    }

    private fun addCollection() {
        if (isCollect!!){
            dbHelper.delete(data)
            menuItem.title = "收藏"
            isCollect = false
            toast("取消收藏")
        }else{
            dbHelper.save(data)
            menuItem.title = "已收藏"
            isCollect = true
            toast("收藏成功")
        }
    }

    fun shareText(context: Context, extraText: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, data.desc)
        intent.putExtra(Intent.EXTRA_TEXT, extraText)//extraText为文本的内容
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK//为Activity新建一个任务栈
        context.startActivity(
                Intent.createChooser(intent, data.desc))//R.string.action_share同样是标题
    }
    /**
     * 复制链接
     */
    fun copyLink(){
        val myClip = ClipData.newPlainText("text", url)
        mClipboard.primaryClip = myClip
        toast("已复制到剪切板")
    }
}
