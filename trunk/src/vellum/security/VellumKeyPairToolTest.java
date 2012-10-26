/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

/**
 *
 * @author evan
 */
public class VellumKeyPairToolTest {
    VellumKeyPairTool tool = new VellumKeyPairTool();
    
    private void test() throws Exception {
        tool.genKeyPair();
        tool.printKeyPair(System.out);
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new VellumKeyPairToolTest().test();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
}
