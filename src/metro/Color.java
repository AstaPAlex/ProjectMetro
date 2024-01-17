package metro;

public enum Color {
    RED("Красная"), BLUE("Синяя"), GREEN("Зеленая"), YELLOW("Желтая"), PINK("Розовая"),
    PURPLE("Фиолетовая"), ORANGE("Оранжевая");

    private final String color;

    Color(String color) {
        this.color = color;
    }

    public String getStringColor() {
        return color;
    }
}
