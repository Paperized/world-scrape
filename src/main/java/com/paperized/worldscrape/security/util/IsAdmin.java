package com.paperized.worldscrape.security.util;

import com.paperized.worldscrape.security.AuthRole;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(AuthRole.AUTH_ROLE_ADMIN)
public @interface IsAdmin {
}
