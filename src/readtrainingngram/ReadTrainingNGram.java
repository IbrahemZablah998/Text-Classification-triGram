
package readtrainingngram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ReadTrainingNGram {
    public static  String currentDirectory;
    public static ArrayList<String> arrliStopWord = new ArrayList<String>();
    public static Stemmer stemmer = new Stemmer();
    public static ArrayList<String> arrli = new ArrayList<String>();
    public static HashMap<String, Integer> mapTowWord = new HashMap<>();
    public static HashMap<String, Integer> mapThreeWord = new HashMap<>();
    public static HashMap<String, Double> hashMapTest = new HashMap<>();
    public static ArrayList<String> arrliTest = new ArrayList<String>();
    public static ArrayList<String> arrliTowWordTest = new ArrayList<String>();
    public static ArrayList<String> arrliThreeWordTest = new ArrayList<String>();
    // training Data
    public static void readFile(File file) throws Exception {
        File root = file ;
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                readFile( f.getAbsoluteFile());
            }
            else {
                Analysis(f);
            }
        }

    }
    public static void Analysis(File f) throws Exception {
        currentDirectory = System.getProperty("user.dir");        
        File file = f.getAbsoluteFile();
        StringBuilder r = readFiles(file);
        StringTokenizer st = new StringTokenizer(r.toString()," 0123456789\t\t\t\r\f,\"/.:)(;?!+*@#$%^&=[]'><-)(_");
        
        while (st.hasMoreTokens()) {

            String tokn = st.nextToken().trim().toUpperCase();
            
            boolean isWhitespace = tokn.matches("^\\s*$");
            if (tokn.length() < 3 || arrliStopWord.contains(tokn) || tokn.isEmpty()) continue;
            else if (isWhitespace == false) {
                
                String stem = stemmer.stemWord(tokn);
                arrli.add(stem);
            }
        }

    }
    public static StringBuilder readFiles(File file) throws Exception {
        BufferedReader inFile;
        StringBuilder stringBuilder = new StringBuilder();

        inFile = new BufferedReader(new FileReader(file));
        Scanner scanner = new Scanner(inFile);

        while(scanner.hasNext()){
            String line = scanner.next().toUpperCase();
            stringBuilder.append(line);
            stringBuilder.append(" ");
        }
        scanner.close();
        return stringBuilder;
    }
    public static void stopWord () throws Exception {
        
        File stopWord = new File (currentDirectory + "/StopWord.txt");
        BufferedReader inFile = new BufferedReader(new FileReader(stopWord));
        
        Scanner scanner = new Scanner(inFile);
        while(scanner.hasNextLine()){
            arrliStopWord.add(scanner.nextLine().toUpperCase());
        }
        scanner.close();
    }
    public static void analysisTwoGram () {

        for (int i = 0 ; i < arrli.size() -1; i ++) {
            
            String text = (arrli.get(i) + " " + arrli.get(i < arrli.size() ? i+1 : i));
            if (mapTowWord.containsKey(text)) {
                Integer numBack = mapTowWord.get(text);
                numBack += 1;
                mapTowWord.put(text, numBack);   
            }
            else {
                mapTowWord.put(text, 1);
            }
              
        }
    }   
    public static void analysisThreeGram () {

        for (int i = 0 ; i < arrli.size() -2; i ++) {
            
            String text = (arrli.get(i) + " " + arrli.get(i < arrli.size() ? i+1 : i) + " " + 
                    arrli.get(i < arrli.size() ? i+2 : i+1));
            if (mapThreeWord.containsKey(text)) {
                Integer numBack = mapThreeWord.get(text);
                numBack += 1;
                mapThreeWord.put(text, numBack);   
            }
            else {
                mapThreeWord.put(text, 1);
            }
              
        }
    }
    public static void resultprobilites () throws Exception {
       currentDirectory = System.getProperty("user.dir");
       File file = new File (currentDirectory + "/result.txt");
       PrintWriter printWriter = new PrintWriter (file);

        for (String name: mapThreeWord.keySet()){ 
            String key =name.toString();
            String names[] = key.split(" ");
            String value = mapThreeWord.get(name).toString();  
           
            if (mapTowWord.containsKey(names[0] + " " + names[1])) {
                hashMapTest.put(name, (Double.parseDouble(value)+1)/(mapTowWord.get(names[0] + " " + names[1])+mapTowWord.size()));
                printWriter.println(name + " "+ (Double.parseDouble(value)+1)/(mapTowWord.get(names[0] + " " + names[1])+mapTowWord.size()));
            }

        }
        printWriter.close();
    } 
    // test Data 
    public static void AnalysisTest(File f) throws Exception {     
        File file = f.getAbsoluteFile();
        StringBuilder r = readFilesTest(file);
        StringTokenizer st = new StringTokenizer(r.toString()," 0123456789\t\t\t\r\f,\"/.:)(;?!+*@#$%^&=[]'><-)(_");
        
        while (st.hasMoreTokens()) {

            String tokn = st.nextToken().trim().toUpperCase();
            
            boolean isWhitespace = tokn.matches("^\\s*$");
            if (tokn.length() < 3 || arrliStopWord.contains(tokn) || tokn.isEmpty()) continue;
            else if (isWhitespace == false) {
                
                String stem = stemmer.stemWord(tokn);
                arrliTest.add(stem);  
            }
        }

    }
    public static StringBuilder readFilesTest(File file) throws Exception {
        BufferedReader inFile;
        StringBuilder stringBuilder = new StringBuilder();

        inFile = new BufferedReader(new FileReader(file));
        Scanner scanner = new Scanner(inFile);

        while(scanner.hasNext()){
            String line = scanner.next().toUpperCase();
            stringBuilder.append(line);
            stringBuilder.append(" ");
        }
        scanner.close();
        return stringBuilder;
    }
    public static void analysisTwoGramTest () {

        for (int i = 0 ; i < arrliTest.size() -1; i ++) {
            String text = (arrliTest.get(i) + " " + arrliTest.get(i < arrliTest.size() ? i+1 : i));
            arrliTowWordTest.add(text);
            
            }                      
    }
    public static void analysisThreeGramTest () {
        for (int i = 0 ; i < arrliTest.size() -2; i ++) {  
            String text = (arrliTest.get(i) + " " + arrliTest.get(i < arrliTest.size() ? i+1 : i) + " " + 
            arrliTest.get(i < arrliTest.size() ? i+2 : i+1));
            arrliThreeWordTest.add(text);             
        }
        
    }
    public static void resultsTest () {
        double p = 1, num = 0;
        for (String name: arrliThreeWordTest){
            if (hashMapTest.containsKey(name.toString())) {
                p *= hashMapTest.get(name);
            }
            else {
                String r[] = name.split(" "); 
                if (mapTowWord.containsKey((mapTowWord.get(r[0] + " " + r[1])))) {
                    p *= ((double)(1.0)/(((mapTowWord.get(r[0] + " " + r[1])))+mapTowWord.size()));
                }
                else {
                    System.out.println("error detection => " + (r[0] + " " + r[1]));
                    p *= ((double)(1.0)/((1)+mapTowWord.size()));
                }
            }
        }
        String allDocuments = "";
        for (String name: arrliTest){
            allDocuments += name;
            allDocuments += " ";
        }
       System.out.println(allDocuments + "probilites is = " + p);
    }
    
    public static void main(String[] args) throws Exception {
        currentDirectory = System.getProperty("user.dir");
        ReadTrainingNGram readTraining = new ReadTrainingNGram();
        stopWord();
        readFile(new File (currentDirectory + "/trainning"));
        analysisTwoGram();
        analysisThreeGram();
        resultprobilites ();
        
        AnalysisTest(new File (currentDirectory + "/test.txt"));
        analysisTwoGramTest();
        analysisThreeGramTest();
        resultsTest();
        
        arrli.clear();
        arrliStopWord.clear();
        mapTowWord.clear();
        hashMapTest.clear();
        arrliTest.clear();
        arrliTowWordTest.clear();
        arrliThreeWordTest.clear();
        mapThreeWord.clear();
        currentDirectory = null;
    }
    
}
