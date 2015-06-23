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

    private String studentId;
    private String name;
    private String gender;
    private String schoolName;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject jsob = new JSONObject();
        try {
            StudentInfo si = new StudentInfo();
            si.setStudentId(studentId);
            si.setName(name);
            si.setGender(gender);
            si.setSchoolName(schoolName);

            //判断学号、学院
            if (Judge.isStudent(studentId)){//学号不存在
                if (!Judge.isSchool(schoolName)){//院系存在
                    DataAccessInterface<StudentInfo> dac = DACFactory.getInstance().createDAC(StudentInfo.class);
                    dac.beginTransaction();
                    dac.add(si);
                    dac.commit();
                    jsob.put(Config.SUCCESS, true);
                }else{
                    jsob.put(Config.SUCCESS,false);
                    jsob.put(Config.FAILREASON,Config.SCHOOL_NOT_EXIST);
                }
            }else{
                jsob.put(Config.SUCCESS,false);
                jsob.put(Config.FAILREASON,Config.STUDENT_NUMBER_EXIST);
            }


        } catch (Exception e) {

        }
        ret=jsob.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}