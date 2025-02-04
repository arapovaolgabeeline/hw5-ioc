package org.example.commands;

import java.util.Map;
import org.example.interfaces.DependencyResolverStrategy;
import org.example.ioc.IoC;

public class RegisterDependencyCommand implements ICommand {
    private final String dependencyName;
    private final DependencyResolverStrategy dependencyResolverStrategy;

    public RegisterDependencyCommand(String dependencyName, DependencyResolverStrategy strategy) {
        this.dependencyName = dependencyName;
        this.dependencyResolverStrategy = strategy;
    }

    /**
     * Register dependency in current scope
     */
    @Override
    public void execute() {
        Map<String, DependencyResolverStrategy> currentScope = IoC.<Map<String, DependencyResolverStrategy>>resolve("IoC.Scope.Current", new Object[]{});
        currentScope.put(dependencyName, dependencyResolverStrategy);
    }
}
