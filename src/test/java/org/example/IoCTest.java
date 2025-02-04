package org.example;

import org.example.commands.ICommand;
import org.example.commands.InitCommand;
import org.example.commands.UpdateIocResolveDependencyStrategyCommand;
import org.example.interfaces.IoCStrategyUpdater;
import org.example.interfaces.StrategyHolder;
import org.example.ioc.IoC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IoCTest {

    @BeforeEach
    public void beforeEach() {
        IoC._strategy = IoC.defaultStrategy();
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

    @Test
    void shouldThrowExceptionWhenUpdateIocResolveDependencyWasNotInvoke() {
        String dependencyName = "Unknown Strategy";

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

        assertThrows(IllegalArgumentException.class, () -> IoC.<ICommand>resolve(dependencyName, args));
    }
}