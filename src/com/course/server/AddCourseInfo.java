package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.course.bean.Time;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONException;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class AddCourseInfo extends ActionSupport implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private HttpServletResponse response;

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    private String course;

    //定义处理用户请求的execute方法
    public String execute() {
        String ret = "";
        JSONObject jsob = new JSONObject();
        try {
            //TODO 代码写在这里
            JSONObject jsonObject = new JSONObject(course);
            CourseInfo ci = new CourseInfo();
            String courseId = jsonObject.getString("courseId");
            ci.setCourseId(jsonObject.getString("courseId"));
            ci.setSchoolName(jsonObject.getString("schoolName"));
            ci.setCourseName(jsonObject.getString("courseName"));
            ci.setTeacherName(jsonObject.getString("teacherName"));
            ci.setCredit(jsonObject.getInt("credit"));
            ci.setLocation(jsonObject.getString("location"));
            ci.setCapacity(jsonObject.getInt("capacity"));
            ci.setSurplus(jsonObject.getInt("capacity"));//课程余量与容量一致

            JSONObject jsobTime = jsonObject.getJSONObject("time");
            Time time = new Time();
            time.setWeekday(jsobTime.getInt("weekday"));
            time.setPeriod(jsobTime.getInt("period"));
            ci.setTime(time);

            //加入dac
            DataAccessInterface<CourseInfo> dac = DACFactory.getInstance().createDAC(CourseInfo.class);
            Condition<CourseInfo> condition = new Condition<CourseInfo>() {
                @Override
                public boolean assertBean(CourseInfo courseInfo) {
                    return courseInfo.getCourseId().equals(courseId);
                }
            };
            //判断选课号是否已存在
            List list = dac.selectByCondition(condition);
            if (list.size() > 0) {
                jsob.put("success", false);
                jsob.put("failReason", "选课号已存在");
                ret = jsob.toString();
                PrintToHtml.PrintToHtml(response, ret);
                return null;
            }

            //TODO 判断时间地点、教师冲突、院系不存在
            dac.beginTransaction();
            dac.add(ci);
            dac.commit();
            jsob.put("success", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret = jsob.toString();
        PrintToHtml.PrintToHtml(response, ret);
        return null;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}