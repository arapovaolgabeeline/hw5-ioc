package org.example.commands;

import org.example.interfaces.IoCStrategyUpdater;
import org.example.ioc.IoC;

/**
 * Команда обновляет зависимости
 * */
public class UpdateIocResolveDependencyStrategyCommand implements ICommand {
    private final IoCStrategyUpdater _updateIoCStrategy;

    public UpdateIocResolveDependencyStrategyCommand(IoCStrategyUpdater updateIoCStrategy) {
        _updateIoCStrategy = updateIoCStrategy;
    }

    @Override
    public void execute() {
        IoC.strategy = _updateIoCStrategy.update(IoC.strategy);
        System.out.println("Ioc strategy updated");
    }

}
