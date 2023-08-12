package com.yangapi.yangapicommon.service;


import com.yangapi.yangapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author lcy
 */
public interface InnerUserService {

    /**
     * 根据accessKey获取用户信息和密匙
     *
     */
     User getInvokeUser(String accessKey);

}
