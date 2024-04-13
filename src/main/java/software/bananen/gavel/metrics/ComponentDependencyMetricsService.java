package software.bananen.gavel.metrics;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.library.metrics.ArchitectureMetrics;
import com.tngtech.archunit.library.metrics.ComponentDependencyMetrics;
import com.tngtech.archunit.library.metrics.MetricsComponents;

import java.util.ArrayList;
import java.util.Collection;

public class ComponentDependencyMetricsService {

    /**
     * Measures the component dependency metrics for the given packages.
     *
     * @param pkg                The package that should be measured.
     * @param resolveSubpackages Can be set to true to measure metrics for the
     *                           subpackages of the given package as well.
     * @return The component dependency metrics.
     */
    public Collection<ComponentDependency> measure(final JavaPackage pkg,
                                                   final boolean resolveSubpackages) {
        final MetricsComponents<JavaClass> components =
                MetricsComponents.fromPackages(pkg.getSubpackages());
        final ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(components);

        final Collection<ComponentDependency> measurements = new ArrayList<>();

        for (JavaPackage subpackage : pkg.getSubpackages()) {
            measurements.add(new ComponentDependency(
                    subpackage.getName(),
                    metrics.getEfferentCoupling(subpackage.getName()),
                    metrics.getAfferentCoupling(subpackage.getName()),
                    metrics.getInstability(subpackage.getName()),
                    metrics.getAbstractness(subpackage.getName()),
                    metrics.getNormalizedDistanceFromMainSequence(subpackage.getName())
            ));

            if (resolveSubpackages) {
                measurements.addAll(measure(subpackage, true));
            }
        }

        return measurements;
    }
}
