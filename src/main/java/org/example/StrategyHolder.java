package org.example;

@FunctionalInterface
public interface StrategyHolder {

    <T> T resolve(String dependency, Object[] args);

}
