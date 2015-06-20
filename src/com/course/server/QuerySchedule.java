package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.course.bean.StudentInfo;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONArray;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class QuerySchedule extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String studentId;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject jsob = new JSONObject();
        //TODO 代码写在这里
        try {
            //获得学生信息
            DataAccessInterface<StudentInfo> dacStu = DACFactory.getInstance().createDAC(StudentInfo.class);
            Condition<StudentInfo> conditionStu = new Condition<StudentInfo>() {
                @Override
                public boolean assertBean(StudentInfo StudentInfo) {
                    return StudentInfo.getStudentId().equals(studentId);
                }
            };
            List<StudentInfo> listStu = dacStu.selectByCondition(conditionStu);
            if (listStu.size()==0){
                //TODO 文档中没有，若学生id不存在怎么处理
                return null;
            }else{

            }

            //得到学生选的课程
            DataAccessInterface<CourseInfo> dac = DACFactory.getInstance().createDAC(CourseInfo.class);
            Condition<CourseInfo> condition = new Condition<CourseInfo>() {
                @Override
                public boolean assertBean(CourseInfo courseInfo) {
                    return listStu.get(0).getCourseId().contains(courseInfo.getCourseId());
                }
            };
            //得到所有course
            JSONArray jsonArray = new JSONArray();
            for (CourseInfo s : dac.selectByCondition(condition))
                jsonArray.put(new JSONObject(s));

            jsob.put("courses", jsonArray);
        } catch (Exception e) {

        }
        ret = jsob.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}