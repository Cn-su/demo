package daofile;
import infile.Result;
import infile.Student;
import java.util.List;
import java.util.Map;
public interface StudentD {
    void initDateStudentDB();
    int insert(Student s);
    int updateStudent(Student s, String snoOld);
    List<Map> queryStudents(String k, String v);
    List<Map> queryStudents();
    int deleteStudent(String sno);
    int updateResult(Result r);
}
