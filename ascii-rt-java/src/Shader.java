public class Shader {
    public Character getBrightness(float dotP) {
        if (dotP > 0.8) return '$';
        if (dotP > 0.7) return '#';
        if (dotP > 0.6) return '*';
        if (dotP > 0.5) return '=';
        if (dotP > 0.4) return ':';
        if (dotP > 0.3) return '-';
        if (dotP > 0.2) return '.';
        return ' ';
    }
}
