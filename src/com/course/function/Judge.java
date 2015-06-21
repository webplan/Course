package com.course.function;

import cn.edu.fudan.se.dac.Condition;
import cn.edu.fudan.se.dac.DACFactory;
import cn.edu.fudan.se.dac.DataAccessInterface;
import com.course.bean.CourseInfo;
import com.course.bean.SchoolInfo;
import com.course.bean.StudentInfo;
import com.course.bean.Time;

import java.util.List;

/**
 * Created by snow on 15-6-20.
 */
public class Judge {

    final static DataAccessInterface<SchoolInfo> dacSch = DACFactory.getInstance().createDAC(SchoolInfo.class);
    final static DataAccessInterface<StudentInfo> dacStu = DACFactory.getInstance().createDAC(StudentInfo.class);
    final static DataAccessInterface<CourseInfo> dacCou = DACFactory.getInstance().createDAC(CourseInfo.class);
    //  学院不存在 false
    public static boolean isSchool(String schoolName){

        Condition<SchoolInfo> conditionSch = new Condition<SchoolInfo>() {
            @Override
            public boolean assertBean(SchoolInfo SchoolInfo) {
                return SchoolInfo.getSchoolName().equals(schoolName);
            }
        };
        List list = dacSch.selectByCondition(conditionSch);
        if (list.size() == 0)
            return false;
        else
            return true;
    }

    //  学生不存在 false
    public static boolean isStudent(String studentId){
        Condition<StudentInfo> conditionStu = new Condition<StudentInfo>() {
            @Override
            public boolean assertBean(StudentInfo studentInfo) {
                return studentInfo.getStudentId().equals(studentId);
            }
        };
        List list = dacStu.selectByCondition(conditionStu);
        if (list.size() == 0)
            return false;
        else
            return true;
    }

    //  课程不存在 false
    public static boolean isCourse(String courseId){
        Condition<CourseInfo> conditionCou = new Condition<CourseInfo>() {
            @Override
            public boolean assertBean(CourseInfo courseInfo) {
                return courseInfo.getCourseId().equals(courseId);
            }
        };
        List list = dacCou.selectByCondition(conditionCou);
        if (list.size() == 0)
            return false;
        else
            return true;
    }

    //录入课程  时间地点唯一 不唯一（冲突）：false
    public static boolean isTimeOrLocation(Time time,String location){
        //选择所有课程
        Condition<CourseInfo> conditionCou = new Condition<CourseInfo>() {
            @Override
            public boolean assertBean(CourseInfo courseInfo) {
                if(courseInfo.getTime().getWeekday()==time.getWeekday()&&
                        courseInfo.getTime().getPeriod()==time.getPeriod())
                    if (courseInfo.getLocation().equals(location))
                        return true;
                return false;
            }
        };
        List list = dacCou.selectByCondition(conditionCou);
        if (list.size()==0)
            return true;
        else
            return false;
    }

    //录入课程 教师时间是否可选  不可选（冲突）: false
    public static boolean isTeacherTime(Time time,String teacherName){
        Condition<CourseInfo> conditionCou = new Condition<CourseInfo>() {
            @Override
            public boolean assertBean(CourseInfo courseInfo) {
                if(courseInfo.getTime().getWeekday()==time.getWeekday()&&
                        courseInfo.getTime().getPeriod()==time.getPeriod())
                    if (courseInfo.getTeacherName().equals(teacherName))
                        return true;
                return false;
            }
        };
        List list = dacCou.selectByCondition(conditionCou);
        if (list.size()==0)
            return true;
        else
            return false;
    }

    //录入学生：学号唯一  不唯一（冲突）：false
    public static boolean isStudentId(String studentId){
        Condition<StudentInfo> conditionStu = new Condition<StudentInfo>() {
            @Override
            public boolean assertBean(StudentInfo studentInfo) {
                if (studentInfo.getStudentId().equals(studentId))
                    return true;
                return false;
            }
        };
        List list = dacStu.selectByCondition(conditionStu);
        if (list.size()==0)
            return true;
        else
            return false;
    }

    //选课   时间地点是否唯一 冲突：false
    public static boolean isSelectTimeOrLocation(String studentId,String courseId){
        Condition<StudentInfo> conditionStu = new Condition<StudentInfo>() {
            @Override
            public boolean assertBean(StudentInfo studentInfo) {
                return studentInfo.getStudentId().equals(studentId);
            }
        };
        List<StudentInfo> list = dacStu.selectByCondition(conditionStu);
        List<String> stuCourseId = list.get(0).getCourseId();

        //获得学生已选课程
        Condition<CourseInfo> conditionCou = new Condition<CourseInfo>() {
            @Override
            public boolean assertBean(CourseInfo courseInfo) {
                if (stuCourseId.contains(courseInfo.getCourseId()))
                    return true;
                return false;
            }
        };
        List<CourseInfo> listCou = dacCou.selectByCondition(conditionCou);

        //获得所要选课程
        conditionCou = new Condition<CourseInfo>() {
            @Override
            public boolean assertBean(CourseInfo courseInfo) {
                return courseInfo.getCourseId().equals(courseId);
            }
        };
        CourseInfo course = (CourseInfo) dacCou.selectByCondition(conditionCou);
        //判断时间地点是否冲突
        for (int i=0;i<listCou.size();i++) {
            if (listCou.get(i).getLocation().equals(course.getLocation()))
                if (listCou.get(i).getTime().getPeriod()==course.getTime().getPeriod()&&
                        listCou.get(i).getTime().getWeekday()==course.getTime().getWeekday())
                    return false;
        }
        return true;
    }

    //TODO 学分   已满 false
    public static boolean isCredit(){
        return false;
    }

    // 选课人数已满
    public static boolean isSelectFull(String courseId){
        Condition<CourseInfo> conditionCou = new Condition<CourseInfo>() {
            @Override
            public boolean assertBean(CourseInfo courseInfo) {
                return courseInfo.getCourseId().equals(courseId);
            }
        };
        List<CourseInfo> list = dacCou.selectByCondition(conditionCou);
        if (list.get(0).getSurplus()>0)
            return true;
        return false;
    }

    //学生未选课 已选：true
    public static boolean isSelect(String studentId,String courseId){
        Condition<StudentInfo> conditionStu = new Condition<StudentInfo>() {
            @Override
            public boolean assertBean(StudentInfo studentInfo) {
                return studentInfo.getStudentId().equals(studentId);
            }
        };
        StudentInfo stu = dacStu.selectByCondition(conditionStu).get(0);
        List<String> course = stu.getCourseId();
        if (course.contains(courseId))
            return true;
        else
            return false;
    }
}
