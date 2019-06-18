package com.swufe.record.database.RecordDbSchema;

public class RecordDbSchema {
    public static final class RecordTable{
        public static final String NAME = "records";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE="date";
            public static final String SOLVED = "sloved";
        }
    }
}
