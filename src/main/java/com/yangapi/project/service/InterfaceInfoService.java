package com.yangapi.project.service;

import com.yangapi.project.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lcyzh
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2023-08-04 16:20:28
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
