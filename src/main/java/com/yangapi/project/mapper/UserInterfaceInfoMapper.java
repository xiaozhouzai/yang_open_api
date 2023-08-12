package com.yangapi.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangapi.yangapicommon.model.entity.UserInterfaceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author lcyzh
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-08-07 17:00:13
* @Entity com.yangapi.project.model.entity.UserInterfaceInfo
*/

public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    @Select("select interfaceInfoId, sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId\n" +
            "        order by totalNum desc limit #{limit};")
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(long limit);

}




