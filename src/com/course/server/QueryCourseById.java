package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.course.bean.StudentInfo;
import com.course.function.Config;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONArray;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletResponse;

public class QueryCourseById extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String courseId;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = null;
        JSONObject jsob = new JSONObject();
        //TODO 代码写在这里
        try {
            DataAccessInterface<CourseInfo> dac = DACFactory.getInstance().createDAC(CourseInfo.class);
            Condition<CourseInfo> condition = new Condition<CourseInfo>() {
                @Override
                public boolean assertBean(CourseInfo courseInfo) {
                    return courseInfo.getCourseId().contains(courseId);
                }
            };
            //得到所有course
            JSONArray jsonArray = new JSONArray();
            for (CourseInfo s : dac.selectByCondition(condition))
                jsonArray.put(new JSONObject(s));
            //课程信息不存在返回 null
            if (jsonArray.length()==0)
                jsonArray = null;
            jsob.put(Config.COURSE,jsonArray);
        } catch (Exception e) {

        }
        ret = jsob.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}