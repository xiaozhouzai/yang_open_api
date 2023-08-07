package com.yangapi.project.model.dto.interfaceinfo;

import com.yangapi.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoQueryRequest  extends PageRequest implements Serializable {
    private static final long serialVersionUID = 7607249066492803941L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 接口状态（0 - 关闭， 1 - 开启））
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

}
