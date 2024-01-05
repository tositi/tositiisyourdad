package com.oddfar.campus.common.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置信息
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${swagger.show}")
    private boolean enableSwagger;

    @Bean
    public Docket webApiConfig() {
        //添加head参数配置
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .enable(enableSwagger)
                .apiInfo(webApiInfo())
                .select()
                //只显示api路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build().globalOperationParameters(pars);//注意这里
    }

    @Bean
    public Docket adminApiConfig() {
        //添加head参数配置
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        //添加head参数配置end
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .enable(enableSwagger)
                .apiInfo(adminApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build()
                .globalOperationParameters(pars);//注意这里
    }


    private ApiInfo webApiInfo() {

        return new ApiInfoBuilder()
                .title("网站-API文档")
                .description("本文档描述了网站接口定义")
                .version("1.0")
                .contact(new Contact("oddfar", "http://oddfar.com", "3066693006@qq.com"))
                .build();
    }

    private ApiInfo adminApiInfo() {

        return new ApiInfoBuilder()
                .title("后台管理系统-API文档")
                .description("本文档描述了后台管理系统接口定义")
                .version("1.0")
                .contact(new Contact("oddfar", "http://oddfar.com", "3066693006@qq.com"))
                .build();
    }

    @Bean
    public Docket touristConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("touristApi")
                .enable(enableSwagger)
                .apiInfo(touristInfo())
                .select()
                //只显示tourist路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/tourist/.*")))
                .build();
    }

    private ApiInfo touristInfo() {
        return new ApiInfoBuilder()
                .title("游客-API文档")
                .description("本文档描述了游客接口定义")
                .version("1.0")
                .contact(new Contact("oddfar", "http://oddfar.com", "3066693006@qq.com"))
                .build();
    }

}
