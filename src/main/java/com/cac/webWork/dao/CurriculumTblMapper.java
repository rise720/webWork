package com.cac.webWork.dao;

import com.cac.webWork.model.CurriculumTbl;
import com.cac.webWork.utility.TargetDataSource;


public interface CurriculumTblMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CurriculumTbl record);

    int insertSelective(CurriculumTbl record);

    CurriculumTbl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CurriculumTbl record);

    int updateByPrimaryKey(CurriculumTbl record);
}