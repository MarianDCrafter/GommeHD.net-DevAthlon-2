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

    /**
     * Converts a string to a UUID
     * @param uuid the uuid to convert
     * @return the UUID
     */
    public static UUID stringToUUID(String uuid) {
        return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
    }

}
