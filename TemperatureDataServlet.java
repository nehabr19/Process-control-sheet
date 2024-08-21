package com.tap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/TemperatureDataServlet")
public class TemperatureDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        List<TemperatureData> temperatureList = new ArrayList<>();

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/jee3";
        String uname = "root";
        String password = "root";

        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(url, uname, password)) {
                // Query to retrieve temperature data
                String sql = "SELECT temperature, time FROM temperature_data";
                try (PreparedStatement pstmt = con.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {
                    // Fetch data from the result set
                    while (rs.next()) {
                        double temperature = rs.getDouble("temperature");
                        String time = rs.getString("time");
                        temperatureList.add(new TemperatureData(temperature, time));
                    }
                }
            }

            // Construct JSON manually
            StringBuilder jsonBuilder = new StringBuilder("[");
            for (int i = 0; i < temperatureList.size(); i++) {
                TemperatureData data = temperatureList.get(i);
                jsonBuilder.append("{");
                jsonBuilder.append("\"temperature\": ").append(data.getTemperature()).append(",");
                jsonBuilder.append("\"time\": \"").append(data.getTime()).append("\"");
                jsonBuilder.append("}");
                if (i < temperatureList.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");

            // Send JSON response to the client
            out.print(jsonBuilder.toString());
            out.flush();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("Error occurred while fetching temperature data.");
        }
    }

    // Inner class to represent temperature data
    class TemperatureData {
        private double temperature;
        private String time;

        public TemperatureData(double temperature, String time) {
            this.temperature = temperature;
            this.time = time;
        }

        public double getTemperature() {
            return temperature;
        }

        public String getTime() {
            return time;
        }
    }
}
