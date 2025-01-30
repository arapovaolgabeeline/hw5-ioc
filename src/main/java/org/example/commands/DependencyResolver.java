package org.example.commands;

import java.util.Map;
import org.example.interfaces.Dependency;
import org.example.resolvers.IDependencyResolver;

/**
 * Хранит список зависимостей. Сами зависимости крадет из текущего скоупа. Если в нем нет нужной, идет в перент.
 */
public class DependencyResolver implements IDependencyResolver {
    public final Map<String, Dependency> _dependencies;

    public DependencyResolver(Object scope) {
        _dependencies = (Map<String, Dependency>) scope;
    }

    @Override
    public Object resolve(String dependencyName, Object[] args) {
        Map<String, Dependency> dependencies = _dependencies;

        while (true) {
            Dependency dependencyResolverStrategy = null;
            if (dependencies.containsKey(dependencyName)) {
                dependencyResolverStrategy = dependencies.get(dependencyName);
                // вызывает искомую зависимость с аргументами
                return dependencyResolverStrategy.resolve(args);
            } else {
                // зависимости из родительского скоупа ищет
                dependencies = (Map<String, Dependency>) dependencies.get("IoC.Scope.Parent").resolve(args);
            }
        }
    }
}
