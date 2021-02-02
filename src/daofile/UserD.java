package daofile;
import infile.User;
public interface UserD {
    int add(User u);
    User queryUser(String username);
}
