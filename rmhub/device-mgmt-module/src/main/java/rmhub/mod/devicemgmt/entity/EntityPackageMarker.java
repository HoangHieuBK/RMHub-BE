package rmhub.mod.devicemgmt.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Empty marker class used to identify this package when using type-safe basePackageClasses in Spring {@link EntityScan}.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface EntityPackageMarker {

}
