package com.yangapi.project.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class DeleteByIdListRequest implements Serializable {
    private static final long serialVersionUID = -9046334220518553435L;


    private List<Long> idList;
}
