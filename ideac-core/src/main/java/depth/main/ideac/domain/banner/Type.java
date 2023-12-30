package depth.main.ideac.domain.banner;

public enum Type {

    HOME, PROJECT, IDEA;

    public static Type fromString(String text) {
        return Type.valueOf(text.toUpperCase());
    }
}
