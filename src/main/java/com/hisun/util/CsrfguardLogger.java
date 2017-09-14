package com.hisun.util;

import org.apache.log4j.Logger;
import org.owasp.csrfguard.log.ILogger;
import org.owasp.csrfguard.log.LogLevel;

import java.util.Date;

/**
 * <p>类名称:CsrfguardLogger</p>
 * <p>类描述:</p>
 * <p>公司:湖南海数互联信息技术有限公司</p>
 *
 * @创建者:init
 * @创建人:16/7/27下午4:10
 * @创建人联系方式:init@hn-hisun.com
 */
public class CsrfguardLogger implements ILogger {

    Logger logger = Logger.getLogger(CsrfguardLogger.class);

    @Override
    public void log(String msg) {
        log(LogLevel.Info, msg);
    }

    @Override
    public void log(LogLevel level, String msg) {
        if(LogLevel.Error.equals(level)) {
            logger.error(String.format("[%s] [%s] %s", new Date(), String.valueOf(level), msg));
        } else {
            logger.info(String.format("[%s] [%s] %s", new Date(), String.valueOf(level), msg));
        }
    }

    @Override
    public void log(Exception exception) {
        log(LogLevel.Error, exception);
    }

    @Override
    public void log(LogLevel level, Exception exception) {
        String.format("[%s] [%s] %s", new Date(), String.valueOf(level), String.valueOf(exception));
    }
}
