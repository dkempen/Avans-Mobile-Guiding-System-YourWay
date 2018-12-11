package com.id.yourway.providers.helpers;

import java.util.Arrays;

public class TableCreationHelper {
    public static String createTableWithColumns(String tableName, String... columns){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");
        builder.append(tableName);
        builder.append(" (");
        builder.append(
                Arrays.toString(columns).replaceAll("[\\[\\]]", "")
        );
        builder.append(")");
        return builder.toString();
    }
}
