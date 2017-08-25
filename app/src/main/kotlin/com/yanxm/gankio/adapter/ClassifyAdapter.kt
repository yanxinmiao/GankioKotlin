package com.yanxm.gankio.adapter

import android.support.annotation.LayoutRes
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yanxm.gankio.R
import com.yanxm.gankio.bean.ClassifyData
import com.yanxm.gankio.bean.Result
import com.yanxm.gankio.utils.ImageUtils

/**
 * Created by dell on 2017/8/23.
 */
class ClassifyAdapter(@LayoutRes layoutResId: Int, data: List<ClassifyData>?) : BaseQuickAdapter<ClassifyData, BaseViewHolder>(layoutResId, data) {

    var isShow:Boolean? = false

    override fun convert(helper: BaseViewHolder, item: ClassifyData) {
        helper.setText(R.id.tv_desc,item.desc)
        helper.setText(R.id.tv_time,item.publishedAt)
        if (isShow!!){
            helper.setVisible(R.id.tv_type,true)
            helper.setText(R.id.tv_type,item.type)
        }else{
            helper.setVisible(R.id.tv_type,false)
        }
        if (item.images != null && item.images.size > 0){
            helper.setVisible(R.id.iv_cover,true)
            var ivCover = helper.getConvertView().findViewById(R.id.iv_cover) as ImageView
            ImageUtils.loadImage(item.images[0],ivCover)

        }else{
            helper.setVisible(R.id.iv_cover,false)
        }
    }
    fun setShow(show: Boolean){
        this.isShow = show
        notifyDataSetChanged()
    }
}
