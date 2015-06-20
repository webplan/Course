package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import java.sql.*;
import javax.servlet.http.HttpServletResponse;

public class QueryCourseByTime extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private Time time;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject obj = new JSONObject();
        //TODO 代码写在这里
        DataAccessInterface<CourseInfo> dac = DACFactory.getInstance().createDAC(CourseInfo.class);
        Condition<CourseInfo> condition = new Condition<CourseInfo>() {
            @Override
            public boolean assertBean(CourseInfo courseInfo) {
                return courseInfo.getTime().equals(time);
            }
        };

        //得到所有course
        for (CourseInfo s :dac.selectByCondition(condition))
            System.err.println(s);
        ret = obj.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}