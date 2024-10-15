package fr.yanis.superbecorpweapons.item.management.config;

public @interface IntConfig {

    String name();
    int value() default 1;
    int maxValue() default Integer.MAX_VALUE;
    int minValue() default Integer.MIN_VALUE;

}
