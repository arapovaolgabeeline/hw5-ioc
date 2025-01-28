package org.example.commands;

import java.util.Map;
import org.example.interfaces.Dependency;
import org.example.ioc.IoC;

public class RegisterDependencyCommand implements ICommand {
    String _dependency;
    Dependency _dependencyResolverStrategy;

    public RegisterDependencyCommand(String _dependency, Dependency _dependencyResolverStrategy) {
        this._dependency = _dependency;
        this._dependencyResolverStrategy = _dependencyResolverStrategy;
    }

    @Override
    public void execute() {
        Map<String, Dependency> currentScope = IoC.<Map<String, Dependency>>resolve("IoC.Scope.Current", new Object[]{});
        currentScope.put(_dependency, _dependencyResolverStrategy);
    }
}
