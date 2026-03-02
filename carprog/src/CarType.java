public enum CarType {
    SEDAN("Седан"),
    SUV("Внедорожник"),
    HATCHBACK("Хэтчбек"),
    ELECTRIC("Электро"),
    TRUCK("Грузовик"),
    COUPE("Купе");

    private final String displayName;

    CarType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}