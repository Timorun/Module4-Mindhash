package com.mindhash.MindhashApp;
import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.model.*;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/login")
public class RegisterServlet extends HttpServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User(email, password);
        UserDao userDao = new UserDao();
        boolean newMail = userDao.newMail(email);
        if (!newMail) {
            request.getRequestDispatcher("/mailused.html").forward(request, response);
            return;
        }

        boolean isRegistered = false;
        try {
            isRegistered = userDao.registerUser(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(isRegistered) {
            request.getRequestDispatcher("/registration-successful.html").forward(request, response);
        } else {
            request.getRequestDispatcher("/index.html").forward(request, response);
        }

    }


}