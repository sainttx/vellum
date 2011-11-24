/*
 */
package mobi.servlet;

import bizmon.logger.Logr;
import bizmon.logger.LogrFactory;
import bizmon.util.Streams;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mobi.config.MobiConfig;

/**
 *
 * @author evan
 */
public class MobiServlet extends HttpServlet {
    Logr logr = LogrFactory.getLogger(getClass());
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        logr.info(req.getRequestURI());
        if (req.getRequestURI().indexOf("fbredirect.html") > 0) {
            new FbRedirect().service(req, res);
        } else if (req.getRequestURI().indexOf("login.html") > 0) {
            new Login().service(req, res);
        } else if (req.getRequestURI().indexOf("fbredirect.html") > 0) {
            new FbLogin().service(req, res);
        } else {
            new Login().service(req, res);
        }
    }    
    
}
