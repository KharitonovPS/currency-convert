package org.kps.currency.utils;

public class Utils {
    public static java.io.File changeExtension(java.io.File f, String newExtension) {
        int i = f.getName().lastIndexOf('.');
        String name = f.getName().substring(0,i);
        return new java.io.File( f.getParent(), name + newExtension);
    }
}
