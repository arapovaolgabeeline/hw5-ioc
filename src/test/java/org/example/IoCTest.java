package org.example;

import org.example.commands.ICommand;
import org.example.commands.UpdateIocResolveDependencyStrategyCommand;
import org.example.commands.VoidCommand;
import org.example.interfaces.IoCStrategyUpdater;
import org.example.interfaces.StrategyHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IoCTest {


    @Test
    void shouldResolveUpdateIocDependency() {
        String dependencyName = "Update Ioc Resolve Dependency Strategy";

        Object[] args = new Object[5];
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
        Assertions.assertTrue(resolve instanceof UpdateIocResolveDependencyStrategyCommand);
        resolve.execute();
    }
}