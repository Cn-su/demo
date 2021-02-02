package service.lmpl;

import daofile.UserD;
import daofile.lmpl.UserDlmpl;
import service.UserService;
import infile.User;

public class UserSlmpl implements UserService{
    private UserD userD =new UserDlmpl();
    @Override//注册账号
    public boolean register(String u,String p){
        boolean result;//如果返回值大于1为true否者是false
        if (ifUserNameNotExist(u)){
             result=userD.add(new User(u, p))>0;
        }
        else {
            result=false;
        }
        return result;
    }
    @Override//验证是否已存在用户
    public boolean ifUserNameNotExist(String u){
        boolean result;//返回的是一个user对象，如果这个对象为空则表示用户名不存在
        result= userD.queryUser(u)==null;
        return result;
    }
    @Override//登录
    public boolean login(String u, String p){
        boolean result;
        User user = userD.queryUser(u);
        if(user.getUsername()!=null){
            result = u.equals(user.getUsername()) && p.equals(user.getPassword());
        }
        else result=false;
        return result;
    }
}

