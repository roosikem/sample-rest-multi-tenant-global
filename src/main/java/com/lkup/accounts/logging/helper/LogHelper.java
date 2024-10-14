
package com.lkup.accounts.logging.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lkup.accounts.logging.context.LoggingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;

public class LogHelper {

    public static void logInfo(Class<?> clazz, String message, LogLevel logLevel) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logWithContext(logger, message, logLevel);
    }

    public static void logInfo(Logger logger, Object message, LogLevel logLevel) {
        logWithContext(logger, message, logLevel);
    }

    private static void logWithContext(Logger logger, Object message, LogLevel logLevel) {
        LoggingContext context = LoggingContext.getCurrentContext();
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (context != null) {
                logger.info("[RequestId: {}] [Method: {}] [URL: {}] [Headers: {}] [Params: {}] [PathVars: {}] - {}",
                        context.getRequestId(),
                        context.getMethod(),
                        context.getRequestUrl(),
                        context.getRequestHeaders(),
                        context.getRequestParams(),
                        context.getPathVariables(),
                        mapper.writeValueAsString(message));
            } else {
                logger.info(mapper.writeValueAsString(message));
            }
        }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
        }
    }
}
    