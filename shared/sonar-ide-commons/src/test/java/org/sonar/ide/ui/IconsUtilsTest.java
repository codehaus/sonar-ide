package org.sonar.ide.ui;

import org.junit.Test;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Violation;

import java.awt.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.sonar.ide.ui.IconsUtils.IMAGES_PATH;

/**
 * @author Evgeny Mandrikov
 */
public class IconsUtilsTest {
    @Test
    public void testPriorityIcons() {
        assertThat(IconsUtils.getPriorityIconPath(newViolation("Blocker")), is(IMAGES_PATH + "priority/blocker.gif"));
        assertThat(IconsUtils.getPriorityIconPath(newViolation("Critical")), is(IMAGES_PATH + "priority/critical.gif"));
        assertThat(IconsUtils.getPriorityIconPath(newViolation("Major")), is(IMAGES_PATH + "priority/major.gif"));
        assertThat(IconsUtils.getPriorityIconPath(newViolation("Minor")), is(IMAGES_PATH + "priority/minor.gif"));
        assertThat(IconsUtils.getPriorityIconPath(newViolation("Info")), is(IMAGES_PATH + "priority/info.gif"));
    }

    @Test
    public void testPriotiryColors() {
        assertThat(IconsUtils.getColor(newViolation("Blocker")), is(Color.RED));
        assertThat(IconsUtils.getColor(newViolation("Critical")), is(Color.RED));
        assertThat(IconsUtils.getColor(newViolation("Major")), is(Color.YELLOW));
        assertThat(IconsUtils.getColor(newViolation("Minor")), is(Color.YELLOW));
        assertThat(IconsUtils.getColor(newViolation("Info")), is(Color.YELLOW));
    }

    @Test
    public void testViolationIcons() {
        assertThat(IconsUtils.getIconPath(newViolation(null)), is(IMAGES_PATH + "violation.png"));
    }

    @Test
    public void testTendencyIcons() {
        assertThat(IconsUtils.getTendencyIconPath(newMeasure(null, 1)), nullValue());
        assertThat(IconsUtils.getTendencyIconPath(newMeasure(1, null)), nullValue());
        assertThat(IconsUtils.getTendencyIconPath(newMeasure(0, 0)), nullValue());
        assertThat(IconsUtils.getTendencyIconPath(newMeasure(0, 1)), is(IMAGES_PATH + "tendency/1-black.png"));
        assertThat(IconsUtils.getTendencyIconPath(newMeasure(-1, 1)), is(IMAGES_PATH + "tendency/1-red.png"));
        assertThat(IconsUtils.getTendencyIconPath(newMeasure(1, -2)), is(IMAGES_PATH + "tendency/-2-green.png"));
    }

    private Violation newViolation(String priority) {
        Violation violation = new Violation();
        violation.setPriority(priority);
        return violation;
    }

    private Measure newMeasure(Integer trend, Integer var) {
        return new Measure()
                .setTrend(trend)
                .setVar(var);
    }
}
