package util;

import java.util.UUID;

public class Generates {
    private Generates() {
    }

    public static int getNumberCores() {
        return Runtime.getRuntime().availableProcessors() - 1;
    }

    public static String createId() {
        return UUID.randomUUID().toString();
    }

    public static String createIdZero() {
        return new UUID(UUID.randomUUID()
                .getMostSignificantBits(), 0)
                .toString();
    }
}
