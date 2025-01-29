package org.example.commands;

import java.util.concurrent.ConcurrentMap;
import org.example.interfaces.ScopeItem;
import org.example.ioc.IoC;
import static org.junit.jupiter.api.Assertions.*;
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

        Object resolve = IoC.<ConcurrentMap<String, ScopeItem>>resolve("IoC.Scope.Current", new Object[]{});
        assertNotNull(resolve);
    }
}