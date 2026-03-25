package com.example.backend.servlet;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import org.apache.commons.text.StringEscapeUtils;

public class VulnerableServlet extends HttpServlet {

    // 🔴 Hardcoded Secret
    private static final String DB_PASSWORD = "admin123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        // 🔴 SQL Injection Vulnerability
        String userId = request.getParameter("id");
        String query = "SELECT * FROM users WHERE id = ?";

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test", "root", DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                out.println("User: " + rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔴 XSS Vulnerability
        String search = request.getParameter("q");
        out.println("<h1>" + search + "</h1>");

        // 🔴 Missing Input Validation
        String username = request.getParameter("username");
        String safeUsername = username == null ? "" : StringEscapeUtils.escapeHtml4(username);
        out.println("Welcome " + safeUsername);
    }
}
