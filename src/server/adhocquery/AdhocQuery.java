/*
 * Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package server.adhocquery;

import server.common.PageHandler;
import vellum.query.HtmlQueryExecutor;
import vellum.query.QueryInfo;
import vellum.query.QueryInfoMap;

/**
 *
 * @author evanx
 */
public class AdhocQuery extends PageHandler {

    public AdhocQuery() {
        super(false);
    }

    @Override
    protected void handle() throws Exception {
        logger.info(path);
        QueryInfoMap queryInfoManager = new QueryInfoMap(getClass(), "query.sql");
        if (path.endsWith(".sql")) {
            int index = path.lastIndexOf("/");
            String name = path.substring(index + 1);
            queryInfoManager = new QueryInfoMap(getClass(), name);
        }
        boolean verbose = queryInfoManager.getOptionList().contains("\\verbose");
        if (parameterMap.containsKey("verbose")) {
            verbose = true;
        }
        if (parameterMap.containsKey("schema")) {
            String schema = parameterMap.get("schema");
            queryInfoManager.setSchema(schema);
        }
        HtmlQueryExecutor executor = new HtmlQueryExecutor(5432, out, verbose);
        for (QueryInfo query : queryInfoManager.getList()) {
            logger.trace("query", query, parameterMap);
            query.putAll(parameterMap);
            executor.execute(query);
        }

    }
}
