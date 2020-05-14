package rmhub.mod.weatherstation.repository;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;

/**
 * Empty marker class used to identify this package when using type-safe basePackageClasses in Spring {@link ComponentScan}.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface RepoPackageMarker {

}
