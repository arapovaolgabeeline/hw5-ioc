package org.example.commands;

import java.util.concurrent.ConcurrentMap;
import org.example.interfaces.Dependency;
import org.example.ioc.IoC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class InitCommandTest {

    @Test
    void shouldInitCommands() {
        InitCommand initCommand = new InitCommand();
        initCommand.execute();
        assertTrue(InitCommand.initialized);
    }

    @Test
    void shouldReturnCurrentScope() {
        InitCommand initCommand = new InitCommand();
        initCommand.execute();
        assertTrue(InitCommand.initialized);

        ConcurrentMap<String, Dependency> resolve = IoC.resolve("IoC.Scope.Current", new Object[]{});
        assertNotNull(resolve);
        assertEquals(7, resolve.size());
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
        InitCommand initCommand = new InitCommand();
        initCommand.execute();
        assertTrue(InitCommand.initialized);
        ConcurrentMap<String, Dependency> parentScope = IoC.resolve("IoC.Scope.Current", new Object[]{});

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

    @Test
    void shouldCreateNewStrategy() {
        // we are here
    }

    @Test
    void shouldUseDifferentScopesForDifferentThreads() {
        // we are here
    }
}