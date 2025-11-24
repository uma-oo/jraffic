package jraffic;

import javafx.scene.paint.Color;

public class TrafficLightController {

    private static final long GRACE_MS = 700;
    private static final long GREEN_MS = 8000 + GRACE_MS;

    private int phaseIndex = 0;
    private long nextSwitchMs = 0;
    private boolean inAllRed = true;

    private static long graceEndMs = 0;

    public TrafficLightController() {
    }

    public void update(long nowMs) {

        if (nextSwitchMs == 0) {
            setAllRed();
            nextSwitchMs = nowMs;
            return;
        }

        if (nowMs < nextSwitchMs)
            return;

        if (inAllRed) {
            // start a green phase for the current phaseIndex lane
            Lane g = phaseIndexToLane(phaseIndex);
            setSingleGreen(g);
            inAllRed = false;
            nextSwitchMs = nowMs + GREEN_MS;
            graceEndMs = System.currentTimeMillis() + GRACE_MS;
        } else {
            setAllRed();
            inAllRed = true;
            phaseIndex = (phaseIndex + 1) % 4;
            nextSwitchMs = nowMs;
            graceEndMs = 0;
        }
    }

    private Lane phaseIndexToLane(int idx) {
        return switch (idx) {
            case 0 -> Lane.NORTH;
            case 1 -> Lane.EAST;
            case 2 -> Lane.SOUTH;
            default -> Lane.WEST;
        };
    }

    private void setSingleGreen(Lane lane) {
        for (LightTraffic tLight : LightTraffic.geLightTraffics()) {
            tLight.setColor(tLight.getLane() == lane ? Color.GREEN : Color.RED);
        }
    }

    private void setAllRed() {
        for (LightTraffic tLight : LightTraffic.geLightTraffics()) {
            tLight.setColor(Color.RED);
        }
    }

    public static boolean isInGrace() {
        return System.currentTimeMillis() < graceEndMs;
    }
}
