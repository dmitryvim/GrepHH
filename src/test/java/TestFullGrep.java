import java.io.*;
import java.util.Random;

/**
 * Created by mhty on 26.02.16.
 */
public class TestFullGrep {
    private static Random random;
    private static int stringLength = 300;
    private static int percentChoose = 100000;
    private static int percentMax = 1000;
    private static int lineCount = 1000000;
    private static String[] filenames = new String[]{
            "file1.txt",
            "file2.txt",
            "file3.txt",
            "file4.txt",
            "file5.txt",
            "file6.txt",
            "file7.txt",
            "file8.txt",
            "file9.txt",
            "file10.txt",
            "file11.txt",
            "file12.txt",
            "file13.txt",
            "file14.txt",
            "file15.txt",
    };
    FilterOutputStream out;

    public static void main(String[] args) {
        String substring = "shool_hh_ru";
        TestFullGrep test = new TestFullGrep();
        test.buildFiles(substring);
        test.searchFiles(substring);

    }

    public void buildFiles(String substring) {
        for (String filename:
                filenames) {
            buildFile(filename, substring);
        }
    }

    public void searchFiles(String substrng) {
        Grep.build(substrng, filenames, out).searchAllFiles();
    }

    public void buildFile(String filename, String substring) {
        buildFile(filename, substring, random.nextInt(percentMax), random.nextInt(lineCount));
    }


    public TestFullGrep() {
        this.random = new Random();
        try {
            this.out = new BufferedOutputStream(new FileOutputStream("ouput1.txt"));
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }
    }

    private char getRandomChar() {
//        int shift = random.nextInt(26 * 2 + 10 + 2);
//        if (shift < 26) {
//            return (char) ('a' + shift);
//        }
//        shift -= 26;
//        if (shift < 26) {
//            return (char) ('A' + shift);
//        }
//        shift -= 26;
//        if (shift < 10) {
//            return (char) ('0' + shift);
//        }
//        shift -= 10;
//        return (shift == 1) ? ' ' : '_';
        return '_';

    }

    private String getRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(getRandomChar());
        }
        return stringBuilder.toString();
    }

    private String getRandomLine(int length) {
        return getRandomString(length) + "\n";
    }

    private String getRandomLineContains(String substring, int length) {
        if (length < substring.length()) {
            return substring + "\n";
        }
        length -= substring.length();
        int substringPosition = random.nextInt(length);
        return getRandomString(substringPosition) + substring + getRandomString(length - substringPosition) + "\n";
    }

    private void buildFile(String filename, String substring, int percentProbability, int lineCount) {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename))) {
            for (int i = 0; i < lineCount; i++) {
                out.write(random.nextInt(percentChoose) < percentProbability
                        ? getRandomLineContains(substring, stringLength).getBytes()
                        : getRandomLine(stringLength).getBytes());
            }

        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

//        try (InputStream bis = new BufferedInputStream(new FileInputStream(currentFile))) {
//            bis.read(arr);
//            fileString = new String(arr);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Cant read to String " + chunkSize + " bytes for " + fileName);
//        }
    }
}
