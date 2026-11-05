package com.uchebka.library.greeting;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("friendly")
public class FriendlyGreeting implements Greeting {

    @Override
    public String text() {
        return "Привет, родной! Проходи, не стой на пороге: самовар уже пыхтит.";
    }
}
