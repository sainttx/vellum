/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        if (req.getRequestURI().indexOf("fbredirect") > 0) {
            res.setContentType("text/plain");
            Writer w = res.getWriter();
            for (String name : req.getParameterMap().keySet()) {
                w.write(name);
                w.write("=");
                w.write(req.getParameter(name));
                w.write("\n");
            }
            w.close();
        } else {
            HtmlBuilder builder = new HtmlBuilder(Streams.readString(getClass(), "fblogin.html"));
            builder.replace("fbAppId", MobiConfig.getProperty("fbAppId"));
            res.setContentType("text/html");
            Writer w = res.getWriter();
            w.write(builder.toString());
            w.close();
        }
    }
    
}
