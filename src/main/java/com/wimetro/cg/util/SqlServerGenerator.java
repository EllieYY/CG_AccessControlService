package com.wimetro.cg.util;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @title: SqlServerGenerator
 * @author: Ellie
 * @date: 2023/03/18 15:30
 * @description:
 **/
public class SqlServerGenerator {
    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder(
            "jdbc:sqlserver://113.57.214.58:14340;databaseName=ykt2_dev",
//            "jdbc:sqlserver://113.57.214.58:14339;databaseName=ykt",
            "sa",
            "iafc@2022");

    public static void main(String[] args) {
        List<String> tables = new ArrayList<>();
        tables.add("cgcg_controller");
//        tables.add("d_schedules");
//        tables.add("d_schedules_group_detail");
//        tables.add("dev_output_detail");
//        tables.add("dev_reader_detail");
//        tables.add("apb");
//        tables.add("defence_input");
//        tables.add("d_access_level_door");
//        tables.add("d_access_level");
//        tables.add("ele_level_reader");
//        tables.add("d_holiday");
//        tables.add("d_schedules_group");
//        tables.add("d_schedules");
//        tables.add("d_schedules_group_detail");
//        tables.add("c_card_info");
//        tables.add("card_format");
//        tables.add("dev_controller_common_attribute");
//        tables.add("e_access_record");
//        tables.add("e_alarm_record");
//        tables.add("e_log_record");
//        tables.add("task_detail");
//        tables.add("ele_access_level_detail");
//        tables.add("ele_access_level");
//        tables.add("ele_level_detail");
//        tables.add("e_event_record");
//        tables.add("trig_scp_proc_detail");
//        tables.add("trigger_info");
//        tables.add("trigger_var");
//        tables.add("e_event_code_detail_dev");
//        tables.add("d_employee_auth");

        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {
                    builder.author("Ellie")
                            .outputDir(System.getProperty("user.dir")+"\\src\\main\\java")    //输出路径(写到java目录)
                            .enableSwagger()           //开启swagger
                            .commentDate("yyyy-MM-dd");
                })
                .packageConfig(builder -> {
                    builder.parent("com.wimetro.cg")
                            .moduleName("db")
                            .entity("entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .mapper("mapper")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    System.getProperty("user.dir")+"\\src\\main\\resources\\mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
//                            .addTablePrefix("p_")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()
                            .enableLombok()
                            .logicDeleteColumnName("deleted")
                            .enableTableFieldAnnotation()
                            .controllerBuilder()
                            .formatFileName("%sController")
                            .enableRestStyle()
                            .mapperBuilder()
                            .enableBaseResultMap()  //生成通用的resultMap
                            .enableBaseColumnList()
                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()
                            .formatXmlFileName("%sMapper");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}


