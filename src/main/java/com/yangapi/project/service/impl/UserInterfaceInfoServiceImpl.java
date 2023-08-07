package com.yangapi.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangapi.project.common.ErrorCode;
import com.yangapi.project.exception.BusinessException;
import com.yangapi.project.mapper.UserInterfaceInfoMapper;
import com.yangapi.project.model.entity.UserInterfaceInfo;
import com.yangapi.project.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author lcyzh
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        Integer status = userInterfaceInfo.getStatus();

        if (add){
            if (userId == null || interfaceInfoId == null){
                throw new BusinessException(ErrorCode.DATA_REQUEST_ERROR.getCode(),"用户不存在或接口不存在");
            }
        }
        if (totalNum > 100){
            throw new BusinessException(ErrorCode.DATA_REQUEST_ERROR.getCode(),"剩余次数设置过大");
        }
        if (leftNum <= 0){
            throw new BusinessException(ErrorCode.DATA_REQUEST_ERROR.getCode(),"剩余次数设置有误");
        }
        if (status > 1 || status < 0){
            throw new BusinessException(ErrorCode.DATA_REQUEST_ERROR.getCode(),"状态超出规定范围");
        }

    }
}




