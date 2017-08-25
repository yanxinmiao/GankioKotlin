/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2015, 蒋朋, china, qd. sd
**                          All Rights Reserved
**
**                           By()
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/

package com.yanxm.gankio.db;


import com.yanxm.gankio.dao.ClassifyDataDao;

/**
 * 一个工具类 DbUtils 获得 Helper
 *
 */
public class DbUtil {

    private static ClassifyDbHelper homeDataHelper;
    private static ClassifyDataDao getDriverDao() {
        return DbCore.getDaoSession().getClassifyDataDao();
    }


    private static ClassifyDataDao getBeanDao() {
        return DbCore.getDaoSession().getClassifyDataDao();
    }

    public static ClassifyDbHelper getHomeDataHelperHelper() {
        if (homeDataHelper == null) {
            homeDataHelper = new ClassifyDbHelper(getDriverDao());
        }
        return homeDataHelper;
    }

}
