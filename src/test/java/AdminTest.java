//import com.mindhash.MindhashApp.dao.AccessDao;
import com.mindhash.MindhashApp.dao.SessionTokenDao;
import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {
    private UserRegJAXB user;
    //private AccessDao accessDao;

    @BeforeEach
    void setUp() throws Exception{
        user = new UserRegJAXB();
        //accessDao = new AccessDao();
    }

    /*
    Admin should be able to log in
     */
    @Test
    public void testAdminlogin() {
        //Check that admin cannot login with wrong pass
        user.setEmail("admin@mindhash.com");
        user.setPassword("12345678A");
        assertFalse(UserDao.login(user).getRes());

        //Tests if user cannot be logged in if he enters incorrect password
        user.setPassword("Mindhash#21");
        assertTrue(UserDao.login(user).getRes());
    }

    /*
    Test that admintoken has adminrights
     */
    @Test
    public void testAdminright() {
        String newToken = UUID.randomUUID().toString();
        SessionTokenDao.setUserToken(newToken, "admin@mindhash.com");
        assertTrue(SessionTokenDao.getUserByToken(newToken).getIsadmin());
    }

    /*
    Test that new account isnotadmin
     */

}