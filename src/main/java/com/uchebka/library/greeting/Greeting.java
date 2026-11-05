package com.uchebka.library.greeting;

/**
 * Две реализации этого интерфейса лежат в контексте одновременно.
 * Так бывает с любым интерфейсом: Spring не угадывает, какую выбрать,
 * пока место внедрения не помечено {@code @Qualifier}.
 */
public interface Greeting {

    String text();
}
