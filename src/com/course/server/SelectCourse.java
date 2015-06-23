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

    private String courseId;
    private String studentId;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject jsob = new JSONObject();

        try {
            //TODO 代码写在这里
            //TODO 判断 '学生不存在'|'课程不存在'|'学分已满'|'选课时间地点冲
            //TODO  突'|'选课人数已满'
            if (!Judge.isStudent(studentId)){//学生存在
                if (!Judge.isCourse(courseId)){//课程存在
                    if (Judge.isSelectTimeOrLocation(studentId, courseId)){//选课时间地点不冲突
                        if (Judge.isSelectFull(courseId)){//选课人数未满
                            if (Judge.isStudentCredit(studentId)){//学生学分满

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
                                        li.add(courseId);
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
                                        int surplus = courseInfo.getSurplus()-1;
                                        courseInfo.setSurplus(surplus);
                                    }
                                };
                                dacCou.updateByCondition(conditionCou,setterCou);
                                jsob.put(Config.SUCCESS,true);
                            }else {
                                jsob.put(Config.SUCCESS,false);
                                jsob.put(Config.FAILREASON,Config.CREDIT_FULL);
                            }
                        }else {
                            jsob.put(Config.SUCCESS,false);
                            jsob.put(Config.FAILREASON,Config.SELECT_FULL);
                        }
                    }else {
                        jsob.put(Config.SUCCESS,false);
                        jsob.put(Config.FAILREASON,Config.SELECT_TIME_LOCATION_CONFLICT);
                    }
                }else {
                    jsob.put(Config.SUCCESS,false);
                    jsob.put(Config.FAILREASON,Config.COURSE_NOT_EXIST);
                }
            }else {
                jsob.put(Config.SUCCESS,false);
                jsob.put(Config.FAILREASON,Config.STUDENT_NOT_EXIST);
            }


        }catch (Exception e){

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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}