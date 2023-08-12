package com.yangapi.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yangapi.project.exception.BusinessException;
import com.yangapi.project.mapper.InterfaceInfoMapper;
import com.yangapi.yangapicommon.model.entity.InterfaceInfo;
import com.yangapi.yangapicommon.service.InnerInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;
    @Override
    public InterfaceInfo checkInterfaceInfoIsExist(String url, String method) {
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfo::getUrl,url);
        queryWrapper.eq(InterfaceInfo::getMethod,method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
