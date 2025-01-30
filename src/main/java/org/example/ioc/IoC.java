package org.example.ioc;

import org.example.commands.UpdateIocResolveDependencyStrategyCommand;
import org.example.interfaces.IoCStrategyUpdater;
import org.example.interfaces.StrategyHolder;

// 1. Интерфейс IoC устойчив к изменению требований.
public class IoC {
    /**
     * Принимает только одну стратегию для определения скоупов, стратегии разрешения зависимостей и самих
     * зависимостей. Здесь мы определяем стратегию, поскольку стратегия может переопределить себя
     * */
    public static StrategyHolder _strategy = new StrategyHolder() { // todo должен быть защищенным
        @Override
        public <T> T resolve(String dependency, Object[] args) {
            if ("Update Ioc Resolve Dependency Strategy".equals(dependency)) {
                return (T) new UpdateIocResolveDependencyStrategyCommand((IoCStrategyUpdater) args[0]);
            } else {
                throw new IllegalArgumentException("Dependency " + dependency + " was not found.");
            }
        }
    };

    // 2. IoC предоставляет ровно один метод для всех операций. 1 балл
    // тут получается каждый раз мы будем переопределять способ определения зависимостей, скоуп искать и проч?
    public static <T> T resolve(String dependency, Object[] args) {
        return (T) _strategy.resolve(dependency, args);
    }

}
