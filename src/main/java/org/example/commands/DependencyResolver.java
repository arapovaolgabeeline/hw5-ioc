package org.example.commands;

import java.util.Map;
import org.example.interfaces.Dependency;
import org.example.interfaces.ScopeItem;
import org.example.resolvers.IDependencyResolver;

public class DependencyResolver implements IDependencyResolver {
    public final Map<String, ScopeItem> _dependencies;

    public DependencyResolver(Object scope) {
        _dependencies = (Map<String, ScopeItem>) scope;
    }

    @Override
    public Object resolve(String dependencyName, Object[] args) {
        Map<String, ScopeItem> dependencies = _dependencies;

        while (true) {
            Dependency dependencyResolverStrategy = null;
            if (dependencies.containsKey(dependencyName)) {
                return dependencies.get(dependencyName).resolve(args);
            } else {
                dependencies = (Map<String, ScopeItem>)dependencies.get("IoC.Scope.Parent").resolve(args);
            }
        }
    }
}
