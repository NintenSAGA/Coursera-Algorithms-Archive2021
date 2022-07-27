package baseballelimination;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public class TestDrive {
    public static void main(String[] args) {
        Path path = Path.of("./");
        In in = new In();
        for (File file : Objects.requireNonNull(path.toFile().listFiles(x -> x.getName().endsWith(".txt")))) {
            BaseballElimination division = new BaseballElimination(file.getName());
            for (String team : division.teams()) {
                if (division.isEliminated(team)) {
                    StdOut.print(team + " is eliminated by the subset R = { ");
                    for (String t : division.certificateOfElimination(team)) {
                        StdOut.print(t + " ");
                    }
                    StdOut.println("}");
                }
                else {
                    StdOut.println(team + " is not eliminated");
                }
            }
            StdOut.println();
            in.readLine();
        }

    }
}
