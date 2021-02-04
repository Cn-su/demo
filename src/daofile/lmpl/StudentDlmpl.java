package daofile.lmpl;

import daofile.StudentD;
import infile.Result;
import utilfile.DatabaseConnection;
import infile.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class StudentDlmpl implements StudentD {  //初始化测试数据；
    @Override
    public void initDateStudentDB(){
        Connection con = DatabaseConnection.getConnection();
        try{
            Statement stms=con.createStatement();
            stms.addBatch("INSERT INTO `students` VALUES ('100001', '王浩', '1998-05-02', '男', '重庆', '地址地址地址', '汉族');");
            stms.addBatch("INSERT INTO `students` VALUES ('100002', '张洋', '1999-12-01', '女', '四川', '地址地址地址', '汉族');");
            stms.addBatch("INSERT INTO `students` VALUES ('100003', '李航', '1996-01-21', '男', '北京', '地址地址地址', '汉族');");
            stms.addBatch("INSERT INTO `students` VALUES ('100004', '吴辉', '1997-03-01', '男', '上海', '地址地址地址', '汉族');");
            stms.addBatch("INSERT INTO `students` VALUES ('100005', '常鑫', '1996-01-06', '男', '西安', '地址地址地址', '汉族');");
            stms.executeBatch();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int insert(Student s){//插入一条数据；
        Connection con = DatabaseConnection.getConnection();
        String sql = "insert into students values(?,?,?,?,?,?,?)";
        int rows = 0;
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1,s.getSno());
            pst.setString(2,s.getSname());
            pst.setString(3,s.getSdatebirth());
            pst.setString(4,s.getSsex());
            pst.setString(5,s.getSnativeplace());
            pst.setString(6,s.getShouseaddress());
            pst.setString(7,s.getSnation());
            rows = pst.executeUpdate();
            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    @Override
    public int updateResult(Result r) {
        Connection con = DatabaseConnection.getConnection();
        String sql = "select * from result where sno='"+r.getSno()+"'";
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            int count=0;
            while(rs.next()){
                count=rs.getInt(1);
            }
            if(count==0){
                sql="insert into result values(?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, r.getSno());
                pst.setString(2, r.getChinese());
                pst.setString(3, r.getMath());
                pst.setString(4, r.getEnglish());
                pst.executeUpdate();
                con.close();
                pst.close();
                return 1;
            }else{
                sql="update result set chinese=?,math=?,english=?where sno="+r.getSno();
                pst = con.prepareStatement(sql);
                pst.setString(1, r.getChinese());
                pst.setString(2, r.getMath());
                pst.setString(3, r.getEnglish());
                pst.executeUpdate();
                con.close();
                pst.close();
                return 1;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public int updateStudent(Student s, String snoOld){//修改数据
        int rows = 0;
        Connection con = DatabaseConnection.getConnection();
        String sql = "update students set sno=?,sname=?,sdatebirth=?,ssex=? ,snativeplace=? ,shouseaddress=? ,snation=? where sno=" + snoOld;
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1,s.getSno());
            pst.setString(2,s.getSname());
            pst.setString(3,s.getSdatebirth());
            pst.setString(4,s.getSsex());
            pst.setString(5,s.getSnativeplace());
            pst.setString(6,s.getShouseaddress());
            pst.setString(7,s.getSnation());
            rows = pst.executeUpdate();
            System.out.println(pst.toString());
            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
    @Override
    public List<Map> queryStudents(String k, String v){//查询
        Connection con = DatabaseConnection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        List<Map> rsList = new ArrayList<>();
        try{
            stmt = con.createStatement();
            String sql = "select * from students where " + k + " like '%" + v + "%'";
            rs = stmt.executeQuery(sql);//json数组  得到rs列数
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();//遍历
            resultSetToList(rs, rsList, metaData, columnCount);
            rs.close();
            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rsList;
    }
    @Override
    public List<Map> queryStudents(){//查询所有学生信息
        Connection con = DatabaseConnection.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        List<Map> rsList = new ArrayList<>();
        try{
            stmt = con.createStatement();
            String sql = "select * from students order by sno";
            rs = stmt.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            resultSetToList(rs, rsList, metaData, columnCount);
            rs.close();
            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rsList;
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
    @Override
    public int deleteStudent(String sno){//删除一条记录
        int rows = 0;
        Connection con = DatabaseConnection.getConnection();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            String sql = "delete from students where sno=" + sno;
            rows = stmt.executeUpdate(sql);
            sql = "delete from result where sno=" + sno;
            stmt.executeUpdate(sql);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
}
