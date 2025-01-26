package org.example.interfaces;

@FunctionalInterface
public interface StrategyHolder {

    <T> T resolve(String dependency, Object[] args);

}
