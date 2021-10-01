public class Shader {
    char[] brightness2 = {'.',':','+','%','#','@'};

    public Character getBrightness(float dotP) {
        if (dotP == 0) {
            return ' ';
        }
        else if (dotP > 0 && dotP <= 0.2) {
            return brightness2[0];
        } else if (dotP > 0.2 && dotP <= 0.4) {
            return brightness2[1];
        } else if (dotP > 0.4 && dotP <= 0.6) {
            return brightness2[2];
        } else if (dotP > 0.6 && dotP <= 0.8) {
            return brightness2[3];
        } else if (dotP > 0.8) {
            return brightness2[4];
        }
        return ' ';
    }
}
