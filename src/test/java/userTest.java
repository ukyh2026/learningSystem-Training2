import com.campus.dao.UserDao;
import com.campus.dao.impl.InMemoryUserDao;
import com.campus.model.Role;
import com.campus.model.Student;
import com.campus.model.Teacher;
import com.campus.model.User;
import com.campus.service.UserService;

public class userTest {
    public static void main(String[] args) {
        UserDao userDao = new InMemoryUserDao();
        UserService service = new UserService(userDao);
        User student = service.register("alice","123", Role.STUDENT);
        User teacher = service.register("teacher","123", Role.TEACHER);

        student.addTag("java1");
        student.addTag("java2");
        System.out.println(student.getTags());
        student.removeTag("java2");
        System.out.println(student.getTags());
    }
}
