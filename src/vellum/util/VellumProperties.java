/*
       Source https://code.google.com/p/vellum by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
 */
package vellum.util;

import java.util.Properties;

/**
 *
 * @author evan.summers
 */
public class VellumProperties {

    public static final VellumProperties systemProperties = 
            new VellumProperties(System.getProperties());
    
    Properties properties;

    public VellumProperties(Properties properties) {
        this.properties = properties;
    }
        
    public String getString(String propertyName) {
        String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            throw new RuntimeException("Missing -D property: " + propertyName);
        }
        return propertyValue;
    } 

    public String getString(String propertyName, String defaultValue) {
        String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            return defaultValue;
        }
        return propertyValue;
    } 
    
    public int getInt(String propertyName) {
        String propertyString = properties.getProperty(propertyName);
        if (propertyString == null) {
            throw new RuntimeException("Missing -D property: " + propertyName);
        }
        return Integer.parseInt(propertyString);
    }
    
    public int getInt(String propertyName, int defaultValue) {
        String propertyString = properties.getProperty(propertyName);
        if (propertyString == null) {
            return defaultValue;
        }
        return Integer.parseInt(propertyString);
    }
    
    public boolean getBoolean(String propertyName, boolean defaultValue) {
        String string = properties.getProperty(propertyName);
        if (string == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(string);
    }    
    
    public char[] getPassword(String propertyName, char[] defaultValue) {
        String passwordString = properties.getProperty(propertyName);
        if (passwordString == null) {
            return defaultValue;
        }
        return passwordString.toCharArray();
    }
}
