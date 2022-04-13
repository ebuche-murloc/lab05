package servlets;

import model.UserProfile;
import services.AccountServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@WebServlet(urlPatterns = "/files")
public class MainServlet extends HttpServlet {

    AccountServices accountService = new AccountServices();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userName = "";
        try {
            UserProfile usersDataSetProfile = accountService.getUserBySessionId(req.getSession().getId());
            userName = usersDataSetProfile.getLogin();
        } catch (RuntimeException exception) {
            resp.sendRedirect("/login");
            return;
        }

        String homeFolder = "C:/Users/" + userName;

        Map<String, String[]> params = req.getParameterMap();
        String path = "";
        try {
            path = params.get("path")[0].replace('\\', '/');
        } catch (Exception ignored) { }

        if (!path.startsWith(homeFolder)) {
            System.out.println("hf: " + homeFolder);
            System.out.println("path: " + path);
            path = homeFolder;
        }

        String pathAdv = path.substring(0, path.lastIndexOf('/'));


        try {
            File[] files = new File(path).listFiles();
            ArrayList<Date> dates = new ArrayList<>();
            assert files != null;
            for(File file:files) {
                dates.add(new Date(file.lastModified()));
            }
            req.setAttribute("dates", dates);
            req.setAttribute("files", files);
        }
        catch (Exception e) {

        }


        req.setAttribute("name", "Devcolibri");
        req.setAttribute("date", new Date());
        req.setAttribute("path", path);
        req.setAttribute("pathAdv", pathAdv);
        req.getRequestDispatcher("mypage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        super.doPost(req, resp);
    }
}
