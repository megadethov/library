/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import calc.CalcOperations;
import calc.OperationType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mega
 */
public class CalcServlet extends HttpServlet {

    private List<String> listOperations = new ArrayList<>(); // поле класса не потокобезопасно

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CalcServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CalcServlet at " + request.getContextPath() + "</h1>");

            try {
                // считывание параметров
                double one = Double.valueOf(request.getParameter("one").toString()).doubleValue();
                double two = Double.parseDouble((request.getParameter("two"))); // мой короче
                String operation = String.valueOf(request.getParameter("operation"));

                // определение или создание сессии
                HttpSession session = request.getSession(true);

                // получение типа операции
                OperationType operType = OperationType.valueOf(operation.toUpperCase());

                // калькуляция
                double result = calcResult(operType, one, two);

                // для новой сессии создаем новый список
                if (session.isNew()) {
                    listOperations.clear();
                }
//                else { // иначе получаем список из атрибутов сессии
//                    listOperations = (List<String>) session.getAttribute("formula");
//                }

                // добавление новой операции в список и атрибут сессии
                listOperations.add(one + " " + operType.getStringValue() + " " + two + " = " + result);
                session.setAttribute("formula", listOperations);
                
                  // вывод всех операций
            out.println("<h1>ID вашей сессии равен: " + session.getId() + "</h1>");
            out.println("<h3>Список операций (всего:" + listOperations.size() + ") </h3>");

            for (String oper : listOperations) {
                out.println("<h3>" + oper + "</h3>");
            }

            } catch (Exception e) {
                // предупреждение пользователю в случае ошибки
            out.println("<h3 style=\"color:red;\">Возникла ошибка. Проверьте параметры</h3>");
            out.println("<h3>Имена параметров должны быть one, two, operation</h3>");
            out.println("<h3>Operation должен принимать 1 из 4 значений: add, subtract, multiply, divide</h3>");
            out.println("<h3>Значения one и two должны быть числами</h3>");
            out.println("<h1>Пример</h1>");
            out.println("<h3>?one=1&two=3&operation=add</h3>");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            finally {
            out.println("</body>");
            out.println("</html>");
            out.close();
        }        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

// калькуляция
    private double calcResult(OperationType operType, double one, double two) {
        double result = 0;
        switch (operType) {
            case ADD:
                result = CalcOperations.add(one, two);
                break;
            case SUBTRACT:
                result = CalcOperations.subtract(one, two);
                break;
            case MULTIPLY:
                result = CalcOperations.multiply(one, two);
                break;
            case DIVIDE:
                result = CalcOperations.divide(one, two);
                break;
        }
        return result;
    }
}
