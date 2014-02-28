package util;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Verifies {
	/**
* The text description of what this test will be doing. This is how unit tests are linked back
* to a specific "@should" on the (hopefully) many "@shoulds" on a method.
*
* @return the text after the "@should" on the method this unit test is testing
*/
public String value();

/**
* The method name within the class that this unit test is testing.
*
* @return the name of the method being tested
*/
public String method();

}
