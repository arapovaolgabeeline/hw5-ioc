package org.example.commands;

import org.example.IoC;

import org.example.interfaces.IoCStrategyUpdater;

public class UpdateIocResolveDependencyStrategyCommand implements ICommand {
    private final IoCStrategyUpdater _updateIoCStrategy;

    public UpdateIocResolveDependencyStrategyCommand(IoCStrategyUpdater updateIoCStrategy) {
        _updateIoCStrategy = updateIoCStrategy;
    }

    @Override
    public void execute() {
        IoC._strategy = _updateIoCStrategy.update(IoC._strategy);
    }

}
