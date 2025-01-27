package org.example.interfaces;

@FunctionalInterface
public interface Dependency {

    Object resolve(Object[] args);

}
