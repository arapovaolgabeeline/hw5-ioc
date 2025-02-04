package org.example;

import org.example.commands.ICommand;
import org.example.commands.UpdateIocResolveDependencyStrategyCommand;
import org.example.interfaces.IoCStrategyUpdater;
import org.example.interfaces.CommonDependencyResolverStrategy;
import org.example.ioc.IoC;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IoCTest {

    @BeforeEach
    public void beforeEach() {
        IoC.strategy = IoC.clearContext();
    }

    @Test
    void shouldResolveUpdateIocDependency() {
        String dependencyName = "Update Ioc Resolve Dependency Strategy";

        Object[] args = new Object[1];
        args[0] = new IoCStrategyUpdater() {
            @Override
            public CommonDependencyResolverStrategy update(CommonDependencyResolverStrategy newDependency) {
                return new CommonDependencyResolverStrategy() {
                    @Override
                    public <T> T resolve(String dependency, Object[] args) {
                        return (T) new UpdateIocResolveDependencyStrategyCommand(new IoCStrategyUpdater() {
                            @Override
                            public CommonDependencyResolverStrategy update(CommonDependencyResolverStrategy newDependency) {
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
            public CommonDependencyResolverStrategy update(CommonDependencyResolverStrategy newDependency) {
                return new CommonDependencyResolverStrategy() {
                    @Override
                    public <T> T resolve(String dependency, Object[] args) {
                        return (T) new UpdateIocResolveDependencyStrategyCommand(new IoCStrategyUpdater() {
                            @Override
                            public CommonDependencyResolverStrategy update(CommonDependencyResolverStrategy newDependency) {
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