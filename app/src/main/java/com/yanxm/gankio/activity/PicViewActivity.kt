package com.yanxm.gankio.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yanxm.gankio.R
import com.yanxm.gankio.utils.ImageUtils
import kotlinx.android.synthetic.main.activity_pic_view.*

class PicViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic_view)

        var url = intent.getStringExtra("url")

        ImageUtils.loadImage(url,image)

    }
}
