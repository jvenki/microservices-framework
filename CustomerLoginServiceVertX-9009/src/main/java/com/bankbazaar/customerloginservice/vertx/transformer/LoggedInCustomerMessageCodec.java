package com.bankbazaar.customerloginservice.vertx.transformer;

import com.bankbazaar.customerloginservice.model.LoggedInCustomer;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class LoggedInCustomerMessageCodec implements MessageCodec<LoggedInCustomer, LoggedInCustomer> {

    //Not implemented below as the transmission is not on wire
    @Override
    public void encodeToWire(Buffer buffer, LoggedInCustomer customer) {

    }

    //Not implemented below as the transmission is not on wire
    @Override
    public LoggedInCustomer decodeFromWire(int pos, Buffer buffer) {
        return null;
    }

    @Override
    public LoggedInCustomer transform(LoggedInCustomer customer) {
        return customer;
    }

    @Override
    public String name() {
        // Each codec must have a unique name.
        // This is used to identify a codec when sending a message and for unregistering codecs.
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        // Always -1
        return -1;
    }
}
