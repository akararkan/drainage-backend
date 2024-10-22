package com.ak.drainage.user.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class Token {
    public String token;
    public String response;
    public Token(String response){
        this.response = response;
    }



}