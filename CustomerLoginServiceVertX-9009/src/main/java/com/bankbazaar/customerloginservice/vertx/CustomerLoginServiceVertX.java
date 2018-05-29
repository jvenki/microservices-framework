package com.bankbazaar.customerloginservice.vertx;

import com.bankbazaar.customerloginservice.model.LoggedInCustomer;
import com.bankbazaar.customerloginservice.vertx.transformer.CustomerMessageCodec;
import com.bankbazaar.customerloginservice.vertx.transformer.LoggedInCustomerMessageCodec;
import com.bankbazaar.customerloginservice.vertx.verticles.CustomerDBVerticle;
import com.bankbazaar.customerloginservice.vertx.verticles.HttpServerVerticle;
import com.bankbazaar.customerloginservice.vertx.verticles.SpringVerticleFactory;
import com.bankbazaar.model.user.Customer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.function.Consumer;

public class CustomerLoginServiceVertX extends AbstractVerticle {
    public static void main(String[] args) {
        initializeVertX(CustomerLoginServiceVertX.class);
    }

    public static void initializeVertX(Class clazz) {
        String classDir = "src/main/java" + clazz.getPackage().getName().replace(".", "/");
        VertxOptions options = new VertxOptions().setClustered(false);
        System.setProperty("vertx.cwd", classDir);
        Consumer<Vertx> runner = vertx -> {
            try {
                vertx.deployVerticle(clazz.getName());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
        Vertx vertx = Vertx.vertx(options);
        runner.accept(vertx);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("sampleCustomerLoginServiceContext.xml", "*/applicationContext.xml");
        VerticleFactory verticleFactory = context.getBean(SpringVerticleFactory.class);
        // The verticle factory is registered manually because it is created by the Spring container
        vertx.registerVerticleFactory(verticleFactory);

        EventBus eventBus = getVertx().eventBus();
        eventBus.registerDefaultCodec(Customer.class, new CustomerMessageCodec());
        eventBus.registerDefaultCodec(LoggedInCustomer.class, new LoggedInCustomerMessageCodec());
        Future<String> dbVerticleDeployment = Future.future();
        vertx.deployVerticle(verticleFactory.prefix() + ":" + CustomerDBVerticle.class.getName(), dbVerticleDeployment.completer());

        dbVerticleDeployment.compose(id -> {
            Future<String> httpVerticleDeployment = Future.future();
            vertx.deployVerticle(
                    verticleFactory.prefix() + ":" + HttpServerVerticle.class.getName(),
                    httpVerticleDeployment.completer());
            return httpVerticleDeployment;
        }).setHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }
}
