package org.example.commands;

import java.util.Map;
import org.example.interfaces.IDependency;
import org.example.ioc.IoC;

public class RegisterDependencyCommand implements ICommand {
    private final String dependencyName;
    private final IDependency IDependency;

    public RegisterDependencyCommand(String dependencyName, IDependency strategy) {
        this.dependencyName = dependencyName;
        this.IDependency = strategy;
    }

    /**
     * Register dependency in current scope
     */
    @Override
    public void execute() {
        Map<String, IDependency> currentScope = IoC.<Map<String, IDependency>>resolve("IoC.Scope.Current", new Object[]{});
        currentScope.put(dependencyName, IDependency);
    }
}
