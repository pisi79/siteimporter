package by.giava.siteimporter.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import by.giava.siteimporter.utils.HtmlCorrector;

import difflib.DiffRow;
import difflib.DiffRowGenerator;

/**
 * Example program to list links from a URL.
 */
public class Corrector
{

   public static void main(String[] args) throws IOException, DocumentException
   {
      // String url = "http://wbpreview.com/previews/WB02G20TX/index.html";
      String url = "http://wbpreview.com/previews/WB0F35928/";
      // print("Fetching %s...", url);
      // String original = download(url);
      // indent(original);
      // System.out.println(replaceExternalResources(downloadCorrect(url), "http://wbpreview.com/previews/WB02G20TX/"));
      System.out.println(replaceExternalResources(downloadCorrect(url), "http://wbpreview.com/previews/WB0F35928/"));

   }

   public static void main2(String[] args) throws IOException
   {
      String url = "http://wbpreview.com/previews/WB02G20TX/index.html";
      print("Fetching %s...", url);
      List<String> original = simpleDownload(url);
      List<String> corrected = downloadAndCorrect(url);
      DiffRowGenerator.Builder builder = new DiffRowGenerator.Builder();
      boolean sideBySide = true; // default -> inline
      builder.showInlineDiffs(!sideBySide);
      builder.columnWidth(120);
      DiffRowGenerator dfg = builder.build();
      List<DiffRow> rows = dfg.generateDiffRows(original, corrected);
      for (DiffRow diffRow : rows)
      {
         System.out.println(diffRow.toString());
      }
   }

   public static String downloadCorrect(String url) throws IOException
   {
      Document doc = Jsoup.connect(url).get();
      // System.out.println(doc.html());
      String content = doc.html();
      return HtmlCorrector.replaceNotPermitted(content);
   }

   public static String replaceExternalResources(String content, String url)
   {
      return content.replaceAll("href=\"css/", "href=\"" + url + "css/")
               .replaceAll("src=\"img/", "src=\"" + url + "img/")
               .replaceAll("href=\"assets/", "href=\"" + url + "assets/")
               .replaceAll("href=\"ico/", "href=\"" + url + "ico/")
               .replaceAll("src=\"js/", "src=\"" + url + "js/")

               .replaceAll("href=\"/css/", "href=\"" + url + "css/")
               .replaceAll("src=\"/img/", "src=\"" + url + "img/")
               .replaceAll("href=\"/assets/", "href=\"" + url + "assets/")
               .replaceAll("href=\"/ico/", "href=\"" + url + "ico/")
               .replaceAll("src=\"/js/", "src=\"" + url + "js/");
   }

   public static List<String> downloadAndCorrect(String url) throws IOException
   {
      Document doc = Jsoup.connect(url).get();
      System.out.println(doc.html());
      String[] lines = doc.html().split("\\r?\\n");
      return Arrays.asList(lines);
   }

   private static List<String> fileToLines(String filename)
   {
      List<String> lines = new LinkedList<String>();
      String line = "";
      try
      {
         BufferedReader in = new BufferedReader(new FileReader(filename));
         while ((line = in.readLine()) != null)
         {
            lines.add(line);
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return lines;
   }

   public static List<String> simpleDownload(String url) throws IOException
   {
      List<String> lines = new ArrayList<String>();
      URL myUrl = new URL(url);
      BufferedReader in = new BufferedReader(
               new InputStreamReader(
                        myUrl.openStream()));
      String line = "";
      while ((line = in.readLine()) != null)
      {
         lines.add(line);
      }

      in.close();
      return lines;
   }

   public static String download(String url) throws IOException
   {
      StringBuffer lines = new StringBuffer();
      URL myUrl = new URL(url);
      BufferedReader in = new BufferedReader(
               new InputStreamReader(
                        myUrl.openStream()));
      String line = "";
      while ((line = in.readLine()) != null)
      {
         lines.append(line);
      }

      in.close();
      return lines.toString();
   }

   private static void print(String msg, Object... args)
   {
      System.out.println(String.format(msg, args));
   }

}