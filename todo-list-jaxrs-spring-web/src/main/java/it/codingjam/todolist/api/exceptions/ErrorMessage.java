package it.codingjam.todolist.api.exceptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorMessage {

    private String message;

    private List<String> details = new ArrayList<>();

    public ErrorMessage() {
        // default constructor to meet JAXB needs
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return Collections.unmodifiableList(details);
    }

    public ErrorMessage addDetail(String detailMessage) {
        this.details.add(detailMessage);
        return this;
    }

    public ErrorMessage addDetails(List<String> detailMessages) {
        this.details.addAll(detailMessages);
        return this;
    }


}
