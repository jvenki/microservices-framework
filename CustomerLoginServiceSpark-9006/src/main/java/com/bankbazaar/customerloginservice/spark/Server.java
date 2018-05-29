package com.bankbazaar.customerloginservice.spark;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.AbstractNCSARequestLog;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;
import spark.embeddedserver.jetty.JettyServerFactory;

import java.io.IOException;

import static spark.Spark.port;

public class Server {
    public static void startServer(int portNumber) {
        AbstractNCSARequestLog requestLogger = createRequestLogger();
        EmbeddedJettyFactory factory = createServerFactory(requestLogger);
        EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, factory);
        port(portNumber);
    }

    private static AbstractNCSARequestLog createRequestLogger() {
        Logger logger = Logger.getLogger(Server.class);
        return new AbstractNCSARequestLog() {
            @Override
            protected boolean isEnabled() {
                return true;
            }

            @Override
            public void write(String s) throws IOException {
                logger.info(s);
            }
        };
    }

    private static EmbeddedJettyFactory createServerFactory(AbstractNCSARequestLog requestLog) {
        JettyServerFactory jettyServerFactory = new JettyServerFactory() {
            @Override
            public org.eclipse.jetty.server.Server create(int maxThreads, int minThreads, int threadTimeoutMillis) {
                org.eclipse.jetty.server.Server server;
                if (maxThreads > 0) {
                    int max = maxThreads > 0 ? maxThreads : 200;
                    int min = minThreads > 0 ? minThreads : 8;
                    int idleTimeout = threadTimeoutMillis > 0 ? threadTimeoutMillis : '\uea60';
                    server = new org.eclipse.jetty.server.Server(new QueuedThreadPool(max, min, idleTimeout));
                } else {
                    server = new org.eclipse.jetty.server.Server();
                }
                server.setRequestLog(requestLog);
                return server;
            }

            @Override
            public org.eclipse.jetty.server.Server create(ThreadPool threadPool) {
                return null;
            }
        };

        return new EmbeddedJettyFactory(jettyServerFactory);
    }
}
