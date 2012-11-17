/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.httphandler.common.AbstractPageHandler;
import crocserver.storage.adminuser.AdminUser;
import vellum.html.HtmlPrinter;
import crocserver.storage.common.CrocStorage;

/**
 *
 * @author evans
 */
public class ViewUserHandler extends AbstractPageHandler {
    CrocStorage storage;

    public ViewUserHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
       HtmlPrinter p = new HtmlPrinter(out);
       String id = pathArgs[1];
       p.div("menuBarDiv");
       p.a_("/", "Home");
       p._div();
       p.span("pageTitle", String.format("User %s", id));
       AdminUser user = storage.getUserStorage().find(id);
       p.tableDiv("resultSet");
       p.thead();
       p._thead();
       p.tbody();
       p.trhd("Username", user.getId());
       p.trhd("Display name", user.getDisplayName());
       p.trhd("Email", user.getEmail());
       p.trhd("Cert subject", user.getSubject());
       p._tbody();
       p._tableDiv();
    }    
}
