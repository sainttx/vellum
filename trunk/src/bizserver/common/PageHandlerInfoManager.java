/*
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */

package bizserver.common;

import bizserver.adhoc.AdhocQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author evanx
 */
public class PageHandlerInfoManager {
    Map<String, PageHandlerInfo> handlerInfoMap = new TreeMap();
    List<PageHandlerInfo> handlerInfoList = new ArrayList();
    PageHandlerInfo info;

    public PageHandlerInfoManager() {
        add(AdhocQuery.class);
    }

    protected final void add(Class type) {
        add(new PageHandlerInfo(type.getSimpleName(), type.getSimpleName(), type));
    }

    protected PageHandlerInfo add(PageHandlerInfo info) {
        this.info = info;
        handlerInfoList.add(info);
        handlerInfoMap.put(info.getName(), info);
        return info;
    }

    public Map<String, PageHandlerInfo> getHandlerInfoMap() {
        return handlerInfoMap;
    }

    public List<PageHandlerInfo> getHandlerInfoList() {
        return handlerInfoList;
    }

    public static PageHandlerInfoManager getInstance() {
        return instance;
    }

    public static PageHandlerInfoManager instance = new PageHandlerInfoManager();
}
