package com.mindhash.MindhashApp;
import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.model.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;


public class RegisterServlet extends HttpServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User(email, password);
        UserDao userDao = new UserDao();
        boolean isRegistered = userDao.registerUser(user);

    }


}