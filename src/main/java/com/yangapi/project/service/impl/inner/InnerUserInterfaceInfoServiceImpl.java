package com.yangapi.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yangapi.project.common.ErrorCode;
import com.yangapi.project.exception.BusinessException;
import com.yangapi.project.service.UserInterfaceInfoService;
import com.yangapi.yangapicommon.model.entity.UserInterfaceInfo;
import com.yangapi.yangapicommon.service.InnerUserInterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 改造升级:调用完判断该用户是否已经调用过此接口，如果存在该记录，执行调用次数加减逻辑
     * 如果不存在，向用户接口关系表插入新纪录，并且设置初始调用次数
     * 固定给用户每个接口分配100此调用机会
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {

        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getUserId,userId);
        UserInterfaceInfo one = userInterfaceInfoService.getOne(queryWrapper);
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        UserInterfaceInfo userInterfaceInfo2 = new UserInterfaceInfo();
        if (one == null ){
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setTotalNum(1);
            userInterfaceInfo.setLeftNum(99);
            return userInterfaceInfoService.save(userInterfaceInfo);
        } else if (one.getInterfaceInfoId() == interfaceInfoId){
            UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("interfaceInfoId", interfaceInfoId);
            updateWrapper.eq("userId", userId);
            updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
            return userInterfaceInfoService.update(updateWrapper);
        } else {
            userInterfaceInfo2.setUserId(userId);
            userInterfaceInfo2.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo2.setTotalNum(1);
            userInterfaceInfo2.setLeftNum(99);
            return userInterfaceInfoService.save(userInterfaceInfo2);
        }
    }
}
