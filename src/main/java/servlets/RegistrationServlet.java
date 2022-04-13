package servlets;

import services.dbService.DBException;
import services.dbService.DBService;
import model.UserProfile;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    //AccountService accountService = new AccountService();
    private DBService dbService = new DBService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("pass1");
        String passwordConfirm = req.getParameter("pass2");
        String email = req.getParameter("email");

        clearErrors(req);

        boolean errorStatus = false;
        try {
            errorStatus = checkErrors(req, login, password, passwordConfirm, email);
        } catch (DBException e) {
            e.printStackTrace();
        }

        if (errorStatus) {
            req.setAttribute("login", login);
            req.setAttribute("pass1", password);
            req.setAttribute("pass2", passwordConfirm);
            req.setAttribute("email", email);
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
        } else {
            UserProfile userProfile = new UserProfile(login, password, email);

            dbService.addUser(userProfile);
            resp.sendRedirect("/lab02_war/login");
        }
    }

    private boolean checkErrors(HttpServletRequest req, String login, String firstPassword, String secondPassword,
                                String email) throws DBException {

        if (login == null || login.equals("")) {
            req.setAttribute("loginErr", "Поле не заполнено");
        } else if (firstPassword == null || firstPassword.equals("")) {
            req.setAttribute("pass1Err", "Поле не заполнено");
        } else if (secondPassword == null || secondPassword.equals("")) {
            req.setAttribute("pass2Err", "Поле не заполнено");
        } else if (email == null || email.equals("")) {
            req.setAttribute("emailErr", "Поле не заполнено");
        } else if (!firstPassword.equals(secondPassword)) {
            req.setAttribute("pass2Err", "Пароли не совпадают");
        } else if (dbService.getUser(login).getLogin() != null) {
            req.setAttribute("loginErr", "Данный логин уже существует");
        } else return false;
        return true;
    }

    private void clearErrors(HttpServletRequest req) {
        req.setAttribute("loginErr", "");
        req.setAttribute("pass1Err", "");
        req.setAttribute("pass2Err", "");
        req.setAttribute("emailErr", "");
    }
}