/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.httphandler.common.AbstractPageHandler;
import crocserver.storage.adminuser.User;
import vellum.html.HtmlPrinter;
import crocserver.storage.CrocStorage;

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
       String id = pathArgs[2];
       p.div("menuBarDiv");
       p.a_("/", "Home");
       p._div();
       p.spanf("pageTitle", "User %s", id);
       User adminUser = storage.getUserStorage().find(id);
       p.tableDiv("resultSet");
       p.thead();
       p._thead();
       p.tbody();
       p.trhd("Username", adminUser.getId());
       p.trhd("Display name", adminUser.getDisplayName());
       p.trhd("Address", adminUser.getEmail());
       p._tbody();
       p._tableDiv();
       p.pre(adminUser.getPublicKey());
    }    
}
