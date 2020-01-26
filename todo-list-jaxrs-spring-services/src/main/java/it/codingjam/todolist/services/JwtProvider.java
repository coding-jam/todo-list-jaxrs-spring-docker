package it.codingjam.todolist.services;

import org.apache.catalina.realm.GenericPrincipal;

public interface JwtProvider {

    String createJwt(GenericPrincipal principal);
}
