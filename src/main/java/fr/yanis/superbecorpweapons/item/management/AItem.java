package fr.yanis.superbecorpweapons.item.management;

import fr.yanis.superbecorpweapons.item.management.config.BooleanConfig;
import fr.yanis.superbecorpweapons.item.management.config.IntConfig;
import fr.yanis.superbecorpweapons.item.management.config.StringConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AItem {

    String defaultName() default "Nom par defaut";
    String defaultDescription() default "Description par defaut";
    int defaultCooldown() default 0;

    StringConfig[] stringConfigValues() default {};
    IntConfig[] intConfigValues() default {};
    BooleanConfig[] booleanConfigValues() default {};

}
