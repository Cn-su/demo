package service;

import org.json.JSONArray;

public interface StudentService {
    void initDB();
    //初始化数据库信息
    boolean addStudent(String sno, String sname, String sdatebirth, String ssex, String snativeplace, String shouseaddress, String snation);
    //添加操作
    boolean isExist(String sno);
    //查重复操作
    JSONArray queryStudent(String key, String value);
    //根据key字段查询
    JSONArray queryStudents();
    //查询所有
    boolean deleteStudent(String sno);
    //根据学号删除
    boolean updateStudent(String sno, String sname, String sdatebirth, String ssex, String snativeplace, String shouseaddress, String snation, String snoOld);
    //根据旧学号进行修改
    boolean updateResult(String sno, String chinese, String math, String english);
    //根据学号修改成绩
}
