package org.talend.decanter.connect;

import java.net.InetAddress;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class LogbackDecanterAppender extends AppenderBase<ILoggingEvent> {
    private static final String MDC_IN_LOG_APPENDER = "inLogAppender";
    private final static String[] ignoredCategories = {"org.apache.karaf.decanter"};
    private final static Logger LOGGER = LoggerFactory.getLogger(LogbackDecanterAppender.class);

    private static EventAdmin dispatcher;

    protected void append(ILoggingEvent event) {
        try {
            if (MDC.get(MDC_IN_LOG_APPENDER) != null) {
                // Avoid recursion
                return;
            }
            MDC.put(MDC_IN_LOG_APPENDER, "true");
            appendInternal(event);
        } catch (Exception e) {
            LOGGER.warn("Error while appending event", e);
        } finally {
            MDC.remove(MDC_IN_LOG_APPENDER);
        }
    }

    private void appendInternal(ILoggingEvent event) throws Exception {
        if (isIgnored(event.getLoggerName())) {
            LOGGER.debug("{} logger is ignored by the log collector", event.getLoggerName());
            return;
        }

        LOGGER.debug("Publishing log event to the appenders ...");

        Map<String, Object> data = new HashMap<>();
        data.put("type", "log");
        String karafName = System.getProperty("karaf.name");
        if (karafName != null) {
            data.put("karafName", karafName);
        }

        data.put("hostAddress", InetAddress.getLocalHost().getHostAddress());
        data.put("hostName", InetAddress.getLocalHost().getHostName());

        data.put("timestamp", event.getTimeStamp());
        data.put("loggerName", event.getLoggerName());
        data.put("threadName", event.getThreadName());
        data.put("message", event.getMessage());
        data.put("level", event.getLevel().toString());
        data.put("MDC", event.getMDCPropertyMap());
        String loggerName = event.getLoggerName();
        if (loggerName == null || loggerName.isEmpty()) {
            loggerName = "default";
        }
        String topic = "decanter/collect/log/" + loggerName.replace(".", "/").replace(" ", "_").replace("{", "_").replace("}", "_").replace("$", "_");
        if (this.dispatcher != null) {
            this.dispatcher.postEvent(new Event(topic, data));
        }
    }

    private Object join(String[] throwableAr) {
        StringBuilder builder = new StringBuilder();
        for (String line : throwableAr) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    private boolean isIgnored(String loggerName) {
        if (loggerName == null) {
            return true;
        }
        for (String cat : ignoredCategories) {
            if (loggerName.startsWith(cat)) {
                return true;
            }
        }
        return false;
    }

    public static void setDispatcher(EventAdmin dispatcher) {
        LogbackDecanterAppender.dispatcher = dispatcher;
    }

}
