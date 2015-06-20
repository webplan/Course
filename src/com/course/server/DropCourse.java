package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.BeanSetter;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.course.bean.StudentInfo;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;

public class DropCourse extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String courseId;
    private String studentId;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject obj = new JSONObject();
        try {
            //TODO 代码写在这里
            //TODO 判断 '学生不存在'|'课程不存在'|'学分已满'|'选课时间地点冲
            //TODO  突'|'选课人数已满'

            //修改学生选课信息
            DataAccessInterface<StudentInfo> dac = DACFactory.getInstance().createDAC(StudentInfo.class);
            Condition<StudentInfo> condition= new Condition<StudentInfo>(){
                @Override
                public boolean assertBean(StudentInfo studentInfo) {
                    return studentInfo.getStudentId().equals(studentId);
                }
            };
            BeanSetter<StudentInfo> setter = new BeanSetter<StudentInfo>() {
                @Override
                public void set(StudentInfo studentInfo) {
                    List<String> li = studentInfo.getCourseId();
                    li.remove(courseId);
                    studentInfo.setCourseId(li);
                }
            };
            dac.updateByCondition(condition, setter);

            //修改课程余量
            DataAccessInterface<CourseInfo> dacCou = DACFactory.getInstance().createDAC(CourseInfo.class);
            Condition<CourseInfo> conditionCou= new Condition<CourseInfo>(){
                @Override
                public boolean assertBean(CourseInfo courseInfo) {
                    return courseInfo.getCourseId().equals(courseId);
                }
            };
            BeanSetter<CourseInfo> setterCou = new BeanSetter<CourseInfo>() {
                @Override
                public void set(CourseInfo courseInfo) {
                    int surplus = courseInfo.getSurplus()+1;
                    courseInfo.setSurplus(surplus);
                }
            };
            dacCou.updateByCondition(conditionCou,setterCou);
        }catch (Exception e){

        }

        ret = obj.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}