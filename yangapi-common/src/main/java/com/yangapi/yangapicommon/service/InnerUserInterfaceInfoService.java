package com.yangapi.yangapicommon.service;



public interface InnerUserInterfaceInfoService {

    //调用接口次数加1
    boolean invokeCount(long interfaceInfoId, long userId);
}
