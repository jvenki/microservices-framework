package com.jvenki.microservices.spark;

import static spark.Spark.*;

public class HelloSparkApplication {
    public static void main(String[] args) {
        port(9004);
        get("/greeting", (req, res) -> "Hello SparkJava");
    }
}