package service;

public interface UserService {
    boolean register(String u, String p);
    //注册账号
    boolean ifUserNameNotExist(String u);
    //验证账号是否存在
    boolean login(String u, String p) ;
    //登录账号
}
