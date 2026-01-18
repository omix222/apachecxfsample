package com.example.soap;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import javax.xml.namespace.QName;

/**
 * JAX-WS SOAP Handler for logging request and response XML messages.
 * Outputs raw SOAP XML to standard output for debugging purposes.
 */
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String SEPARATOR = "========================================";

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outbound != null && outbound) {
            logMessage("SOAP REQUEST", context);
        } else {
            logMessage("SOAP RESPONSE", context);
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        logMessage("SOAP FAULT", context);
        return true;
    }

    @Override
    public void close(MessageContext context) {
        // Nothing to clean up
    }

    private void logMessage(String direction, SOAPMessageContext context) {
        try {
            SOAPMessage message = context.getMessage();
            if (message != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                message.writeTo(baos);
                String xml = baos.toString(StandardCharsets.UTF_8);

                System.out.println(SEPARATOR);
                System.out.println("[" + direction + "]");
                System.out.println(SEPARATOR);
                System.out.println(xml);
                System.out.println(SEPARATOR);
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("Error logging SOAP message: " + e.getMessage());
        }
    }
}
