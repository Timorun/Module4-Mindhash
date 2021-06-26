import com.mindhash.MindhashApp.Security.TokenUtils;
import com.mindhash.MindhashApp.dao.PasswordResetTokenDao;
import com.mindhash.MindhashApp.dao.SessionTokenDao;
import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        assertFalse(userDao.register(user).getRes());

        //Tests if user can be registered if email has not been taken yet
        //Also tests if email token is sent to user's email account after registration
        user.setEmail("random-" + UUID.randomUUID().toString() + "@example.com");
        assertTrue(userDao.register(user).getRes());
    }

    /*
    User should be able to login after their registration
     */
    @Test
    public void testUserLogin() {
        //Test if user can be logged in if he enters correct password
        user.setEmail("admin@mindhash.com");
        user.setPassword("Mindhash#21");
        assertTrue(userDao.login(user).getRes());

        //Tests if user cannot be logged in if he enters incorrect password
        user.setPassword("12345678A");
        assertFalse(userDao.login(user).getRes());

    }

    /*
    * User should be able to autologin if their session token hasn't expired
    * If their token hasn't expired, token's expiry time is updated according to a current time
    * If their token is expired, token's expiry time remains the same
    */
    @Test
    public void testCheckUserByTokenAndUpdate() {
        //Tests if user's unexpired session token is updated
        String newToken = UUID.randomUUID().toString();
        SessionTokenDao.setUserToken(newToken, "admin@mindhash.com");
        assertTrue(SessionTokenDao.checkUserByTokenAndUpdate(newToken));
        //Tests if user's expired session token is not updated
        SessionTokenDao.setTokenExpired(newToken);
        assertFalse(SessionTokenDao.checkUserByTokenAndUpdate(newToken));
    }

    /*
    User should be able to reset their password
     */
    @Test
    public void testResetPassword() {
        user.setEmail("admin@mindhash.com");
        user.setPassword("Mindhash#2");
        //Tests if password reset token is sent to user's email
        assertTrue(userDao.resetPasssword(user).getRes());

        //Tests if user's password can be reset
        String token = new TokenUtils().generatePasswordResetToken();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        PasswordResetTokenDao.setPasswordToken(token, user.getEmail());
        NewPassword newPassword = new NewPassword();
        newPassword.setPassword("Mindhash#21");
        newPassword.setToken(passwordResetToken.getToken());
        assertTrue(userDao.confirmNewPassword(newPassword).getRes());

        //Tests if user's password cannot be reset if token has expired
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MjQ2MjQxMjB9.nMXsmO7W4Jg-18opX63oGBSq02sLJO1bm2mG_aAKx6rUsW4Jl25qC4s9V_JrlhhI5ogC_ZEVi90NgjSADlkfxA";
        newPassword.setPassword("#Mindhash#2");
        newPassword.setToken(expiredToken);
        assertEquals("The password reset token has expired. Please try again.", userDao.confirmNewPassword(newPassword).getMsg());

        //Tests if user's password cannot be reset if token does not exist
        newPassword.setToken(passwordResetToken.getToken() + "1");
        assertFalse(userDao.confirmNewPassword(newPassword).getRes());
    }

}
