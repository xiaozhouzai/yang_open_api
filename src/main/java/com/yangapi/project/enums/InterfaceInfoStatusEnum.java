package com.yangapi.project.enums;

public enum InterfaceInfoStatusEnum {
    OFFLINE(0,"未发布"),
    ONLINE(1,"已发布")
    ;



    private final Integer code;


    private final String status;

    InterfaceInfoStatusEnum(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    /**
     * 获取
     * @return code
     */
    public Integer getCode() {
        return code;
    }
    /**
     * 获取
     * @return status
     */
    public String getStatus() {
        return status;
    }


}
