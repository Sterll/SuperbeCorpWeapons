package fr.yanis.superbecorpweapons.item.management.config;

public @interface StringConfig {

    String name();
    String value() default "";
    String minLen() default "0";
    String maxLen() default "256";
}
