package crawler;

import dao.daoImpl.PlaceDaoImpl;
import model.Place;
import model.Show;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import other.Map;

import javax.persistence.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;

/**
 * Created by zhuanggangqing on 2018/2/13.
 */
public class DM_crawler {
//    private static String url="https://www.228.com.cn/s/";

//    @PersistenceUnit(name = "ZZ")
//    private EntityManagerFactory factory;
//
//    @PersistenceContext
//    private EntityManager em;

    private static String url = "jdbc:mysql://localhost:3306/ZZ";
    private static String user = "root";
    private static String password = "root";
    static Connection conn = null;
    public static void main(String args[]) throws SQLException, IOException {
        DM_crawler dm_crawler = new DM_crawler();
        try {
            conn = DriverManager.getConnection(url, user, password);
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e){}
        dm_crawler.crawler();
    }

   public void crawler(){
       try {
            PreparedStatement pstm =null;
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/crawler/test.txt"));
            StringBuffer stringBuffer = new StringBuffer();
            String a;
            while ((a=bufferedReader.readLine())!=null){
                stringBuffer.append(a);
            }
            Document document = Jsoup.parse(stringBuffer.toString());
            Elements content_txt = document.select("#content_list").select(".search_txt");
            Elements content_img = document.select("#content_list").select(".search_img");
            Show show;
            String sql = "REPLACE INTO zz_show(name,time,place,price,type,introduction,img,city,map,checkmap) VALUES (?,?,?,?,?,?,?,?,?,?)";
            for(int i = 0; i < content_img.size(); i++){
                a = content_txt.get(i).select(".search_txt_time").text();
                if (a.length()<5){
                    continue;
                }
                if (a.substring(4,5).equals(".")){
                    System.out.println(i);
                    show = new Show();
                    document = Jsoup.connect("https://piao.damai.cn/"+content_txt.get(i).select("h3").select("a").attr("id").split("_")[2]+".html").get();
                    show.setName(content_txt.get(i).select("h3").select("a").text());
//                    System.out.println();
                    try{
                        show.setTime(content_txt.get(i).select(".search_txt_time").text().substring(0,10));
                        show.setType(document.select(".m-crm").text().split(" > ")[2]);
                    }catch (Exception e){
                        continue;
                    }

                    //设置演出信息
                    show.setPlace(content_txt.get(i).select(".c1").text().split(" - ")[0]);
                    show.setCity(content_txt.get(i).select(".c1").text().split(" - ")[1]);
                    show.setPrice("80,180,360,540");

                    show.setIntroduction(content_txt.get(i).select(".search_txt_cut").text());
                    downloadImg(content_img.get(i).select("img").attr("src"),"show");
                    show.setImg("src/Resource/show/"+content_img.get(i).select("img").attr("src").split("/")[6]);
//                    show.setMark();

                    //设置场馆信息
                    getPlace(document.select(".m-venue").select(".ct").select(".txt").select("a").attr("href"),show.getCity());


                    pstm = conn.prepareStatement(sql);
                    pstm.setString(1,show.getName());
                    pstm.setString(2,show.getTime());
                    pstm.setString(3,show.getPlace());
                    pstm.setString(4,show.getPrice());
                    pstm.setString(5,show.getType());
                    pstm.setString(6,show.getIntroduction());
                    pstm.setString(7,show.getImg());
                    pstm.setString(8,show.getCity());
                    pstm.setString(9, Map.getMap());
                    pstm.setString(10,Map.getMap());
                    pstm.executeUpdate();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
           e.printStackTrace();
       }
   }

    private static void downloadImg(String url,String type){
        String newUrl="https:"+url;
        try {
            URL U = new URL(newUrl);
            URLConnection urlConnection = U.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            File file = new File("src/Resource/"+type);
            if(!file.exists()){
                System.out.println(file.mkdirs());
            }
            OutputStream outputStream = new FileOutputStream(new File("src/Resource/"+type+"/"+url.split("/")[6]));
            byte[] buf = new byte[1024];
            int l = 0;
            while ((l = inputStream.read(buf)) != -1){
                outputStream.write(buf,0,l);
            }
            outputStream.close();
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void downloadPlaceImg(String url,String type){
        String newUrl="https:"+url;
        try {
            URL U = new URL(newUrl);
            URLConnection urlConnection = U.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            File file = new File("src/Resource/"+type);
            if(!file.exists()){
                System.out.println(file.mkdirs());
            }
            OutputStream outputStream = new FileOutputStream(new File("src/Resource/"+type+"/"+url.split("=")[1]+".jpg"));
            byte[] buf = new byte[1024];
            int l = 0;
            while ((l = inputStream.read(buf)) != -1){
                outputStream.write(buf,0,l);
            }
            outputStream.close();
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getPlace(String url,String city) throws IOException {
        PreparedStatement pstm =null;
        String newUrl="https:"+url;
        Document document = Jsoup.connect(newUrl).get();
        String sql = "REPLACE INTO zz_place(name,city,introduction,img,id,passwd,state,balance) VALUES (?,?,?,?,?,?,?,?)";
        Place place = new Place();
        place.setName(document.select("#VenueName").text());
        place.setIntroduction(document.select("#agree").text());
        place.setCity(city);
        place.setImg("src/Resource/place/"+document.select(".venueDetal").select(".img").attr("src").split("=")[1]+".jpg");
        downloadPlaceImg(document.select(".venueDetal").select(".img").attr("src"),"place");
        place.setId(PlaceDaoImpl.get_random_id());
        place.setPasswd("123");
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1,place.getName());
            pstm.setString(3,place.getIntroduction());
            pstm.setString(2,place.getCity());
            pstm.setString(4,place.getImg());
            pstm.setString(5,place.getId());
            pstm.setString(6,place.getPasswd());
            pstm.setInt(7,1);
            pstm.setDouble(8,0.0);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
