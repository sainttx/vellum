/*
 * Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */

package server.common;

/**
 *
 * @author evanx
 */
public class PageHandlerInfo {
    String name;
    String label;
    Class type;
    PageHandlerType handlerType = PageHandlerType.COMMAND;

    public PageHandlerInfo(String name, String label, Class type) {
        this.name = name;
        this.label = label;
        this.type = type;
    }

    public void setHandlerType(PageHandlerType handlerType) {
        this.handlerType = handlerType;
    }

    public PageHandlerType getHandlerType() {
        return handlerType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }


}
