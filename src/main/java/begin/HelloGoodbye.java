package begin;

public class HelloGoodbye {
    public static void main(String[] args) {
        String[] data = args;

        System.out.printf("Hello %s and %s.\n", data[0], data[1]);
        System.out.printf("Goodbye %s and %s.\n", data[1], data[0]);
    }
}
