package com.lyz.serviceDriverUser.generator;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 自动生成代码工具类
 */
public class MysqlGenerator {
    public static void main(String[] args) {

        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/service-driver-user?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC",
                        "root","20000113a@")
                .globalConfig(builder -> {
                    builder.author("Yangzi").fileOverride().outputDir("/Users/championfish/Desktop/online_taxi/online-taxi-public/service-driver-user/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.lyz.serviceDriverUser").pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            "/Users/championfish/Desktop/online_taxi/online-taxi-public/service-driver-user/src/main/java/com/lyz/serviceDriverUser/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("driver_user_work_status");

                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
