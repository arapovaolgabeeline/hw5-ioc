package org.example.ioc;

import org.example.interfaces.IDependencyResolverStrategyUpdater;
import org.example.interfaces.IDependencyResolverStrategy;

// 1. Интерфейс IoC устойчив к изменению требований.
public class IoC {
    static IDependencyResolverStrategy strategy = new IDependencyResolverStrategy() {
        @Override
        public <T> T resolve(String dependency, Object[] args) {
            if ("Update Ioc Resolve Dependency Strategy".equals(dependency)) {
                return (T) new IocResolveDependencyStrategySetterCommand((IDependencyResolverStrategyUpdater) args[0]);
            } else {
                throw new IllegalArgumentException("Dependency " + dependency + " was not found.");
            }
        }
    };

    // 2. IoC предоставляет ровно один метод для всех операций. 1 балл
    // тут получается каждый раз мы будем переопределять способ определения зависимостей, скоуп искать и проч?
    public static <T> T resolve(String dependency, Object[] args) {
        return (T) strategy.resolve(dependency, args);
    }

}
