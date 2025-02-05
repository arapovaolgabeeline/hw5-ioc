package org.example.ioc;

import org.example.interfaces.CommonDependencyResolverStrategy;
import org.example.interfaces.IoCStrategyUpdater;

public class IocContextCleaner extends IoC {

    public static void clearContext() {
        strategy = new CommonDependencyResolverStrategy() {
            @Override
            public <T> T resolve(String dependency, Object[] args) {
                if ("Update Ioc Resolve Dependency Strategy".equals(dependency)) {
                    return (T) new UpdateIocResolveDependencyStrategyCommand((IoCStrategyUpdater) args[0]);
                } else {
                    throw new IllegalArgumentException("Dependency " + dependency + " was not found.");
                }
            }
        };
    }

}
