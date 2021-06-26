import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.model.UserRegJAXB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.UUID;

public class UserDaoTest {
    private UserDao userDao;
    private UserRegJAXB user;

    @BeforeEach
    void setUp() throws Exception{
        userDao = new UserDao();
        user = new UserRegJAXB();
    }

    /*
    User should be able to sign up
     */
    @Test
    public void testUserRegistration() {
        //Tests if user cannot be registered if email has already been taken
        user.setEmail("admin@mindhash.com");
        user.setPassword("12345678A");
        user.setConfirmPassword("12345678A");
        assertEquals(false, userDao.register(user).getRes());

        //Tests if user can be registered if email has not been taken yet
        user.setEmail("random-" + UUID.randomUUID().toString() + "@example.com");
        assertEquals(true, userDao.register(user).getRes());

    }

    /*
    User should be able to login after his registration
     */
    @Test
    public void testUserLogin() {
        //Test if user can be logged in if he enters correct password
        user.setEmail("admin@mindhash.com");
        user.setPassword("Mindhash#2");
        assertEquals(true, userDao.login(user).getRes());

        //Tests if user cannot be logged in if he enters incorrect password
        user.setPassword("12345678A");
        assertEquals(false, userDao.login(user).getRes());

    }


}
