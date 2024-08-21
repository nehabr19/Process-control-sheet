package com.tap;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            // Open a connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jee3", "root", "root");

            // Execute SQL query to check if user exists
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM members WHERE name=? AND password=?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // User exists and password matches, forward to home.html
                RequestDispatcher dispatcher = request.getRequestDispatcher("home.html");
                dispatcher.forward(request, response);
            }
            else {
            	PrintWriter out = response.getWriter();
                out.println("Username or Password incorrect!");
                out.close();
            }

            // Close resources
            rs.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            //response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("Exception: " + e.getMessage());
            out.close();
        }
    }
}
