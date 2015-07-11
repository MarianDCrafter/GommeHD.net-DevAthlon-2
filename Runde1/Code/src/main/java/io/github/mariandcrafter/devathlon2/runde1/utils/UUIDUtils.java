package io.github.mariandcrafter.devathlon2.runde1.utils;

import java.util.UUID;

public final class UUIDUtils {

    /**
     * Converts a UUID into a string
     * @param uuid the uuid to convert
     * @return the string
     */
    public static String stringFromUUID(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

}
