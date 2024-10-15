package fr.yanis.superbecorpweapons.item.management.config;

public @interface BooleanConfig {

    String name();
    boolean value() default false;

}
