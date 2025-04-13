package model.Globals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ThingTest {

    @Test
    void createThingTest() {
        Thing thing = new Thing("Dall-E", "A gentle cleaning robot.");
        assertEquals("A gentle cleaning robot.", thing.getDescription());
        assertEquals("Dall-E", thing.getName());
    }

    @Test
    void changeNameAndDescriptionTest() {
        Thing thing = new Thing("Admiral Ackbar", "It's a trap!");

        thing.setName("Jar Jar Binks");
        thing.setDescription("Dis is nutsen.");

        assertEquals("Jar Jar Binks", thing.getName());
        assertEquals("Dis is nutsen.", thing.getDescription());
    }

}