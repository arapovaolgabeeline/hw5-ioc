package org.example.commands;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.example.interfaces.Dependency;
import org.example.interfaces.IoCStrategyUpdater;
import org.example.interfaces.StrategyHolder;
import org.example.ioc.IoC;

public class InitCommand implements ICommand {
    public static ConcurrentMap<String, Dependency> rootScope = new ConcurrentHashMap<String, Dependency>();
    static boolean initialized = false;

    public static ThreadLocal<Object> currentScope = new ThreadLocal<>();

    @Override
    public void execute() {
        if (initialized) {
            return;
        }

        synchronized (rootScope) {
            /**
             * Work with scopes
             */
            rootScope.put("IoC.Scope.Current.Set", (Object[] args) -> new SetCurrentScopeCommand(args[0]));
            rootScope.put("IoC.Scope.Current.Clear", (Object[] args) -> new ClearCurrentScopeCommand());
            rootScope.put("IoC.Scope.Current", (Object[] args) -> Objects.isNull(currentScope.get()) ? rootScope : currentScope.get());
            rootScope.put("IoC.Scope.Parent", (Object[] args) -> {
                throw new RuntimeException("The root scope has no a parent scope.");
            });
            rootScope.put("IoC.Scope.Create.Empty", (Object[] args) -> new ConcurrentHashMap<>());
            rootScope.put("IoC.Scope.Create", (Object[] args) -> {
                ConcurrentMap<String, Dependency> createdScope = IoC.resolve("IoC.Scope.Create.Empty", new Object[]{});
                if (args.length != 0) {
                    Object parentScope = args[0]; // первым аргументом ждем ParentScope
                    createdScope.put("IoC.Scope.Parent", arguments -> parentScope);
                } else {
                    Object parentScope = IoC.resolve("IoC.Scope.Current", new Object[]{});
                    createdScope.put("IoC.Scope.Parent", arguments -> parentScope);
                }
                return createdScope;
            });

            /**
             * Resolvers and registers
             */
            rootScope.put("IoC.Register", (Object[] args) -> new RegisterDependencyCommand((String) args[0],
                    (Dependency) args[1]));

            Object[] args = new Object[5];
            args[0] = new IoCStrategyUpdater() { // принимает String dependency, Object[] args и возвращает T
                @Override
                public StrategyHolder update(StrategyHolder oldStrategy) {
                    return new StrategyHolder() {
                        @Override
                        public <T> T resolve(String dependency, Object[] args) {
                            Object scope = Objects.nonNull(currentScope.get()) ? currentScope.get() : rootScope;
                            DependencyResolver dependencyResolver = new DependencyResolver(scope);
                            return (T) dependencyResolver.resolve(dependency, args); // тут вернется команда в итоге
                        }
                    };
                }
            };

            // первая и единственная команда IoC: определяем место для хранения зависимостей, в итоге
            // "Update Ioc Resolve Dependency Strategy" подменяет себя на настоящие стратегии
            IoC.<ICommand>resolve("Update Ioc Resolve Dependency Strategy", args).execute(); // эта хуйня будет
            // дергаться постоянно, через нее все команды и будут вызываться
            // или нет....

            initialized = true;
        }
    }
}
