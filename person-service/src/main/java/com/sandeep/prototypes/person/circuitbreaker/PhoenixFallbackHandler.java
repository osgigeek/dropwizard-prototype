package com.sandeep.prototypes.person.circuitbreaker;

public interface PhoenixFallbackHandler<T> {

  T getFallback();
}
