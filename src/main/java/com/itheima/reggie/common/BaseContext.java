package com.itheima.reggie.common;

public class BaseContext {

    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    /**
     * 设置值
     * @param ID
     */
    public static void setThreadLocal(Long ID){
        threadLocal.set(ID);
    }


    /**
     * 获取值
     * @return
     */
    public static Long getCurrentID(){
        return threadLocal.get();
    }
}
