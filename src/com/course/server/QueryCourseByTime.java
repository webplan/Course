package com.course.server;/**
 * Created by snow on 15-6-19.
 */

import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.course.bean.Time;
import com.opensymphony.xwork2.ActionSupport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.course.function.PrintToHtml;
import org.apache.struts2.interceptor.ServletResponseAware;

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
        JSONObject jsob = new JSONObject();
        //TODO 代码写在这里
        try {
            DataAccessInterface<CourseInfo> dac = DACFactory.getInstance().createDAC(CourseInfo.class);
            Condition<CourseInfo> condition = new Condition<CourseInfo>() {
                @Override
                public boolean assertBean(CourseInfo courseInfo) {
                    if (courseInfo.getTime().getPeriod() == time.getPeriod() &&
                            courseInfo.getTime().getWeekday() == time.getWeekday())
                        return courseInfo.getTime().equals(time);
                    else
                        return false;
                }
            };

            //得到所有course
            JSONArray jsonArray = new JSONArray();
            for (CourseInfo s : dac.selectByCondition(condition))
                jsonArray.put(new JSONObject(s));

            jsob.put("courses", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret = jsob.toString();
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