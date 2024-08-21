package com.tap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

//@WebServlet("/uploadServlet")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "jdbc:mysql://localhost:3306/jee3";
        String uname = "root";
        String password = "root";
        String sql = "INSERT INTO temperature_data (temperature, time) VALUES (?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, uname, password);

            Part filePart = request.getPart("file");
            InputStream fileContent = filePart.getInputStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 2) {
                        double temperature = Double.parseDouble(data[0].trim());
                        String time = data[1].trim();

                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setDouble(1, temperature);
                        pstmt.setString(2, time);
                        pstmt.executeUpdate();
                        pstmt.close();
                    }
                }
            }

            con.close();
            //response.getWriter().println("Data uploaded successfully!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("tempchart.html");
            dispatcher.forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error occurred while uploading data.");
        }
    }
}
