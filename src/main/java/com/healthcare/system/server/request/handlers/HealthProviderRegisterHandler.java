package com.healthcare.system.server.request.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.system.controllers.HealthProviderController;
import com.healthcare.system.dto.HealthProviderDTO;
import com.healthcare.system.dto.ResponseCrudDTO;
import com.healthcare.system.dependency.injection.Context;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HealthProviderRegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        HealthProviderDTO requestObject = mapper.readValue(requestBody, HealthProviderDTO.class);

        ResponseCrudDTO<Void> responseCrud = new HealthProviderController(Context.healthProviderService).register(requestObject);
        String jsonResponse = mapper.writeValueAsString(responseCrud);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(responseCrud.status, jsonResponse.length());

        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }
}
