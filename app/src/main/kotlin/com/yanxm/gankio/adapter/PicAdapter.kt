package com.yanxm.gankio.adapter

import android.support.annotation.LayoutRes
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yanxm.gankio.R
import com.yanxm.gankio.bean.ClassifyData
import com.yanxm.gankio.utils.ImageUtils

/**
 * Created by dell on 2017/8/23.
 */
class PicAdapter (@LayoutRes layoutResId: Int, data: List<ClassifyData>?) : BaseQuickAdapter<ClassifyData, BaseViewHolder>(layoutResId, data){

    override fun convert(helper: BaseViewHolder?, item: ClassifyData?) {
        val image = helper?.getConvertView()?.findViewById(R.id.iv_image) as ImageView

        ImageUtils.loadImage(item?.url,image)
    }
}