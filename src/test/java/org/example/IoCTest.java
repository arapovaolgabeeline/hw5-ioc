package org.example;

import org.example.commands.ICommand;
import org.example.commands.InitCommand;
import org.example.commands.UpdateIocResolveDependencyStrategyCommand;
import org.example.interfaces.IoCStrategyUpdater;
import org.example.interfaces.StrategyHolder;
import org.example.ioc.IoC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class IoCTest {

    @BeforeAll
    public static void beforeAll() {
        InitCommand.initialized = false;
        IoC._strategy = new StrategyHolder() { // todo должен быть защищенным
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

    @Test
    void shouldResolveUpdateIocDependency() {
        String dependencyName = "Update Ioc Resolve Dependency Strategy";

        Object[] args = new Object[1];
        args[0] = new IoCStrategyUpdater() {
            @Override
            public StrategyHolder update(StrategyHolder newDependency) {
                return new StrategyHolder() {
                    @Override
                    public <T> T resolve(String dependency, Object[] args) {
                        return (T) new UpdateIocResolveDependencyStrategyCommand(new IoCStrategyUpdater() {
                            @Override
                            public StrategyHolder update(StrategyHolder newDependency) {
                                return null;
                            }
                        });
                    }
                };
            }
        };

        ICommand resolve = IoC.<ICommand>resolve(dependencyName, args);

        Assertions.assertNotNull(resolve);
        Assertions.assertInstanceOf(UpdateIocResolveDependencyStrategyCommand.class, resolve);
        resolve.execute();
    }
}