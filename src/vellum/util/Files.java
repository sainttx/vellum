/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package vellum.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.exception.ArgsRuntimeException;
import vellum.exception.Exceptions;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import vellum.exception.SizeRuntimeException;

/**
 *
 * @author evanx
 */
public class Files {

    public static final String fileSeparator = System.getProperty("file.separator");
    public static final String userHomeDir = System.getProperty("user.home");
    public static Logr logger = LogrFactory.getLogger(Files.class);

    public static BufferedReader newBufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static BufferedReader newBufferedGzip(String fileName) {
        try {
            File file = newFile(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static BufferedReader newBufferedReader(String fileName) {
        if (fileName.endsWith(".gz")) {
            return newBufferedGzip(fileName);
        }
        try {
            File file = newFile(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static BufferedReader newBufferedReaderTail(String fileName, long length) {
        try {
            String command = String.format("tail -%d %s", length, fileName);
            InputStream inputStream = exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static BufferedReader newBufferedReaderTailFollow(String fileName, long length) {
        try {
            String command = String.format("tail -f %s", fileName);
            InputStream inputStream = exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static BufferedReader newBufferedReaderEnd(String fileName, long length) {
        try {
            File file = newFile(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            if (file.length() > length) {
                reader.skip(file.length() - length);
            }
            reader.readLine();
            return reader;
        } catch (IOException e) {
            throw new ArgsRuntimeException(e, null, fileName);
        }
    }

    public static File newFile(String fileName) {
        if (true) {
            return new File(fileName);
        }
        if (fileName.startsWith("/")) {
            return new File(fileName);
        } else {
            return new File(userHomeDir, fileName);
        }
    }

    public static String readString(Class parent, String resourceName) {
        return readString(getResourceAsStream(parent, resourceName));
    }

    protected static InputStream getResourceAsStream(Class type, String resourceName) {
        InputStream stream = type.getResourceAsStream(resourceName);
        if (stream == null) {
            throw new ArgsRuntimeException(type, resourceName);
        }
        return stream;
    }

    public static byte[] readBytes(String filePath) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            FileInputStream stream = new FileInputStream(filePath);
            while (true) {
                int b = stream.read();
                if (b < 0) {
                    return outputStream.toByteArray();
                }
                outputStream.write(b);
            }
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static byte[] readBytes(InputStream stream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            while (true) {
                int b = stream.read();
                if (b < 0) {
                    return outputStream.toByteArray();
                }
                outputStream.write(b);
            }
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static StringBuilder readStringBuilder(InputStream stream) {
        return new StringBuilder(readString(stream));
    }

    public static String readString(InputStream stream) {
        return new String(readBytes(stream));
    }

    public static char[] readChars(InputStream stream) {
        return Bytes.toCharArray(readBytes(stream));
    }

    public static InputStream exec(String command) throws IOException {
        logger.info(command);
        Process process = Runtime.getRuntime().exec(command);
        return process.getInputStream();
    }

    public static void process(LineProcessor processor, InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                processor.processLine(line);
            }
        } finally {
            reader.close();
        }
    }

    public static void close(ServerSocket closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Socket closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void renameTo(String srcFileName, String destFileName) {
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        srcFile.renameTo(destFile);
        logger.info("replaceFile", srcFileName, destFileName);
    }

    public static List<String> readLineList(InputStream stream, int capacity) {
        List<String> lineList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return lineList;
                }
                lineList.add(line);
                if (lineList.size() > capacity) {
                    throw new SizeRuntimeException(lineList.size());
                }
            } catch (Exception e) {
                throw Exceptions.newRuntimeException(e);
            }
        }
    }

    public static void read(InputStream stream, StringBuilder builder) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return;
                }
                if (builder != null) {
                    builder.append(line);
                    builder.append("\n");
                }
            } catch (Exception e) {
                throw Exceptions.newRuntimeException(e);
            }
        }
    }

    public static String readString(InputStream stream, long capacity) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    return builder.toString();
                }
                builder.append(line);
                builder.append("\n");
                if (builder.length() > capacity) {
                    throw new SizeRuntimeException(builder.length());
                }
            } catch (Exception e) {
                throw Exceptions.newRuntimeException(e);
            }
        }
    }

    public static void flush() {
        System.out.flush();
        System.err.flush();
    }

    public static void printStackTrace(Exception e) {
        flush();
        e.printStackTrace(System.err);
    }

    public static PrintWriter newPrintWriter(OutputStream outputStream) {
        return new PrintWriter(outputStream);
    }

    public static String baseName(String fileName) {
        int index = fileName.lastIndexOf(fileSeparator);
        if (index >= 0) {
            return fileName.substring(index + 1);
        }
        return fileName;
    }

    public static String removeFileNameExtension(File file) {
        return removeExtension(file.getName());

    }

    public static String removeExtension(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                fileName = fileName.substring(0, index);
                index = fileName.lastIndexOf('/');
                if (index >= 0) {
                    fileName = fileName.substring(index + 1);
                }
            }
        }
        return fileName;
    }

    public static void transmit(InputStream inputStream, OutputStream outputStream) throws IOException {
        while (true) {
            int b = inputStream.read();
            if (b < 0) {
                return;
            }
            outputStream.write(b);
        }
    }
}
