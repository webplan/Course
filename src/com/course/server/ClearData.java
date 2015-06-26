package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;

public class ClearData extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }



    //定义处理用户请求的execute方法
    public String execute() {
        //TODO 代码写在这里
            //TODO 清空dac
            //          方法1：使用给定的dulete方法，搜索所有dac，然后删除---太蠢
            //          方法2：删除文件----不知文件名
        return null;
    }

}