public class Main {
    public static void main(String[] args) {
        ReadFile f = new ReadFile();
        f.openFile("tmp.txt");
        f.readFile();
        f.closeFile();
    }
}