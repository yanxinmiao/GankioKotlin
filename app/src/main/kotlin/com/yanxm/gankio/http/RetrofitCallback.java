package com.yanxm.gankio.http;

import android.accounts.NetworkErrorException;

import com.orhanobut.logger.Logger;
import com.yanxm.gankio.bean.BaseBean;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dell on 2017/5/10.
 *
 * 自定义retrofit 回调
 *
 */

public abstract class RetrofitCallback<T extends BaseBean> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response != null && response.isSuccessful()){
            int code = response.raw().code();
            Logger.d("code:" + code);
            if (code == 200){
                boolean status = response.body().getError();
                if (!status){//请求成功
                    T t = response.body();
                    onSuccess(t);
                }else{
                    onFail(response.body().getError() + "");
//                    int errorCode = response.body().getError().getCode();
//                    switch (errorCode){
//                        case Constant.TOKEN_OVERDUE_CODE://token过期
//
//                        case Constant.REQUIRED_TOKEN_CODE:
//                            onAutoLogin();
//                            break;
//                        default:
//                            onFail(Constant.UNKNOWN_ERROR_CODE,response.body().getError().getMsg());
//                            break;
//                    }
                }
            }else{
                onFail(response.body().getError()+"");
            }
        }else {
            onFail(response.body().getError()+"");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (call.isCanceled() || t instanceof SocketException) {
            return;
        }

        if (t instanceof SocketTimeoutException){
//            onFail(Constant.SOCKETTIMEOUT_ERROR_CODE,"请求超时");
        }else if(t instanceof NetworkErrorException){
            onNetworkError();
        }else if(t instanceof RuntimeException){

        }else if (t instanceof UnknownHostException){
            onNetworkError();
        }else{
//            onFail(Constant.UNKNOWN_ERROR_CODE,t.getMessage());
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onFail(String message);

    public void onNetworkError(){

    }
    public void onAutoLogin(){

    }
}
