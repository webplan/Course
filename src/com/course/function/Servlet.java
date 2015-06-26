package com.course.function;

import cn.edu.fudan.se.dac.BeanSetter;
import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.course.bean.SchoolInfo;
import com.course.bean.StudentInfo;
import com.course.bean.Time;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by snow on 15-6-26.
 */
public class Servlet {

    public static JSONObject addCourseInfo(JSONObject jsInput){
        JSONObject jsOutput = new JSONObject();
        try {
            //TODO 代码写在这里
            CourseInfo ci = new CourseInfo();
            String courseId = jsInput.getString("courseId");
            ci.setCourseId(jsInput.getString("courseId"));
            ci.setSchoolName(jsInput.getString("schoolName"));
            ci.setCourseName(jsInput.getString("courseName"));
            ci.setTeacherName(jsInput.getString("teacherName"));
            ci.setCredit(jsInput.getInt("credit"));
            ci.setLocation(jsInput.getString("location"));
            ci.setCapacity(jsInput.getInt("capacity"));
            ci.setSurplus(jsInput.getInt("capacity"));//课程余量与容量一致

            JSONObject jsobTime = jsInput.getJSONObject("time");
            Time time = new Time();
            time.setWeekday(jsobTime.getInt("weekday"));
            time.setPeriod(jsobTime.getInt("period"));
            ci.setTime(time);

            //加入dac
            DataAccessInterface<CourseInfo> dac = DACFactory.getInstance().createDAC(CourseInfo.class);
            //判断课程、时间地点、教师冲突、院系
            if (Judge.isCourse(courseId))//课程不存在
                if (Judge.isTimeOrLocation(time,jsInput.getString("location")))//时间地点不冲突
                    if (Judge.isTeacherTime(time,jsInput.getString("teacherName")))//教师时间不冲突
                        if (!Judge.isSchool(jsInput.getString("schoolName"))) {//院系存在
                            dac.beginTransaction();
                            dac.add(ci);
                            dac.commit();
                            jsOutput.put(Config.SUCCESS, true);
                        }else{//院系不存在
                            jsOutput.put(Config.SUCCESS,false);
                            jsOutput.put(Config.FAILREASON,Config.SCHOOL_NOT_EXIST);
                        }
                    else{//教师时间冲突
                        jsOutput.put(Config.SUCCESS,false);
                        jsOutput.put(Config.FAILREASON,Config.TEACHER_CONFLICT);
                    }
                else{//时间地点冲突
                    jsOutput.put(Config.SUCCESS,false);
                    jsOutput.put(Config.FAILREASON,Config.TIME_LOCATION_CONFLICT);
                }
            else{//课程已存在
                jsOutput.put(Config.SUCCESS,false);
                jsOutput.put(Config.FAILREASON,Config.COURSE_NUMBER_EXIST);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsOutput;
    }

    public static JSONObject addSchoolInfo(JSONObject jsInput){
        //TODO 代码写在这里
        JSONObject jsOutput = new JSONObject();
        try {
            SchoolInfo si = new SchoolInfo();
            si.setSchoolName(jsInput.getString("schoolName"));
            si.setCreditRequirement(jsInput.getString("creditRequirement"));
            if(Judge.isSchool(jsInput.getString("schoolName"))){//学院不存在
                //加入dac
                DataAccessInterface<SchoolInfo> dac = DACFactory.getInstance().createDAC(SchoolInfo.class);

                dac.beginTransaction();
                dac.add(si);
                dac.commit();
                jsOutput.put(Config.SUCCESS, true);
            }else{
                jsOutput.put(Config.SUCCESS, false);
                jsOutput.put(Config.FAILREASON, Config.SCHOOL_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsOutput;
    }

    public static JSONObject addStudentInfo(JSONObject jsInput){
        JSONObject jsOutput = new JSONObject();
        try {
            StudentInfo si = new StudentInfo();
            si.setStudentId(jsInput.getString("studentId"));
            si.setName(jsInput.getString("name"));
            si.setGender(jsInput.getString("gender"));
            si.setSchoolName(jsInput.getString("schoolName"));

            //判断学号、学院
            if (Judge.isStudent(jsInput.getString("studentId"))){//学号不存在
                if (!Judge.isSchool(jsInput.getString("schoolName"))){//院系存在
                    DataAccessInterface<StudentInfo> dac = DACFactory.getInstance().createDAC(StudentInfo.class);
                    dac.beginTransaction();
                    dac.add(si);
                    dac.commit();
                    jsOutput.put(Config.SUCCESS, true);
                }else{
                    jsOutput.put(Config.SUCCESS, false);
                    jsOutput.put(Config.FAILREASON, Config.SCHOOL_NOT_EXIST);
                }
            }else{
                jsOutput.put(Config.SUCCESS,false);
                jsOutput.put(Config.FAILREASON,Config.STUDENT_NUMBER_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsOutput;
    }

    public static JSONObject queryCourseById(JSONObject jsInput){
        JSONObject jsOutput = new JSONObject();

        //TODO 代码写在这里
        try {
            String courseId = jsInput.getString("courseId");
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
            jsOutput.put(Config.COURSE,jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsOutput;
    }

    public static JSONObject queryCourseByTime(JSONObject jsInput){
        JSONObject jsOutput = new JSONObject();
        //TODO 代码写在这里
        try {
            Time time = (Time) jsInput.get("time");
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

            jsOutput.put(Config.COURSES, jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsOutput;
    }

    public static JSONObject querySchedule(JSONObject jsInput){
        JSONObject jsOutput = new JSONObject();
        try {
            String studentId = jsInput.getString("studentId");
            //获得学生信息
            DataAccessInterface<StudentInfo> dacStu = DACFactory.getInstance().createDAC(StudentInfo.class);
            Condition<StudentInfo> conditionStu = new Condition<StudentInfo>() {
                @Override
                public boolean assertBean(StudentInfo StudentInfo) {
                    return StudentInfo.getStudentId().equals(studentId);
                }
            };
            List<StudentInfo> listStu = dacStu.selectByCondition(conditionStu);
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

            jsOutput.put(Config.COURSES, jsonArray);
        } catch (Exception e) {

        }
        return jsOutput;
    }

    public static JSONObject selectCourse(JSONObject jsInput){
        JSONObject jsOutput = new JSONObject();
        try {
            //TODO 代码写在这里
            //TODO 判断 '学生不存在'|'课程不存在'|'学分已满'|'选课时间地点冲
            //TODO  突'|'选课人数已满'
            String studentId = jsInput.getString("studentId");
            String courseId = jsInput.getString("courseId");
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
                                jsOutput.put(Config.SUCCESS,true);
                            }else {
                                jsOutput.put(Config.SUCCESS,false);
                                jsOutput.put(Config.FAILREASON,Config.CREDIT_FULL);
                            }
                        }else {
                            jsOutput.put(Config.SUCCESS,false);
                            jsOutput.put(Config.FAILREASON,Config.SELECT_FULL);
                        }
                    }else {
                        jsOutput.put(Config.SUCCESS,false);
                        jsOutput.put(Config.FAILREASON,Config.SELECT_TIME_LOCATION_CONFLICT);
                    }
                }else {
                    jsOutput.put(Config.SUCCESS,false);
                    jsOutput.put(Config.FAILREASON,Config.COURSE_NOT_EXIST);
                }
            }else {
                jsOutput.put(Config.SUCCESS,false);
                jsOutput.put(Config.FAILREASON,Config.STUDENT_NOT_EXIST);
            }
        }catch (Exception e){
e.printStackTrace();
        }
        return jsOutput;
    }

    public static JSONObject dropCourse(JSONObject jsInput){
        JSONObject jsOutput = new JSONObject();
        try {
            //TODO 代码写在这里
            //TODO 判断 学号不存在、课程不存在、未选
            String studentId = jsInput.getString("studentId");
            String courseId = jsInput.getString("courseId");
            if (!Judge.isStudent(studentId)){//学号存在
                if (!Judge.isCourse(courseId)){//课程存在
                    if (Judge.isSelect(studentId,courseId)){//学生已选这门课

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
                        dacCou.updateByCondition(conditionCou, setterCou);

                        jsOutput.put(Config.SUCCESS,true);
                    }else {
                        jsOutput.put(Config.SUCCESS,false);
                        jsOutput.put(Config.FAILREASON,Config.STUDENT_NOT_SELECT);
                    }
                }else {
                    jsOutput.put(Config.SUCCESS,false);
                    jsOutput.put(Config.FAILREASON,Config.COURSE_NOT_EXIST);
                }
            }else {
                jsOutput.put(Config.SUCCESS,false);
                jsOutput.put(Config.FAILREASON,Config.STUDENT_NOT_EXIST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsOutput;
    }

    public static void clearData(){

    }
}
