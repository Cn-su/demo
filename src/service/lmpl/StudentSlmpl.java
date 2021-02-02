package service.lmpl;

import infile.Result;
import service.StudentService;
import daofile.StudentD;
import daofile.lmpl.StudentDlmpl;
import infile.Student;
import utilfile.DataType;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class StudentSlmpl implements StudentService{
    private static StudentD studentD = new StudentDlmpl();
    @Override//初始化数据
    public void initDB(){
        studentD.initDateStudentDB();
    }
    @Override//添加一个学生
    public boolean addStudent(String sno, String sname, String sdatebirth, String ssex, String snativeplace, String shouseaddress, String snation){
        boolean result;//返回值>0为true否者为false
        result = studentD.insert(new Student(sno, sname, sdatebirth, ssex, snativeplace, shouseaddress, snation))>0;
        return result;
    }
    @Override//查询学生是否存在
    public boolean isExist(String sno){
        boolean result;//返回的是一个json数组，如果这个json数组内存储的数据个数大于0则表示存在，result为true
        List<Map> rsList = studentD.queryStudents("sno", sno);
        JSONArray jsonArray= DataType.rsListToJsonArray(rsList);
        result = jsonArray.length() > 0;
        return result;
    }
    @Override//查询学生信息
    public JSONArray queryStudent(String key, String value){
        List<Map> rsList = studentD.queryStudents(key, value);
        return DataType.rsListToJsonArray(rsList);
    }
    @Override//查询所有学生信息
    public JSONArray queryStudents(){
        List<Map> rsList = studentD.queryStudents();
        return DataType.rsListToJsonArray(rsList);
    }
    @Override//删除学生
    public boolean deleteStudent(String sno){
        return studentD.deleteStudent(sno)>0;
    }
    @Override//修改学生信息
    public boolean updateStudent(String sno, String sname, String sdatebirth, String ssex, String snativeplace, String shouseaddress, String snation, String snoOld){
        return studentD.updateStudent(new Student(sno, sname, sdatebirth, ssex, snativeplace, shouseaddress, snation), snoOld) > 0;
    }
    @Override//修改学生成绩
    public boolean updateResult(String sno, String chinese, String math, String english){
        return studentD.updateResult(new Result(sno,chinese,math,english)) > 0;
    }
}
