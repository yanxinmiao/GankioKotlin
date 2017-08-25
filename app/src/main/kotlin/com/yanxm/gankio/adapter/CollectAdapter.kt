package com.yanxm.gankio.adapter

import android.support.annotation.LayoutRes
import android.widget.ImageView
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yanxm.gankio.R
import com.yanxm.gankio.bean.ClassifyData
import com.yanxm.gankio.utils.ImageUtils

/**
 * Created by dell on 2017/8/24.
 *
 * 带滑动删除
 */
class CollectAdapter(@LayoutRes id: Int, data: List<ClassifyData>?) : BaseItemDraggableAdapter<ClassifyData,BaseViewHolder>(id,data) {

    override fun convert(helper: BaseViewHolder, item: ClassifyData) {
        helper.setText(R.id.tv_desc,item.desc)
        helper.setText(R.id.tv_time,item.publishedAt)
        helper.setText(R.id.tv_type,item.type)
        if (item.images != null && item.images.size > 0){
            helper.setVisible(R.id.iv_cover,true)
            var ivCover = helper.getConvertView().findViewById(R.id.iv_cover) as ImageView
            ImageUtils.loadImage(item.images[0],ivCover)

        }else{
            helper.setVisible(R.id.iv_cover,false)
        }
    }
}