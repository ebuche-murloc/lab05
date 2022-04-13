package servlets;

import model.UserProfile;
import services.AccountServices;
import services.dbService.DBException;
import services.dbService.DBService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    AccountServices accountService = new AccountServices();
    private DBService dbService = new DBService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProfile user = accountService.getUserBySessionId("sessionID-" + req.getRemoteAddr());
        if (user != null) {
            resp.sendRedirect("/lab02_war/files");

            return;
        }
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("pass");

        clearErrors(req);


        boolean errorStatus = false;
        try {
            errorStatus = checkErrors(req, login, password);
        } catch (DBException e) {
            e.printStackTrace();
        }

        if (errorStatus) {
            req.setAttribute("login", login);
            req.setAttribute("pass", password);
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        } else {
            UserProfile user = null;
            user = dbService.getUser(login);

            accountService.addSession(req.getSession().getId(), user);
            resp.sendRedirect("/lab02_war/files");
        }
    }

    private boolean checkErrors (HttpServletRequest req, String login, String password) throws
            DBException, DBException {

        if (login == null || login.equals("")) {
            req.setAttribute("loginErr", "Поле не заполнено");
        } else if (password == null || password.equals("")) {
            req.setAttribute("passErr", "Поле не заполнено");
        } else if (!dbService.checkUserExists(login)) {
            req.setAttribute("loginErr", "Аккаунта с таким логином не существует");
        } else if (!dbService.getUser(login).getPass().equals(password)) {
            req.setAttribute("passErr", "Неверный пароль");
        } else return false;
        return true;
    }

    private void clearErrors (HttpServletRequest req){
        req.setAttribute("loginErr", "");
        req.setAttribute("passErr", "");
    }
}