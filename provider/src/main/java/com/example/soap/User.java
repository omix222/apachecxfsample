package com.example.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * User entity for SOAP web service
 * This class will be automatically mapped to XML schema in WSDL
 */
@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {
    "userId",
    "name",
    "status",
    "created"
})
public class User {

    @XmlElement(required = true)
    private String userId;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private String status;

    @XmlElement(required = true)
    private String created;

    // Default constructor (required for JAXB)
    public User() {
    }

    // Constructor with all fields
    public User(String userId, String name, String status, String created) {
        this.userId = userId;
        this.name = name;
        this.status = status;
        this.created = created;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
