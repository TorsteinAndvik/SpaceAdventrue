package model.Score;

public class SystemUserNameProvider implements NameProvider {

    @Override
    public String getPlayerName() {
        return System.getProperty("user.name");
    }
}