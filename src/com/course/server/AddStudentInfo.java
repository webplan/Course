package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.SchoolInfo;
import com.course.bean.StudentInfo;
import com.course.function.Config;
import com.course.function.Judge;
import com.course.function.Servlet;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class AddStudentInfo extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private JSONObject student;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject jsob = Servlet.addStudentInfo(student);

        ret=jsob.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public JSONObject getStudent() {
        return student;
    }

    public void setStudent(JSONObject student) {
        this.student = student;
    }
}