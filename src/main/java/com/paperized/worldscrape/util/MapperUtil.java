package com.paperized.worldscrape.util;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MapperUtil {
  public static <T, R> R mapTo(T arg, Set<? extends GrantedAuthority> roles,
                               BiFunction<T, Set<? extends GrantedAuthority>, R> mapFn) {
    return mapFn.apply(arg, roles);
  }

  public static <T, R> R mapTo(T arg, Function<T, R> mapFn) {
    return mapFn.apply(arg);
  }
}
