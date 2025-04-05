package view.bars;

import model.Globals.Damageable;

public class HealthBar extends PercentageBar {
    Damageable subject;

    public HealthBar(Damageable subject) {
        super(subject.getMaxHitPoints(), subject.getHitPoints());
        this.subject = subject;
    }

}
