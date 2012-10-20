/*
 * Copyright 2011, Evan Summers
 * Apache Software License 2.0
 */
package vellumexp.servlet;

/**
 *
 * @author evan
 */
public interface HttpHandler {
 
    public void handle(HttpExchange httpExchange) throws Exception;
    
}
