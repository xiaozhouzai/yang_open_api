package com.yangapi.project.model.dto.userinterfaceinfo;

import com.yangapi.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 3836154590649455057L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;


}
