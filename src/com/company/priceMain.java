package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class priceMain {

    public static void main(String[] args) {
        System.out.println("[HttpURLConnection 사용해  get 방식 데이터 요청 및 응답 값 확인 실시]");

        /*[설 명]
         * 1. HttpURLConnection은 http 통신을 수행할 객체입니다
         * 2. URL 객체로 connection을 만듭니다
         * 3. 응답받은 결과를 InputStream으로 받아서 버퍼에 순차적으로 쌓습니다
         * */

        //데이터 정의 실시
        String url = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo";
        String params = "resultType=json&ServiceKey=0tel5j%2B2K0SOzwcdFiF6yYbdmPR%2BrQr6Fp8YmYw8c%2Fu9Fem7nz8ex9XFnbwcw4St12SX6IgmSTtKKb0UaKl4CQ%3D%3D";

        //메소드 호출 실시
        httpGetConnection(url, params);

    }

    public static void httpGetConnection(String UrlData, String ParamData) {

        //http 요청 시 url 주소와 파라미터 데이터를 결합하기 위한 변수 선언
        String totalUrl = "";
        if(ParamData != null && ParamData.length() > 0 &&
                !ParamData.equals("") && !ParamData.contains("null")) { //파라미터 값이 널값이 아닌지 확인
            totalUrl = UrlData.trim().toString() + "?" + ParamData.trim().toString();
        }
        else {
            totalUrl = UrlData.trim().toString();
        }

        //http 통신을 하기위한 객체 선언 실시
        URL url = null;
        HttpURLConnection conn = null;

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        String responseData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        //메소드 호출 결과값을 반환하기 위한 변수
        String returnData = "";

        try {
            //파라미터로 들어온 url을 사용해 connection 실시
            url = new URL(totalUrl);
            conn = (HttpURLConnection) url.openConnection();

            //http 요청에 필요한 타입 정의 실시
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");

            //http 요청 실시
            conn.connect();
            System.out.println("http 요청 방식 : "+"GET");
            System.out.println("http 요청 타입 : "+"application/json");
            System.out.println("http 요청 주소 : "+UrlData);
            System.out.println("http 요청 데이터 : "+ParamData);
            System.out.println("");

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
            returnData = sb.toString();

            //http 요청 응답 코드 확인 실시
            String responseCode = String.valueOf(conn.getResponseCode());
            System.out.println("http 응답 코드 : "+responseCode);
            System.out.println("http 응답 데이터 : "+returnData);

            // Parse String -> Json
            procJsonString(returnData);

            //  DB Connection Info
            final String dbUrl = "jdbc:mysql://springboot-db.cqy5hke5r5w3.ap-northeast-2.rds.amazonaws.com:3306/stockinfo?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
            final String userId = "admin";
            final String passWord = "Cc270618!!";
            DataBase DbObj = new DataBase(dbUrl, userId, passWord);

            Connection dbConn = null; //접속을 위한 객체
            Statement st = null;    //쿼리문을 보내기 위한 객체
            dbConn = DriverManager.getConnection(DbObj.getURL(), DbObj.getUSER(), DbObj.getPASSWORD());
            st = dbConn.createStatement();

            ResultSet rs = st.executeQuery("show databases"); // ResultSet은 쿼리문을 보낸후 나온 결과를 가져올 때 사용한다.

            while(rs.next()) {
                System.out.println(rs.getString("Database"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Parsing json to internal data
    public static void procJsonString(String Response) {
        JSONObject jObject = new JSONObject(Response);
        JSONObject resObject = jObject.getJSONObject("response");
        JSONObject bodyObject = resObject.getJSONObject("body");

        int numOfRows = bodyObject.getInt("numOfRows");
        int pageNo = bodyObject.getInt("pageNo");
        int totalCount = bodyObject.getInt("totalCount");

        System.out.println("numOfRows: " + numOfRows);
        System.out.println("pageNo: " + pageNo);
        System.out.println("totalCount: " + totalCount);

        JSONObject itemsObject = bodyObject.getJSONObject("items");
        JSONArray itemArrObj = itemsObject.getJSONArray("item");
        for (int i = 0; i < itemArrObj.length(); i++) {
            JSONObject itemObject = itemArrObj.getJSONObject(i);    // Stock Info Object
            System.out.println("[" + i + "]");
            System.out.println("basDt: " + itemObject.getString("basDt"));
            System.out.println("itmsNm: " + itemObject.getString("itmsNm"));
        }
    }
}