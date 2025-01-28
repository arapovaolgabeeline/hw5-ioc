package org.example.commands;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InitCommandTest {

    @Test
    void shouldInitCommands() {
        InitCommand initCommand = new InitCommand();
        initCommand.execute();
        assertTrue(InitCommand.initialized);
    }
}