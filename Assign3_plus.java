public class Assign3_plus {
    public static void main(String[] args) {
        try {
            if (args.length != 2) System.exit(1);
            int a = Integer.parseInt(args[0]);
            int b = Integer.parseInt(args[1]);
            if (a < 0 || b < 0) System.exit(1);
            System.out.println(Math.addExact(a, b));
        } catch (Exception e) {
            System.exit(1);
        }
    }
}
