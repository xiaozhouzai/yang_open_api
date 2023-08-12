package com.yangapi.project.yangapigateway;

import com.yangapi.project.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class })
@EnableDubbo  //开启dubbo注解
public class YangapiGatewayApplication {
	/*@Bean
	//编程式配置路由
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("tobaidu", r -> r.path("/baidu")
						.uri("http://baidu.com"))
				.route("host_route", r -> r.host("*.")
						.uri("http://httpbin.org"))
				.build();
	}*/
	@DubboReference   //需要调用的服务加上@DubboReference注解
	private DemoService demoService;

	// [程序员交流园地](https://www.code-nav.cn/) 从 0 到 1 求职指导，斩获 offer！1 对 1 简历优化服务、200+ 真实简历和建议参考、25w 字前后端精选面试题、2000+ 求职面试经验分享

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(YangapiGatewayApplication.class, args);
		YangapiGatewayApplication application = context.getBean(YangapiGatewayApplication.class);
		String result = application.doSayHello("world");
		String result2 = application.doSayHello2("world");
		System.out.println("result: " + result);
		System.out.println("result: " + result2);
	}

	public String doSayHello(String name) {
		return demoService.sayHello(name);
	}

	public String doSayHello2(String name) {
		return demoService.sayHello2(name);
	}


}
