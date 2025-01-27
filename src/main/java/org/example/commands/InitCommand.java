package org.example.commands;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.example.interfaces.IoCStrategyUpdater;
import org.example.interfaces.ScopeItem;
import org.example.interfaces.StrategyHolder;
import org.example.ioc.IoC;

public class InitCommand implements ICommand {
    public static ConcurrentMap<String, ScopeItem> rootScope = new ConcurrentHashMap<String, ScopeItem>();
    static boolean initialized = false;

    public ThreadLocal<Object> currentScope = new ThreadLocal<>();

    @Override
    public void execute() {
        if (initialized) {
            return;
        }

        synchronized (rootScope) {

            Object[] args = new Object[5];
            args[0] = new IoCStrategyUpdater() { // принимает String dependency, Object[] args и возвращает T
                @Override
                public StrategyHolder update(StrategyHolder oldStrategy) {
                    return new StrategyHolder() {
                        @Override
                        public <T> T resolve(String dependency, Object[] args) {
                            Object scope = Objects.nonNull(currentScope.get()) ? currentScope.get() : rootScope;
                            DependencyResolver dependencyResolver = new DependencyResolver(scope);
                            return (T) dependencyResolver.resolve(dependency, args);
                        }
                    };
                }
            };

            IoC.<ICommand>resolve("Update Ioc Resolve Dependency Strategy", args).execute();

            initialized = true;
        }
    }
}
