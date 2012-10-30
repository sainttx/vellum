/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.httphandler.common.AbstractPageHandler;
import crocserver.storage.common.CrocStorage;
import java.io.IOException;

/**
 *
 * @author evans
 */
public class AccessHomeHandler extends AbstractPageHandler {

    CrocStorage storage;

    public AccessHomeHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    public void handle() throws IOException {
        htmlPrinter.div("menuBarDiv");
        htmlPrinter.a_("/", "Home");
        htmlPrinter._div();
    }
    
}
