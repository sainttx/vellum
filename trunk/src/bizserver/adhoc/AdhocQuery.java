/*
 * (c) Copyright 2010, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package bizserver.adhoc;

import bizdb.query.HtmlQueryExecutor;
import bizdb.query.QueryInfo;
import bizdb.query.QueryInfoMap;
import bizserver.common.PageHandler;

/**
 *
 * @author evans
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
            logger.info(parameterMap);
            query.putAll(parameterMap);
            logger.info(query);
            executor.execute(query);
        }

    }
}
