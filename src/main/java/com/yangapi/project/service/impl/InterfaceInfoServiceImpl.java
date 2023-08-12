package com.yangapi.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangapi.project.common.ErrorCode;
import com.yangapi.project.exception.BusinessException;
import com.yangapi.project.mapper.InterfaceInfoMapper;

import com.yangapi.project.service.InterfaceInfoService;
import com.yangapi.yangapicommon.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author lcyzh
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2023-08-04 16:20:28
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();
        if (add){
            if (StringUtils.isAnyBlank(name,description,url,requestHeader,responseHeader,method)){
                throw new BusinessException(ErrorCode.DATA_REQUEST_ERROR);
            }
        }
        if (name.length() > 50){
            throw new BusinessException(ErrorCode.DATA_REQUEST_ERROR,"名称过长");
        }
    }


}




