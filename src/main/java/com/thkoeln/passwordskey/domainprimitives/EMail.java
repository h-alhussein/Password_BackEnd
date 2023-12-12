package com.thkoeln.passwordskey.domainprimitives;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EMail {
    private String address;
    private boolean verified;
    private String verifyCode;

    public EMail(String address) {
        this.address = address;
        this.verifyCode = UUID.randomUUID().toString();
    }
}
