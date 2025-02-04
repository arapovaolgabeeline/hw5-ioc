package org.example.commands;

import java.util.Map;
import org.example.interfaces.DependencyResolverStrategy;
import org.example.resolvers.IDependencyResolver;

/**
 * Хранит список зависимостей. Сами зависимости крадет из текущего скоупа. Если в нем нет нужной, идет в перент.
 */
public class DependencyResolver implements IDependencyResolver {
    private final Map<String, DependencyResolverStrategy> dependencies;

    public DependencyResolver(Object scope) {
        dependencies = (Map<String, DependencyResolverStrategy>) scope;
    }

    @Override
    public Object resolve(String dependencyName, Object[] args) {
        Map<String, DependencyResolverStrategy> dependencies = this.dependencies;

        while (true) {
            DependencyResolverStrategy dependencyResolverStrategy = null;
            if (dependencies.containsKey(dependencyName)) {
                dependencyResolverStrategy = dependencies.get(dependencyName);
                // вызывает искомую зависимость с аргументами
                return dependencyResolverStrategy.resolve(args);
            } else {
                // зависимости из родительского скоупа ищет
                dependencies = (Map<String, DependencyResolverStrategy>) dependencies.get("IoC.Scope.Parent").resolve(args);
            }
        }
    }
}
