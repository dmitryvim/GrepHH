import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mhty on 15.02.16.
 */
public class Grep {
    final static int BUFFER_SIZE = 256;
    String subString;
    String[] filenames;
    FilterOutputStream out;

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("It's needed 2 arguments:\n grep [substring] [[filename list]]\n");
        }

        Grep.build(args[0], Arrays.copyOfRange(args, 1, args.length), System.out).searchAllFiles();
    }

    public void searchAllFiles() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        for(final String filename: filenames) {
            service.submit((Runnable) () -> search(subString, filename));
        }
        service.shutdown();
    }

    public static Grep build(String subString, String[] Filenames, FilterOutputStream out) {
        return new Grep(subString, Filenames, out);
    }

    private Grep(String subString, String[] Filenames, FilterOutputStream out) {
        this.subString = subString;
        this.filenames = Filenames;
        this.out = out;
    }

    private void search(String subString, String filename) {
        int count;
        try (SeekableByteChannel channel = Files.newByteChannel(Paths.get(filename))) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            StringBuffer line = new StringBuffer();

            do {
                count = channel.read(byteBuffer);
                if (count != -1) {
                    byteBuffer.rewind();
                    for (int i = 0; i < count; i++) {
                        char symbol = (char)byteBuffer.get();
                        if (symbol == '\n') {
                            printIfContainsSubstring(subString, filename, line);
                        } else {
                            line.append(symbol);
                        }


                    }
                }
            } while (count != -1);
            printIfContainsSubstring(subString, filename, line);

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private void printIfContainsSubstring(String subString, String filename, StringBuffer line) {
        if (line.indexOf(subString) > -1) {
            StringBuilder stringBuilder = new StringBuilder(filename.length() + line.length() + 12);
            stringBuilder
                    .append(filename)
                    .append(": .. ")
                    .append(line)
                    .append(" ..\n");
            try {
                out.write(stringBuilder.toString().getBytes());
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        line.setLength(0);
    }


}
