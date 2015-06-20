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
    //判断学院是否存在
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

    // 课程不存在 false
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

    //TODO 学分已满 false
    public static boolean isCredit(){
        return false;
    }

    //TODO 选课时间地点冲
    public static boolean isTimeOrLocation(Time time,String location){
        return true;
    }

    // TODO 选课人数已满
    public static boolean isFull(){
        return false;
    }
}
