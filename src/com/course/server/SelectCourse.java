package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.BeanSetter;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.course.bean.StudentInfo;
import com.course.function.Config;
import com.course.function.Judge;
import com.course.function.Servlet;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class SelectCourse extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private JSONObject select;
    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject jsob = Servlet.selectCourse(select);


        ret = jsob.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public JSONObject getSelect() {
        return select;
    }

    public void setSelect(JSONObject select) {
        this.select = select;
    }
}