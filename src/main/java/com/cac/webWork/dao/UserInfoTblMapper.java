package com.cac.webWork.dao;

import java.util.List;

import com.cac.webWork.model.UserInfoTbl;

public interface UserInfoTblMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(UserInfoTbl record);

    int insertSelective(UserInfoTbl record);

    UserInfoTbl selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(UserInfoTbl record);

    int updateByPrimaryKey(UserInfoTbl record);
    
    List<UserInfoTbl> findSelective(UserInfoTbl record);
}