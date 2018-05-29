package com.bankbazaar.customerloginservice.vertx.verticles;

import com.bankbazaar.customerloginservice.converter.LoggedInCustomerConverter;
import com.bankbazaar.customerloginservice.model.LoggedInCustomer;
import com.bankbazaar.customerloginservice.vertx.transformer.JacksonTransformer;
import com.bankbazaar.model.user.Customer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
// Prototype scope is needed as multiple instances of this verticle will be deployed
@Scope(SCOPE_PROTOTYPE)
public class HttpServerVerticle extends AbstractVerticle {

    private static final String CONFIG_HTTP_SERVER_PORT = "http.server.port";
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private String customerDBQueue = "customerdb.queue";

    @Autowired
    LoggedInCustomerConverter loggedInCustomerConverter;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer server = vertx.createHttpServer();

        int portNumber = config().getInteger(CONFIG_HTTP_SERVER_PORT, 9009);
        server
                .requestHandler(getRouter()::accept)
                .listen(portNumber, ar -> {
                    if (ar.succeeded()) {
                        LOGGER.info("HTTP server running on port " + portNumber);
                        startFuture.complete();
                    } else {
                        LOGGER.error("Could not start a HTTP server", ar.cause());
                        startFuture.fail(ar.cause());
                    }
                });
    }

    private Router getRouter() {
        Router router = Router.router(vertx);

        router.get("/customer/:id").handler(this::getCustomerByID);
        router.route("/customer*").handler(BodyHandler.create());
        router.post("/customer").handler(this::createCustomer);

        return router;
    }

    private void getCustomerByID(RoutingContext context) {
        String id = context.request().getParam("id");
        JsonObject request = new JsonObject().put("id", id);

        DeliveryOptions options = new DeliveryOptions().addHeader("action", "get-by-id");
        vertx.eventBus().send(customerDBQueue, request, options, reply -> {

            if (reply.succeeded()) {
                Customer customer = (Customer) reply.result().body();
                LoggedInCustomer loggedInCustomer = loggedInCustomerConverter.convertToV2Model(customer);
                respond(context, loggedInCustomer);
            } else {
                context.fail(reply.cause());
            }
        });
    }

    private void createCustomer(RoutingContext context) {
        Customer customer = new Customer();
        customer.setEmail("abc3@gmail.com");
        customer.setPassword("aaaaaaaa");
        customer.setFirstName("aaa");
        customer.setLastName("asasfd");

        DeliveryOptions options = new DeliveryOptions().addHeader("action", "create");
        vertx.eventBus().send(customerDBQueue, customer, options, reply -> {
            if (reply.succeeded()) {
                Customer createdCustomer = (Customer) reply.result().body();
                respond(context, createdCustomer);
            } else {
                context.fail(reply.cause());
            }
        });
    }

    private void respond(RoutingContext context, Object o) {
        JacksonTransformer jacksonTransformer = new JacksonTransformer();
        try {
            String string = jacksonTransformer.toString(o);
            context.response().end(string);
        } catch (Exception e) {
            e.printStackTrace();
            context.fail(e.getCause());
        }
    }

    private Object decode(RoutingContext routingContext, Class clazz) {
        String payload = routingContext.getBodyAsString();
        JacksonTransformer jacksonTransformer = new JacksonTransformer();
        try {
            return jacksonTransformer.toObject(payload, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
