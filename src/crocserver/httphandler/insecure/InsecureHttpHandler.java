/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 */
package crocserver.httphandler.insecure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan.summers
 */
public class InsecureHttpHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(InsecureHttpHandler.class);
    CrocApp app;
   
    public InsecureHttpHandler(CrocApp app) {
        this.app = app;
    }
        
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        new InsecureHomePageHandler(app).handle(httpExchange);
    }
}
