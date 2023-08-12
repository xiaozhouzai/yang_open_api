package com.yangapi.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yangapi.yangapicommon.model.entity.UserInterfaceInfo;

/**
* @author lcyzh
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-08-07 17:00:13
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);
}
