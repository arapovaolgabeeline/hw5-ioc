package org.example;

// 1. Интерфейс IoC устойчив к изменению требований.
public class IoC {
    private static StrategyHolder _strategy = new StrategyHolder() {
        @Override
        public <T> T resolve(String dependency, Object[] args) {
            if ("Update Ioc Resolve Dependency Strategy".equals(dependency)) {
                return null;
            } else
            {
                throw new IllegalArgumentException("Dependency " + dependency + " was not found.");
            }
        }
    };

    // 2. IoC предоставляет ровно один метод для всех операций. 1 балл
    public static <T> T resolve(String dependency, Object[] args) {
        return (T)_strategy.resolve(dependency, args);
    }

}
