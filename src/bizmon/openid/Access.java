/*
 * (c) Copyright 2011, iPay (Pty) Ltd
 */
package bizmon.openid;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;

/**
 *
 * @author evans
 */
public class Access {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // API entry point
            ConsumerManager manager = new ConsumerManager();

            // Get available endpoints
            List<?> discoveries = manager.discover("https://www.google.com/accounts/o8/id");

            // Bind to endpoint
            DiscoveryInformation discovered = manager.associate(discoveries);

            // Create the auth request, providing return URL
            AuthRequest authReq = manager.authenticate(discovered, request.getRequestURL().toString());

            // Redirects to provider login page
            response.sendRedirect(authReq.getDestinationUrl(true));

        } catch (Exception e) {

            throw new ServletException(e);
        }
    }
}
