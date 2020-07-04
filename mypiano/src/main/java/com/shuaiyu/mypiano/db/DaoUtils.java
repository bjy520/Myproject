package com.shuaiyu.mypiano.db;

import java.util.List;

public class DaoUtils {

    private static DaoUtils mInstance; //单例

    public static DaoUtils getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {//保证异步处理安全操作

                if (mInstance == null) {
                    mInstance = new DaoUtils();
                }
            }
        }
        return mInstance;
    }
    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param dao
     */

    public static void insertSound(Dao dao) {
        GreenDaoManager.getInstance().getSession().getDaoDao().insertOrReplace(dao);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteSound(long id) {
        GreenDaoManager.getInstance().getSession().getDaoDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param dao
     */
    public static void updateSound(Dao dao) {
        GreenDaoManager.getInstance().getSession().getDaoDao().update(dao);
    }

    /**
     * 查询条件为的数据
     *
     * @return
     */
    public static List<Dao> querySound(int tag) {
        return GreenDaoManager.getInstance().getSession().getDaoDao().queryBuilder().where(DaoDao.Properties.Tag.eq(tag)).list();
    }

    /**
     * 查询全部数据
     */
    public static List<Dao> queryAll() {
        return GreenDaoManager.getInstance().getSession().getDaoDao().loadAll();
    }
}
