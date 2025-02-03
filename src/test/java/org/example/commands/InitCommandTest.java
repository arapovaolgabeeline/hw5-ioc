package org.example.commands;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.example.interfaces.Dependency;
import org.example.ioc.IoC;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InitCommandTest {

    @BeforeAll
    public static void beforeAll() {
        InitCommand.initialized = false;
    }

    @BeforeEach
    void init() {
        doInitialization();
    }

    @Test
    void shouldReturnCurrentScope() {
        ConcurrentMap<String, Dependency> resolve = IoC.resolve("IoC.Scope.Current", new Object[]{});
        assertNotNull(resolve);
        assertEquals(8, resolve.size());
        assertTrue(resolve.containsKey("IoC.Scope.Create"));
        assertTrue(resolve.containsKey("IoC.Scope.Current"));
        assertTrue(resolve.containsKey("IoC.Scope.Current.Clear"));
        assertTrue(resolve.containsKey("IoC.Scope.Parent"));
        assertTrue(resolve.containsKey("IoC.Scope.Current.Set"));
        assertTrue(resolve.containsKey("IoC.Scope.Create.Empty"));
        assertTrue(resolve.containsKey("IoC.Register"));
    }

    @Test
    void shouldChangeScopeToNewOne() {
        ConcurrentMap<String, Dependency> parentScope = IoC.resolve("IoC.Scope.Current", new Object[]{});

        // создаст скоуп
        ConcurrentMap<String, Dependency> createdScope = IoC.resolve("IoC.Scope.Create", new Object[]{});
        // посмотри сколько у него там зависимостей и потом ту которой нет но есть в перенте дерни
        assertNotNull(createdScope);

        ICommand setScopeCommand = IoC.resolve("IoC.Scope.Current.Set", new Object[]{createdScope});
        setScopeCommand.execute();

        ConcurrentMap<String, Dependency> childScope = IoC.resolve("IoC.Scope.Current", new Object[]{});

        // надо взять любую зависимость из перента и поискать ее в текущем
        assertNotNull(parentScope);
        assertEquals(childScope, createdScope);
        assertNotEquals(childScope, parentScope);
    }

    private static void doInitialization() {
        InitCommand initCommand = new InitCommand();
        initCommand.execute();
        assertTrue(InitCommand.initialized);
    }

    @Test
    void shouldRegisterNewStrategy() throws Exception {
        MutableBoolean isNewDependencyInvoked = new MutableBoolean();
        Object registerDependencyCommand = IoC.resolve("IoC.Register", new Object[]{"IoC.newDependency", new Dependency() {
            @Override
            public Object resolve(Object[] args) {
                return new ICommand() {
                    @Override
                    public void execute() {
                        ((MutableBoolean) isNewDependencyInvoked).setValue(Boolean.TRUE);
                    }
                };
            }
        }});
        ((ICommand) registerDependencyCommand).execute();

        ICommand newDependency = IoC.resolve("IoC.newDependency", new Object[]{isNewDependencyInvoked});

        assertFalse(isNewDependencyInvoked.getValue());
        newDependency.execute();
        assertTrue(isNewDependencyInvoked.getValue());
    }


    @Test
    void shouldUseDifferentScopesForDifferentThreads() {
        // проверим, что в разных тредах создаются разные скоупы
        MutableBoolean wasScopeCreated = new MutableBoolean();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            synchronized (wasScopeCreated) {
                createAndSetNewScope();
                ICommand parentScope = IoC.resolve("IoC.Scope.Parent", new Object[]{});
                assertDoesNotThrow(parentScope::execute);
                wasScopeCreated.setTrue();
            }
        });

        executor.submit(() -> {
            synchronized (wasScopeCreated) {
                // assert current scope is root
                ICommand parentScope = IoC.resolve("IoC.Scope.Parent", new Object[]{});
                assertThrows(RuntimeException.class, parentScope::execute);
            }
        });
    }

    private static void createAndSetNewScope() {
        // создаст скоуп
        ConcurrentMap<String, Dependency> createdScope = IoC.resolve("IoC.Scope.Create", new Object[]{});
        // посмотри сколько у него там зависимостей и потом ту которой нет но есть в перенте дерни
        assertNotNull(createdScope);

        ICommand setScopeCommand = IoC.resolve("IoC.Scope.Current.Set", new Object[]{createdScope});
        setScopeCommand.execute();
    }

}