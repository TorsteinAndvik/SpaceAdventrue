package model.Score;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class NameProviderTest {

    @Test
    void testNameIsNotNull() {
        assertNotNull(new SystemUserNameProvider().getPlayerName());
    }
}
