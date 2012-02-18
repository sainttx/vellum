/*
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package bizserver.adhoc;

import vellum.sql.query.HtmlQueryExecutor;
import vellum.sql.query.QueryInfo;
import vellum.sql.query.QueryInfoMap;
import bizserver.common.PageHandler;

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
            logger.info(parameterMap);
            query.putAll(parameterMap);
            logger.info(query);
            executor.execute(query);
        }

    }
}
