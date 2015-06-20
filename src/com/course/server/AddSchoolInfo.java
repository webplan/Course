package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.SchoolInfo;
import com.course.function.PrintToHtml;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.struts2.interceptor.ServletResponseAware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;

public class AddSchoolInfo extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String schoolName;
    private String creditRequirement;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject obj = new JSONObject();
        //TODO 代码写在这里
        SchoolInfo si = new SchoolInfo();
        si.setSchoolName(schoolName);
        si.setCreditRequirement(creditRequirement);

        DataAccessInterface<SchoolInfo> dac = DACFactory.getInstance().createDAC(SchoolInfo.class);
        //TODO
        dac.beginTransaction();
        dac.add(si);
        dac.commit();


        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCreditRequirement() {
        return creditRequirement;
    }

    public void setCreditRequirement(String creditRequirement) {
        this.creditRequirement = creditRequirement;
    }
}