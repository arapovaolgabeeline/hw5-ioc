package org.example.commands;

import org.example.interfaces.IoCStrategyUpdater;
import org.example.ioc.IoC;

public class UpdateIocResolveDependencyStrategyCommand implements ICommand {
    private final IoCStrategyUpdater _updateIoCStrategy;

    public UpdateIocResolveDependencyStrategyCommand(IoCStrategyUpdater updateIoCStrategy) {
        _updateIoCStrategy = updateIoCStrategy;
    }

    @Override
    public void execute() {
        IoC._strategy = _updateIoCStrategy.update(IoC._strategy);
        System.out.println("Ioc strategy updated");
    }

}
