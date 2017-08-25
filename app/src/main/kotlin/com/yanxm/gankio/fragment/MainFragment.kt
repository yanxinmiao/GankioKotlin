package com.yanxm.gankio.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yanxm.gankio.R
import com.yanxm.gankio.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by dell on 2017/8/23.
 */
class MainFragment : Fragment(){

    var v: View? = null
    lateinit var pagerApdapter: PagerAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (v == null){
            v = View.inflate(activity, R.layout.fragment_main,null)
        }
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView(){
        var ls = mutableListOf<String>()
        ls?.add("Android")
        ls.add("iOS")
        ls.add("休息视频")
        ls.add("拓展资源")
        ls.add("前端")
        ls.add("福利")
        var fragments = mutableListOf<Fragment>()
        for (i in 0..4) {
            val fragment: ListFragment = ListFragment()
            val bundle: Bundle = Bundle()
            bundle.putString("key",ls[i])
            fragment.arguments = bundle
            fragments.add(fragment)
        }
        val fragment = FuLiFragment()
        val bundle: Bundle = Bundle()
        bundle.putString("key","福利")
        fragment.arguments = bundle
        fragments.add(fragment)

        pagerApdapter = ViewPagerAdapter(fragmentManager,fragments, ls)
        if (viewpager == null){
            return
        }
        viewpager.adapter = pagerApdapter
        tabs.setViewPager(viewpager)
        tabs.textSize = 42
        tabs.textColor = resources.getColor(R.color.white)

    }
}