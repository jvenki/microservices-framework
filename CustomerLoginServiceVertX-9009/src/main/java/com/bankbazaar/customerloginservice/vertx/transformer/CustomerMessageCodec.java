package com.bankbazaar.customerloginservice.vertx.transformer;

import com.bankbazaar.model.user.Customer;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class CustomerMessageCodec implements MessageCodec<Customer, Customer> {

    //Not implemented below as the transmission is not on wire
    @Override
    public void encodeToWire(Buffer buffer, Customer customer) {

    }

    //Not implemented below as the transmission is not on wire
    @Override
    public Customer decodeFromWire(int pos, Buffer buffer) {
        return null;
    }

    @Override
    public Customer transform(Customer customer) {
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
