package me.geso.nanobench;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Bench} annotation tells nanobench that the {@code public} method
 * to which it is attached can be run as a benchmark target.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bench {
}
