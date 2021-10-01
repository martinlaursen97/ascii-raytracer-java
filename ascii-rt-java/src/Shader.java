import java.util.HashMap;
import java.util.Map;

public class Shader {
    Map<Integer, Character> brightness;

    Shader() {
        brightness = new HashMap<>();
        brightness.put(12,  '.');
        brightness.put(11,  ',');
        brightness.put(10,  '-');
        brightness.put(9,  '~');
        brightness.put(8,  ':');
        brightness.put(7,  ';');
        brightness.put(6,  '=');
        brightness.put(5,  '!');
        brightness.put(4,  '*');
        brightness.put(3, '#');
        brightness.put(2, '$');
        brightness.put(1, '@');
    }

    public Character getBrightness(float dotP) {
        if (dotP <= 0.1) {
            return brightness.get(12);
        } else if (dotP > 0.1 && dotP <= 0.2) {
            return brightness.get(11);
        } else if (dotP > 0.2 && dotP <= -0.3) {
            return brightness.get(10);
        } else if (dotP > -0.3 && dotP <= -0.4) {
            return brightness.get(9);
        } else if (dotP > -0.4 && dotP <= -0.5) {
            return brightness.get(8);
        } else if (dotP > -0.5 && dotP <= -0.6) {
            return brightness.get(7);
        } else if (dotP > -0.6 && dotP <= 0.650) {
            return brightness.get(6);
        } else if (dotP > 0.650 && dotP <= 0.7) {
            return brightness.get(5);
        } else if (dotP > 0.7 && dotP <= 0.75) {
            return brightness.get(4);
        } else if (dotP > 0.75 && dotP <= 0.8) {
            return brightness.get(3);
        } else if (dotP > 0.8 && dotP <= 0.9) {
            return brightness.get(2);
        } else {
            return brightness.get(1);
        }
    }
}
