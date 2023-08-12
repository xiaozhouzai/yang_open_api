package com.yangapi.project.yangapigateway;


import com.yangapi.sdk.Utils.SignUtils;
import com.yangapi.yangapicommon.model.entity.InterfaceInfo;
import com.yangapi.yangapicommon.model.entity.User;
import com.yangapi.yangapicommon.service.InnerInterfaceInfoService;
import com.yangapi.yangapicommon.service.InnerUserInterfaceInfoService;
import com.yangapi.yangapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    private static final String INTERFACE_HOST = "http://localhost:8100";

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1","0:0:0:0:0:0:0:1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = Objects.requireNonNull(request.getMethod()).toString();
        log.info("请求的唯一标识: {}", request.getId());
        log.info("请求的方法: {}", request.getMethod());
        log.info("请求的路径: {}", request.getPath().value());
        log.info("请求的来源: {}", Objects.requireNonNull(request.getRemoteAddress()).getHostString());
        log.info("请求的参数: {}", request.getQueryParams());
        //白名单，自定义一个白名单集合
        //获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        //如果不在白名单中
        if (!IP_WHITE_LIST.contains(request.getRemoteAddress().getHostString())) {
            response.setStatusCode(HttpStatus.FORBIDDEN); //设置响应状态码
            return response.setComplete();
            //调用response.setComplete()时，它会立即终止请求的处理并发送响应。
            // 这意味着不会执行后续的处理器或过滤器，并且不会将响应传递给客户端。
            //通常，在请求处理过程中，当我们已经完成了所需的操作并且不需要进一步处理请求时，
            // 可以调用response.setComplete()来提前结束请求的处理。
            // 这对于某些场景下的性能优化或特定的业务逻辑非常有用。
            //需要注意的是，一旦调用了response.setComplete()，就不能再对响应进行任何更改
        }
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String bod = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        if (accessKey == null){
            log.error("未获取到请求信息");
            return returnResponse(response);
        }
        //获取当前调用接口的用户信息
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null){
            log.error("accessKey校验失败，未获取到信息");
            return returnResponse(response);
        }
        String secretKey = invokeUser.getSecretKey();
        String accessKey1 = invokeUser.getAccessKey();
        String sign1 = SignUtils.getSign(bod, secretKey);
        if (!accessKey1.equals(accessKey)){
            log.error("accessKey不匹配，无权限");
            return returnResponse(response);
        }
        if (!sign1.equals(sign)){
            log.error("secretKey不匹配，无权限");
            return returnResponse(response);
        }
        //判断当前接口是否存在
        InterfaceInfo info = null;
        try {
            info = innerInterfaceInfoService.checkInterfaceInfoIsExist(path,method);
        }catch (Exception e){
            log.error("接口不存在",e);
        }
        if (info == null){
            return returnResponse(response);
        }

        return decoratedResponse(exchange,chain,info.getId(),invokeUser.getId());

    }



    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> decoratedResponse(ServerWebExchange exchange, GatewayFilterChain chain,Long interfaceInfoId,Long invokeUserId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                //todo 处理业务逻辑
                                //todo 调用完接口次数加1
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId,invokeUserId);
                                }catch (Exception e){
                                    log.error("调用接口次数+1方法失败:",e);
                                }




                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //data为响应结果
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                log.info("响应结果:{}",data);
//                                log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关内部处理异常 ！\n" + e);
            return chain.filter(exchange);
        }
    }





    private Mono<Void> returnResponse(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN); //设置响应状态码
        return response.setComplete();
    }
    @Override
    public int getOrder() {
        return -1;
    }

}

