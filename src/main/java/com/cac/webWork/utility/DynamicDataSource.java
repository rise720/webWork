package com.cac.webWork.utility;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * 
* @ClassName: DynamicDataSource
* @Description: 动态数据源
*
* @version: v1.0.0
* @author: JinWH
* @date: 2019年5月5日 下午3:02:55 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年5月5日     JinWH           v1.0.0               修改原因
 */
public class DynamicDataSource extends AbstractRoutingDataSource{

	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceContextHolder.getDataSourceType();
	}

}
