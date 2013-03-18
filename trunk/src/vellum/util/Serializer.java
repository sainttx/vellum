/*
 * Apache Software License 2.0
 * Apache Software License 2.0, (c) Copyright 2013 Evan Summers
 */
package vellum.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author evanx
 */
public interface Serializer {

    public void writeObject(OutputStream stream) throws IOException;
    
    public void readObject(InputStream stream) throws IOException;

}
