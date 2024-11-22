package org.bank.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OuthClientDetails implements Serializable {

    private String id;

    private String clientName;

    private String clientId;

    private String clientSecret;

}
