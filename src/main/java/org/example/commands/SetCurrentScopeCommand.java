package org.example.commands;

public class SetCurrentScopeCommand implements ICommand {
    private final Object scope;

    public SetCurrentScopeCommand(Object scope) {
        this.scope = scope;
    }

    @Override
    public void execute() {
        InitCommand.currentScope.set(scope);
    }
}
