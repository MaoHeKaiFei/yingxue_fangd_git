package com.fd;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fd.dao.UserMapper;
import com.fd.entity.*;

import com.fd.service.UserService;
import com.sun.rowset.internal.Row;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



@RunWith(SpringRunner.class)
@SpringBootTest
public class POITest {

    @Autowired
    UserMapper userMapper;



    @Test
    public void testMy() throws FileNotFoundException {

        //创建集合
        ArrayList<Admin> list = new ArrayList<>();



        //创建工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作表
        HSSFSheet sheet = workbook.createSheet("管理员信息1");

        //创建一个标题行


        //创建标题行
        HSSFRow row = sheet.createRow(0);
        String[] title={"ID","名字","密码"};

        //处理单元格对象
        HSSFCell cell = null;
        for (int i=0; i<title.length;i++){
            cell = row.createCell(i); //单元格下标
            cell.setCellValue(title[i]); //单元格内容
            //cell.setCellStyle(cellStyle); //标题行使用样式
        }

        //处理数据行
        for (int i = 0; i <list.size() ; i++) {
            //遍历一次创建一行
            HSSFRow row2 = sheet.createRow(i + 1);
            row2.createCell(0).setCellValue(list.get(i).getId());
            row2.createCell(1).setCellValue(list.get(i).getUsername());
            row2.createCell(2).setCellValue(list.get(i).getPassword());
        }


        //创建输出流 从内存中写入本地磁盘
        try {
            workbook.write(new FileOutputStream(new File("D:/测试poi.xls")));
            //关闭资源
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Test
    public void inportTest(){
        //获取表格中数据 读入程序中
        //插入数据库

        try {
            //获取导入的文件
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File("D:/测试poi.xls")));
           //根据文档获取工作·表
            HSSFSheet sheet = workbook.getSheet("管理员信息1");
            //获取行


            for (int i = 1; i <=sheet.getLastRowNum(); i++) {
                //获取行
                HSSFRow row = sheet.getRow(i);
                String id = row.getCell(0).getStringCellValue();
                String username = row.getCell(1).getStringCellValue();
                String password = row.getCell(2).getStringCellValue();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //导出 一对多 多对多导出
    @Test
    public void easyPoiTest() throws FileNotFoundException {
        List<Emp> emps = new ArrayList<>();
        emps.add(new Emp("1", "小黄", 23, new Date()));
        emps.add(new Emp("2", "小刘", 26, new Date()));
        emps.add(new Emp("3", "小黑", 24, new Date()));
        emps.add(new Emp("4", "小张", 18, new Date()));

        Admin admin = new Admin("1", "方蝶", "12345", emps);
        Admin admin2 = new Admin("2", "花花", "12345", emps);
        //创键用户集合
        ArrayList<Admin> list = new ArrayList<>();
        list.add(admin);
        list.add(admin2);


        //导出的参数：标题 工作表名
        ExportParams params = new ExportParams("计算机一班学生", "学生");
        //配置工作表参数：导出参数对象 导出数据对象（类型） 倒数数据集合
        Workbook workbook = ExcelExportUtil.exportExcel(params,Admin.class,list);

        try {
            //指定导出位置和文件名
            workbook.write(new FileOutputStream(new File("D://easyPoi.xls")));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void easyPoiImport(){
        //导入参数
        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(1);//表格标题行数 默认0
        importParams.setHeadRows(2);//表头所占行 表头行数 默认1

        try {
            FileInputStream inputStream = new FileInputStream(new File("D://easyPoi.xls"));
            List<Admin> list = ExcelImportUtil.importExcel(inputStream, Admin.class, importParams);


            for (Admin admin : list) {

                System.out.println("用户数据"+admin);
                List<Emp> emps = admin.getEmps();
                for (Emp emp : emps) {
                    System.out.println("对应数据"+emp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //用户导出
    @Test
    public void userTest(){

        List<User> users = userMapper.selectAll();

        System.out.println(users);

        //导出的参数：标题 工作表名
        ExportParams params = new ExportParams("用户信息", "用户");
        //配置工作表参数：导出参数对象 导出数据对象（类型） 倒数数据集合
        Workbook workbook = ExcelExportUtil.exportExcel(params,User.class,users);

        try {
            //指定导出位置和文件名
            workbook.write(new FileOutputStream(new File("D://userPoi.xls")));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //图片导出
    @Test
    public void pictureTest() {


        List<Student> list = new ArrayList<>();
        list.add(new Student("1","放过","D:\\springLearn\\yingxue_fangd\\src\\main\\webapp\\bootstrap\\img\\pic1.jpg"));
        list.add(new Student("2","hahh","D:\\springLearn\\yingxue_fangd\\src\\main\\webapp\\bootstrap\\img\\pic1.jpg"));
        list.add(new Student("3","aaa","src\\main\\webapp\\bootstrap\\img\\pic1.jpg"));
        //导出的参数：标题 工作表名
        ExportParams params = new ExportParams("student信息", "student");
        //配置工作表参数：导出参数对象 导出数据对象（类型） 倒数数据集合
        Workbook workbook = ExcelExportUtil.exportExcel(params,Student.class,list);

        try {
            //指定导出位置和文件名
            workbook.write(new FileOutputStream(new File("D://student.xls")));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }



    @Test
    public void easyPoiInput(){
        //创建导入对象
        ImportParams params = new ImportParams();
        params.setTitleRows(1);  //表格标题行数,默认0
        params.setHeadRows(1);  //表头行数,默认1
        // 获取导入数据
        List<User> users = null;
        try {
            users = ExcelImportUtil.importExcel(new FileInputStream(new
                    File("D:/userPoi.xls")), User.class, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (User user : users) {
            System.out.println(user);
        }
    }




}


















