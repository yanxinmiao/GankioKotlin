package com.yanxm.gankio.http;

import com.yanxm.gankio.bean.BaseBean;
import com.yanxm.gankio.bean.ClassifyData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dell on 2017/8/23.
 */

public interface ApiService {

    /**
     * 获取分类数据
     * @param classify 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * @param page
     * @return
     */
    @GET("data/{classify}/10/{page}")
    Call<BaseBean<List<ClassifyData>>> getClassifyData(@Path("classify") String classify , @Path("page") int page);

    /**
     *  http://gank.io/api/search/query/listview/category/Android/count/10/page/1
     * @return
     */
    @GET("search/query/{key}/category/all/count/10/page/{page}")
    Call<BaseBean<List<ClassifyData>>> search(@Path("key") String key,@Path("page") int page);
}
