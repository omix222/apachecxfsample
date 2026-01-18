package com.example.soap;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * JAX-WS SOAP Handler that filters out unknown elements from responses.
 * This allows the client to handle responses with additional fields
 * not defined in the local WSDL schema.
 *
 * Known fields are automatically detected from JAXB-generated classes
 * using reflection, so no hardcoding is required.
 */
public class UnknownElementFilter implements SOAPHandler<SOAPMessageContext> {

    // Cache of known fields per XML type name, populated from JAXB classes
    private static final Map<String, Set<String>> KNOWN_FIELDS_CACHE = new HashMap<>();

    static {
        // Register JAXB-generated classes to scan for known fields
        registerJaxbClass(com.example.soap.generated.User.class);
    }

    /**
     * Register a JAXB class and extract its known field names from annotations.
     */
    private static void registerJaxbClass(Class<?> clazz) {
        Set<String> fields = new HashSet<>();

        // Get XML type name from @XmlType annotation
        XmlType xmlType = clazz.getAnnotation(XmlType.class);
        String typeName = (xmlType != null && !xmlType.name().isEmpty())
            ? xmlType.name()
            : clazz.getSimpleName();

        // Extract field names from @XmlElement annotations
        for (Field field : clazz.getDeclaredFields()) {
            XmlElement xmlElement = field.getAnnotation(XmlElement.class);
            if (xmlElement != null) {
                String name = xmlElement.name();
                if ("##default".equals(name)) {
                    name = field.getName();
                }
                fields.add(name);
            } else {
                // Field without @XmlElement uses its Java name
                fields.add(field.getName());
            }
        }

        KNOWN_FIELDS_CACHE.put(typeName, fields);
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        // Only process inbound (response) messages
        if (outbound != null && !outbound) {
            filterUnknownElements(context);
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

    private void filterUnknownElements(SOAPMessageContext context) {
        try {
            SOAPMessage message = context.getMessage();
            if (message == null) {
                return;
            }

            SOAPBody body = message.getSOAPBody();
            if (body == null) {
                return;
            }

            // Find elements matching known types and filter unknown children
            filterElements(body);

            // Save changes
            message.saveChanges();

        } catch (Exception e) {
            System.err.println("Error filtering unknown elements: " + e.getMessage());
        }
    }

    private void filterElements(Node node) {
        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) child;

                // Check if this element matches a known type and filter unknown children
                String matchedType = detectKnownType(element);
                if (matchedType != null) {
                    removeUnknownFields(element, KNOWN_FIELDS_CACHE.get(matchedType));
                }

                // Recursively process children
                filterElements(child);
            }
        }
    }

    /**
     * Detect if the element contains fields matching a registered JAXB type.
     * Returns the type name if matched, null otherwise.
     */
    private String detectKnownType(Element element) {
        NodeList children = element.getChildNodes();
        Set<String> childNames = new HashSet<>();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                childNames.add(child.getLocalName());
            }
        }

        // Find a type where at least one known field matches
        for (Map.Entry<String, Set<String>> entry : KNOWN_FIELDS_CACHE.entrySet()) {
            Set<String> knownFields = entry.getValue();
            for (String childName : childNames) {
                if (knownFields.contains(childName)) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

    private void removeUnknownFields(Element element, Set<String> knownFields) {
        NodeList children = element.getChildNodes();
        List<Node> toRemove = new ArrayList<>();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String name = child.getLocalName();
                if (!knownFields.contains(name)) {
                    toRemove.add(child);
                }
            }
        }

        for (Node node : toRemove) {
            element.removeChild(node);
        }
    }
}
