package com.yanxm.gankio.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by dell on 2017/8/23.
 */
class ViewPagerAdapter(fm: FragmentManager, val list: List<Fragment>,  val titles: MutableList<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}
