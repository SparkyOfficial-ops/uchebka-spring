package com.uchebka.library.greeting;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("formal")
public class FormalGreeting implements Greeting {

    @Override
    public String text() {
        return "Здравствуйте, многоуважаемый. Честь имею кланяться и подать том в твёрдом переплёте.";
    }
}
