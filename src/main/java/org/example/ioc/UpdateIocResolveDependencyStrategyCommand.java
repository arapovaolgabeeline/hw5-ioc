package org.example.ioc;

import org.example.commands.ICommand;
import org.example.interfaces.IoCStrategyUpdater;

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
    }

}
