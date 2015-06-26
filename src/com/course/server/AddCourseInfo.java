package com.course.server;/**
 * Created by snow on 15-6-19.
 */


import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.course.function.Servlet;
import javax.servlet.http.HttpServletResponse;

public class AddCourseInfo extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private JSONObject course;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject jsob = Servlet.addCourseInfo(course);

        ret = jsob.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public JSONObject getCourse() {
        return course;
    }

    public void setCourse(JSONObject course) {
        this.course = course;
    }
}