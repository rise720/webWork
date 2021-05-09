package com.cac.webWork.utility;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @ClassName: DynamicDataSourceContextHolder
* @Description: 动态数据源上下文管理
*
* @version: v1.0.0
* @author: JinWH
* @date: 2019年5月5日 下午1:22:40 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年5月5日     JinWH           v1.0.0               修改原因
*/
public class DynamicDataSourceContextHolder {
	private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
	
	//存放当前线程使用的数据源类型信息
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    //存放数据源id
    public static List<String> dataSourceIds = new ArrayList<String>();

    //设置数据源
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    //获取数据源
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    //清除数据源
    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    //判断当前数据源是否存在
    public static boolean isContainsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}