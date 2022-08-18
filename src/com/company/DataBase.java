package com.company;

public class DataBase {
    private static  String URL; // "jdbc:mysql://springboot-db.cqy5hke5r5w3.ap-northeast-2.rds.amazonaws.com:3306/stockinfo?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
    private static  String USER; // "admin";
    private static  String PASSWORD; // "Cc270618!!";
    private static int timeout;

    public DataBase(String url, String userId, String passWord) {
        this.URL = url;
        this.USER = userId;
        this.PASSWORD = passWord;
    }

    public static String getURL() {
        return URL;
    }

    public static void setURL(String URL) {
        DataBase.URL = URL;
    }

    public static String getUSER() {
        return USER;
    }

    public static void setUSER(String USER) {
        DataBase.USER = USER;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setPASSWORD(String PASSWORD) {
        DataBase.PASSWORD = PASSWORD;
    }

    public boolean connectDataBase() {

        return true;
    }
}
