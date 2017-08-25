package com.yanxm.gankio.bean

/**
 * Created by dell on 2017/8/23.
 *
 *
 */
class Result{
    /**
     * 数据类 ，它会自动生成所有属性和它们的访问器，以及一些有用的方法，比如，toString()
     */
    data class HomeItem( var id: Long ,var _id: String,  var createdAt: String, var desc: String, var images: List<String>,
                         var publishedAt: String, var source: String, var type: String, var url: String,
                         var used: Boolean,  var who: String)
}