package mobi.servlet.login;

import vellum.util.Passwords;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mobi.entity.Person;
import mobi.storage.PersonConnection;
import mobi.server.Servlets;

/**
 *
 */
public class LoginHandler_1 {

    HttpServletRequest req;
    HttpServletResponse res;
    Connection connection;
    Map responseMap = new HashMap();

    public void handle(HttpServletRequest req, HttpServletResponse res) throws Exception {
        this.req = req;
        this.res = res;
        try {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            connection = Servlets.getConnection();
            PersonConnection personConnection = new PersonConnection(connection);
            Person person = personConnection.find(email);
            if (person == null) {
                responseMap.put("message", "Email not found");
            } else if (!Passwords.matches(password, person.getPasswordHash(), person.getPasswordSalt())) {
                responseMap.put("message", "Incorrect password");
            } else {
                responseMap.put("name", person.getPersonName());
                Servlets.createSession(req, res, person);
            }
        } catch (Exception e) {
            Servlets.warn(e);
            responseMap.put("message", "There is a technical glitch");
        } finally {
            Servlets.close(connection);
        }
        Servlets.writeResponseMap(res, responseMap);
    }

}
