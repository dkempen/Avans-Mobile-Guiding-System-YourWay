package com.id.yourway.providers.helpers;

import java.util.Arrays;

public class TableCreationHelper {
    public static String createTableWithColumns(String tableName, String... columns) {
        return "CREATE TABLE " +
                tableName +
                " (" +
                Arrays.toString(columns).replaceAll("[\\[\\]]", "") +
                ")";
    }
}
