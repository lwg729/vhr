package com.lwg.vhr.mapper;

import com.lwg.vhr.pojo.Hr;
import com.lwg.vhr.pojo.HrExample;
import com.lwg.vhr.pojo.Role;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HrMapper {
    long countByExample(HrExample example);

    int deleteByExample(HrExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Hr record);

    int insertSelective(Hr record);

    List<Hr> selectByExample(HrExample example);

    Hr selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Hr record, @Param("example") HrExample example);

    int updateByExample(@Param("record") Hr record, @Param("example") HrExample example);

    int updateByPrimaryKeySelective(Hr record);

    int updateByPrimaryKey(Hr record);

    Hr loadUserByUsername(String username);

    List<Role> getHrRolesById(Integer id);
}