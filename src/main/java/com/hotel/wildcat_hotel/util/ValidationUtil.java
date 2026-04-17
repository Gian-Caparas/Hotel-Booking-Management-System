package com.hotel.wildcat_hotel.util;

public class ValidationUtil {
    // Business Rule: Capacity Check
    public static boolean validateCapacity(String type, int count) {
        if (type.equalsIgnoreCase("SINGLE") && count > 1) return false;
        if (type.equalsIgnoreCase("DOUBLE") && count > 2) return false;
        if (type.equalsIgnoreCase("DELUXE") && count > 4) return false;
        return count > 0;
    }
}