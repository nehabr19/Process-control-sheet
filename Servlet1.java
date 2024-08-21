package com.tap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet1 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "jdbc:mysql://localhost:3306/jee3";
        String uname = "root";
        String password = "root";
        Connection con = null;
        PreparedStatement pstmt = null;
        PrintWriter out = null;
        String sql = "INSERT INTO members (name, lastname, emailid, password) VALUES (?, ?, ?, ?)";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, uname, password);
            pstmt = con.prepareStatement(sql);
            
            // Set parameters
            pstmt.setString(1, req.getParameter("name"));
            pstmt.setString(2, req.getParameter("lastname"));
            pstmt.setString(3, req.getParameter("emailid"));
            pstmt.setString(4, req.getParameter("password")); // Assuming password is submitted via a form field named "password"
            
            int n = pstmt.executeUpdate();
            out = resp.getWriter();
            
            if (n > 0) {
                out.print("Registration successful");
                RequestDispatcher dispatcher = req.getRequestDispatcher("index.html");
                dispatcher.forward(req, resp);
                
            } else {
                out.print("Registration unsuccessful");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close resources
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
