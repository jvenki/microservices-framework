package com.jvenki.microservices.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.function.Consumer;

public class HelloVertXApplication extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
              if (req.path().equals("/greeting")) {
                  req.response()
                      .putHeader("content-type", "text/plain")
                      .end("Hello from Vert.x!");
              } else {
                  req.response()
                      .setStatusCode(500)
                      .putHeader("content-type", "text/plain")
                      .end("Check the URL");
              }
            }).listen(9005);
        System.out.println("HTTP server started on port 9005");
    }

    public static void initializeVertX() {
        String classDir = "src/main/java" + HelloVertXApplication.class.getPackage().getName().replace(".", "/");
        VertxOptions options = new VertxOptions().setClustered(false);
        System.setProperty("vertx.cwd", classDir);
        Consumer<Vertx> runner = vertx -> {
            try {
                vertx.deployVerticle(HelloVertXApplication.class.getName());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
        Vertx vertx = Vertx.vertx(options);
        runner.accept(vertx);
    }

    public static void main(String [] args) {
        initializeVertX();
    }
}
