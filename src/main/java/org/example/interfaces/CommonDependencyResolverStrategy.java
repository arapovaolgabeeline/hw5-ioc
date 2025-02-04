package org.example.interfaces;

@FunctionalInterface
public interface CommonDependencyResolverStrategy {

    <T> T resolve(String dependency, Object[] args);

}
