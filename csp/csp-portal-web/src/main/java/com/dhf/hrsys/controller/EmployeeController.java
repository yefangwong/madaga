package com.dhf.hrsys.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/emp")
public class EmployeeController extends HttpServlet {
    public void doGet(HttpServletRequest request,
        HttpServletResponse response) {
        String type = request.getParameter("type");
        if (type == null || "search".equals(type)) {
            search(request, response);
        } else if ("add".equals(type)) {
            add(request, response);
        } else if ("update".equals(type)) {
            update(request, response);
        } else if ("delete".equals(type)) {
            delete(request, response);
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
    }

    private void add(HttpServletRequest request, HttpServletResponse response) {
    }

    private void search(HttpServletRequest request, HttpServletResponse response) {
    }
}
