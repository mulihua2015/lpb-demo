package com.example.demo;

public class DBQueryExecuter {

    private String dbUrl = "jdbc:ucanaccess://resources/分摊库.mdb";

    private String diverName = "net.ucanaccess.jdbc.UcanaccessDriver";

    private AccessQueryCriteria accessQueryCriteria;

    public DBQueryExecuter(String dbUrl, String diverName) {
        this.dbUrl = dbUrl;
        this.diverName = diverName;
    }

    public DBQueryExecuter() {
        accessQueryCriteria = new AccessQueryCriteria();
    }

    public AccessQueryCriteria queryTable(String tableName) {
        return accessQueryCriteria.internalQuery(tableName, dbUrl, diverName);
    }
    //   public static List<Map<String, Object>> get
}
