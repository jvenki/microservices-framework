package com.bankbazaar.customerloginservice.vertx.verticles;

import com.bankbazaar.customerloginservice.CustomerLoginService;
import com.bankbazaar.dao.CustomerDao;
import com.bankbazaar.model.user.Customer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
// Prototype scope is needed as multiple instances of this verticle will be deployed
@Scope(SCOPE_PROTOTYPE)
public class CustomerDBVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDBVerticle.class);

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CustomerLoginService customerLoginService;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("customerdb.queue", this::onMessage);  // <3>
        startFuture.complete();
    }

    public enum ErrorCodes {
        NO_ACTION_SPECIFIED,
        BAD_ACTION,
        DB_ERROR
    }

    public void onMessage(Message<Object> message) {

        if (!message.headers().contains("action")) {
            LOGGER.error("No action header specified for message with headers {} and body {}",
                    message.headers());
            message.fail(ErrorCodes.NO_ACTION_SPECIFIED.ordinal(), "No action header specified");
            return;
        }
        String action = message.headers().get("action");
        switch (action) {
            case "get-by-id":
                this.getById(message);
                break;
            case "create":
                this.createCustomer(message);
                break;
            default:
                message.fail(ErrorCodes.BAD_ACTION.ordinal(), "Bad action: " + action);
        }
    }

    private void getById(Message<Object> message) {
        JsonObject jsonObject = (JsonObject) message.body();
        Long id_long = Long.valueOf(jsonObject.getString("id"));
        vertx.executeBlocking(future -> {
            Customer customer = customerDao.get(id_long);
            future.complete(customer);
        }, res -> message.reply(res.result()));
    }

    private void createCustomer(Message<Object> message) {
        Customer customer = (Customer) message.body();
        vertx.executeBlocking(future -> {
            Customer savedCustomer = customerDao.save(customer);
            future.complete(savedCustomer);
        }, res -> message.reply(res.result()));
    }

    private void reportQueryError(Message<Object> message, Throwable cause) {
        LOGGER.error("Database query error", cause);
        message.fail(ErrorCodes.DB_ERROR.ordinal(), cause.getMessage());
    }
}
