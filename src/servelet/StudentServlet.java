package servelet;

import service.StudentService;
import service.lmpl.StudentSlmpl;
import utilfile.DataType;
import org.json.JSONArray;
import utilfile.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "StudentServlet", urlPatterns = "/StudentServlet")
public class StudentServlet extends HttpServlet {
    private final int MIN_STUDENT_SNO_LENGTH = 6;//学号长度限制
    private static StudentService studentService = new StudentSlmpl();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        PrintWriter pw = response.getWriter();
        System.out.println("收到的请求method：" + method);//根据不同的method调用不同的方法
        switch (method){
            case "addStudent":
                addStudent(request, response, pw);
                break;
            case "deleteStudent":
                deleteStudent(request, response, pw);
                break;
            case "updateStudent":
                updateStudent(request, response, pw);
                break;
            case "qureyStudent":
                qureyStudent(request, response, pw);
                break;
            case "loadStudent":
                loadStudent(request, response, pw);
                break;
            case "StuGrade":
                StuGrade(request, response, pw);
                break;
            case "searResult":
                searResult(request, response, pw);
                break;
            default:
        }
    }
    private void searResult(HttpServletRequest request, HttpServletResponse response, PrintWriter pw){
        String sno = request.getParameter("sno");
        Connection con = DatabaseConnection.getConnection();
        List<Map> rsList = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        String s1=null;
        String s2= null;
        String s3=null;
        int sum=0;
        String chinese=null;
        String math=null;
        String english=null;
        int no=1;
        String subject=" ";
        PreparedStatement pst = null;
        try{
            String sql = "select * from result where sno='"+sno+"'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int count=0;
            while(rs.next()){
                count=rs.getInt(1);
                chinese=rs.getString("chinese");
                math=rs.getString("math");
                english=rs.getString("english");
            }
            if(count==0){
                chinese="0";
                math="0";
                english="0";
                no=0;
                subject="0";
            }else{
                if(Integer.parseInt(chinese)<60)
                    subject="语文 ";
                if(Integer.parseInt(math)<60)
                    subject=subject+"数学 ";
                if(Integer.parseInt(english)<60)
                    subject=subject+"英语";
                if(Integer.parseInt(chinese)>=60 && Integer.parseInt(math)>=60 && Integer.parseInt(english)>=60)
                    subject="全部及格";
                sum=Integer.parseInt(chinese)+Integer.parseInt(math)+Integer.parseInt(english);
                sql = "select * from result where sno !='"+sno+"'";
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    s1=rs.getString("chinese");
                    s2=rs.getString("math");
                    s3=rs.getString("english");
                    if(Integer.parseInt(s1)+Integer.parseInt(s2)+Integer.parseInt(s3)>sum)
                        no++;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        try{
           String sql="insert into temp values(?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, chinese);
            pst.setString(2,math);
            pst.setString(3,english);
            pst.setString(4,Integer.toString(no));
            pst.setString(5,subject);
            pst.executeUpdate();
            sql = "select * from temp";
            rs = stmt.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();//遍历
            resultSetToList(rs, rsList, metaData, columnCount);
            sql = "delete from temp where ch=" + chinese;
            stmt.executeUpdate(sql);
            rs.close();
            stmt.close();
            pst.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        JSONArray jsonArray =DataType.rsListToJsonArray(rsList);
        pw.write(jsonArray.toString());
    }
    private void resultSetToList(ResultSet rs, List<Map> rsList, ResultSetMetaData metaData, int columnCount) throws SQLException {//将rs中的数据转存到list中
        while (rs.next()){
            HashMap rowData = new HashMap(columnCount);
            for (int i = 1; i <= columnCount; i++){
                String key = metaData.getColumnLabel(i);
                String value = rs.getString(key);
                rowData.put(key, value);
            }
            rsList.add(rowData);
        }
    }
    private void addStudent(HttpServletRequest request, HttpServletResponse response, PrintWriter pw){
        String sno = request.getParameter("sno");
        String sname = request.getParameter("sname");
        String sdatebirth = request.getParameter("sdatebirth");
        String ssex = request.getParameter("ssex");
        String snativeplace = request.getParameter("snativeplace");
        String shouseaddress = request.getParameter("shouseaddress");
        String snation = request.getParameter("snation");
        int n = 0;
        for(int i = 0; i < sname.length(); i++) {
            n = (int)sname.charAt(i);
            if(!(19968 <= n && n <40869)){
                pw.write("姓名非法");
                return;
            }
        }
        if (sno.length() != MIN_STUDENT_SNO_LENGTH){
            pw.write("学号必须为6位");
            return;
        }
        if (studentService.isExist(sno)){
            pw.write("学号已存在");
            return;
        }
        boolean result = studentService.addStudent(sno, sname, sdatebirth, ssex, snativeplace, shouseaddress, snation);
        if (result){
            pw.write("添加成功");
        }
        else{
            pw.write("添加失败");
        }
    }
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) {
        String sno = request.getParameter("sno");
            boolean result = studentService.deleteStudent(sno);
            if (result) {
                pw.write("删除成功");
            } else {
                pw.write("删除失败");
            }
        }
    private void StuGrade(HttpServletRequest request, HttpServletResponse response, PrintWriter pw){
        String sno = request.getParameter("sno");
        String chinese = request.getParameter("chinese");
        String math = request.getParameter("math");
        String english =request.getParameter("english");
            if(!DataType.isNum(chinese)){
                pw.write("语文成绩必须为数字");
            return;
            }
            if(Integer.parseInt(chinese)>100||Integer.parseInt(chinese)<0) {
                pw.write("语文成绩必须在0~100之间");
                return;
            }
        if(!DataType.isNum(math)){
            pw.write("数学成绩必须为数字");
            return;
        }
        if(Integer.parseInt(math)>100||Integer.parseInt(math)<0) {
            pw.write("数学成绩必须在0~100之间");
            return;
        }
        if(!DataType.isNum(english)){
            pw.write("英语成绩必须为数字");
            return;
        }
        if(Integer.parseInt(english)>100||Integer.parseInt(english)<0) {
            pw.write("英语成绩必须在0~100之间");
            return;
        }
        boolean result = studentService.updateResult(sno, chinese, math, english);
        if (result) {
            pw.write("修改成功");
        } else {
            pw.write("修改失败");
        }
    }
    private void updateStudent(HttpServletRequest request, HttpServletResponse response, PrintWriter pw){
        String sno = request.getParameter("sno");
        String snoOld = request.getParameter("sno_old");
        String sname = request.getParameter("sname");
        String sdatebirth = request.getParameter("sdatebirth");
        String ssex = request.getParameter("ssex");
        String snativeplace = request.getParameter("snativeplace");
        String shouseaddress = request.getParameter("shouseaddress");
        String snation = request.getParameter("snation");
        int n = 0;
        for(int i = 0; i < sname.length(); i++) {
            n = (int)sname.charAt(i);
            if(!(19968 <= n && n <40869)){
                pw.write("姓名非法");
                return;
            }
        }
        if (!DataType.isNum(sno)) {
            pw.write("学号必须为数字");
            return;
        }
        if (sno.length() !=6) {
            pw.write("学号必须为6位");
            return;
        }
        boolean result = studentService.updateStudent(sno, sname, sdatebirth, ssex, snativeplace, shouseaddress, snation, snoOld);
        if (result) {
            pw.write("修改成功");
        } else {
            pw.write("修改失败");
        }
    }
    private void qureyStudent(HttpServletRequest request, HttpServletResponse response, PrintWriter pw) throws UnsupportedEncodingException{
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        if (key == null || key.length() < 1) {
            pw.write("请选择索引字段");
            return;
        }
        if (value == null || value.length() < 1) {
            pw.write("请输入搜索内容");
            return;
        }
        JSONArray jsonArray = studentService.queryStudent(key, value);
        pw.write(jsonArray.toString());
    }
    private void loadStudent(HttpServletRequest request, HttpServletResponse response, PrintWriter pw){
        JSONArray jsonArray = studentService.queryStudents();
        if (jsonArray.length() < 1) {
            studentService.initDB();
        }
        pw.write(jsonArray.toString());
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
