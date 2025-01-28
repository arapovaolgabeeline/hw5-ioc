package org.example.commands;

public class SetCurrentScopeCommand implements ICommand {
    Object _scope;

    public SetCurrentScopeCommand(Object _scope) {
        this._scope = _scope;
    }

    @Override
    public void execute() {
        InitCommand.currentScope.set(_scope);
    }
}
