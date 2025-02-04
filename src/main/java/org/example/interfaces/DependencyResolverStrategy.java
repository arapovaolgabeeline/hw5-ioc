package org.example.interfaces;

@FunctionalInterface
public interface DependencyResolverStrategy {

    Object resolve(Object[] args);

}
