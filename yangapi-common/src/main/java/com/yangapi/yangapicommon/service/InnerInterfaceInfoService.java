package com.yangapi.yangapicommon.service;


import com.yangapi.yangapicommon.model.entity.InterfaceInfo;

public interface InnerInterfaceInfoService {
    //校验并获取接口
    InterfaceInfo checkInterfaceInfoIsExist(String path, String method);
}
