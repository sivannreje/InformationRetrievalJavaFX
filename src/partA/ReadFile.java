package partA;
import javax.print.Doc;
import java.io.*;
import java.nio.CharBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sivan on 12/5/2017.
 */
public class ReadFile {
    private static String pathStr;
    private long positionTracker;
    private File headDir;
    private File[] listOfDirs;
    private Pattern patternDocNo;
    private Pattern patternText;
    //private HashMap<String, String[]> docsFile;
    //private List<Document> documents;

    public ReadFile() {
        patternDocNo = Pattern.compile("(?<=<DOCNO>)(.*?)(?=</DOCNO>)");
        patternText = Pattern.compile("(?<=<TEXT>)(.*?)(?=</TEXT>)");
    }
    public ReadFile(String path){
        pathStr = path;
        headDir = new File(pathStr);
        listOfDirs = headDir.listFiles();
        patternDocNo = Pattern.compile("(?<=<DOCNO>)(.*?)(?=</DOCNO>)");
        patternText = Pattern.compile("(?<=<TEXT>)(.*?)(?=</TEXT>)");
    }

    public void setPathStr(String path){
        pathStr = path;
    }

    public void setListOfDirs(){

        headDir = new File(pathStr);
        listOfDirs = headDir.listFiles();
    }

    public LinkedHashMap<Document, String> readFile(File currentFile) {
        LinkedHashMap<Document, String> documentsOfFile = new LinkedHashMap<Document, String>();

        BufferedReader bufferedReader = null;
        try {
            FileReader fileReader = new FileReader(currentFile);
            bufferedReader = new BufferedReader(fileReader);
            String path = currentFile.getAbsolutePath().substring(currentFile.getAbsolutePath().lastIndexOf('\\')+1);

            StringBuilder fileString = new StringBuilder();

            String temp;
            boolean isPositionDefined = true;
            positionTracker = 0;
            long position = 0;
            while ((temp = bufferedReader.readLine()) != null) {
                if(temp.length() <= 10 && temp.contains("<TEXT>")) {
                    isPositionDefined = false;
                }
                if(!isPositionDefined) {
                    position = positionTracker;
                    isPositionDefined = true;
                }
                long size = temp.getBytes().length;
                positionTracker += size+1;

                fileString.append(temp);
                fileString.append(" ");

                if (temp.length() <= 10 && temp.contains("</TEXT>")) {
                    readDoc(fileString.toString(), position, path, documentsOfFile);
                    fileString = new StringBuilder();
                }

            }
            bufferedReader.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentsOfFile;
    }
    public void readDoc(String documentText,long position, String path, LinkedHashMap<Document, String> documentsOfFile) throws Exception {
        Matcher matchDocNo = patternDocNo.matcher(documentText);
        Document document = new Document();
        String fileName = path.substring(path.lastIndexOf('\\') + 1);
        document.setPath(fileName);
        document.setPositionInFile(position);
        if(matchDocNo.find()) {
            document.setDocNo(matchDocNo.group());
        }
        //String[] docDetails = {document.getPath(), String.valueOf((document.getPositionInFile()))};
        //docsFile.put(document.getDocNo(), docDetails);
        documentsOfFile.put(document, readDocContent(documentText));
    }

    public String readDocContent(String documentText) {
        Matcher matchDocNo = patternDocNo.matcher(documentText);
        if(matchDocNo.find()) {
            String DOCNO = matchDocNo.group();
        }
        Matcher matchText = patternText.matcher(documentText);
        if(matchText.find()) {
            return matchText.group();
        }
        else return null;
    }

    public static void main(String[] args) {
        String path = "D:\\corpus";
        //get to the main directory
        File headDir = new File(path);
        //get all the directories from the main directory:
        File[] listOfDirs = headDir.listFiles();
        //create new readFile
        ReadFile corpus = new ReadFile(path);

        //iterate on all the files in the corpus
        for (int i = 0; i < listOfDirs.length; i++) {
            //get to the wanted file
            File temp = listOfDirs[i];
            File[] currDir = temp.listFiles();
            File currFile = currDir[0];
            corpus.readFile(currFile);

        }
    }
}
