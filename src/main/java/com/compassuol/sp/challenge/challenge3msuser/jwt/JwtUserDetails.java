package com.compassuol.sp.challenge.challenge3msuser.jwt;


import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;



public class JwtUserDetails extends org.springframework.security.core.userdetails.User {

    private User usuario;

    public JwtUserDetails(User usuario) {
        super(usuario.getEmail(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }


}
