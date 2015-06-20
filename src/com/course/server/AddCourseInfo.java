package com.course.server;/**
 * Created by snow on 15-6-19.
 */

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
import javax.xml.datatype.DatatypeFactory;

public class AddCourseInfo extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String courseId;
    private String schoolName;
    private String courseName;
    private String teacherName;
    private String credit;
    private String location;
    private Time time;
    private int capacity;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        //TODO 代码写在这里
        CourseInfo ci = new CourseInfo();
        ci.setCourseId(courseId);
        ci.setSchoolName(schoolName);
        ci.setCourseName(courseName);
        ci.setTeacherName(teacherName);
        ci.setCredit(credit);
        ci.setLocation(location);
        ci.setTime(time);
        ci.setCapacity(capacity);

        //加入dac
        DataAccessInterface<CourseInfo> dac = DACFactory.getInstance().createDAC(CourseInfo.class);
        //TODO 判断是否已存等
        dac.beginTransaction();
        dac.add(ci);
        dac.commit();

        ret = "success";
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}