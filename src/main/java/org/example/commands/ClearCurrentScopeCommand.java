package org.example.commands;

public class ClearCurrentScopeCommand implements ICommand {
    @Override
    public void execute() {
        InitCommand.currentScope.remove();
    }
}
