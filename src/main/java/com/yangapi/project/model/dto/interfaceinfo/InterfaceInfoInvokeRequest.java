package com.yangapi.project.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    private static final long serialVersionUID = 1127337082956508728L;

    private Long id;
    private String userRequestParams;
}
