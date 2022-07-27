package baseballelimination;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

public class BaseballElimination {
    private final int n;
    private final String[] teams;
    private final HashMap<String, Integer> teamNum;
    private final TreeMap<String, Bag<String>> elimination;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    private int maxI;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();

        In in = new In(filename);
        n = in.readInt();
        teams = new String[n];
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        teamNum = new HashMap<>();
        elimination = new TreeMap<>();
        maxI = 0;
        int maxW = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            teams[i] = in.readString();
            teamNum.put(teams[i], i);
            w[i] = in.readInt();
            if (maxW < w[i]) {
                maxI = i;
                maxW = w[i];
            }
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        teamValidate(team);

        return w[teamNum.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        teamValidate(team);

        return l[teamNum.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        teamValidate(team);

        return r[teamNum.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        teamValidate(team1);
        teamValidate(team2);

        return g[teamNum.get(team1)][teamNum.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        teamValidate(team);

        if (!elimination.containsKey(team)) setElimination(team);

        return elimination.get(team).size() != 0;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (isEliminated(team)) return elimination.get(team);
        return null;
    }

    private void setElimination(String team) {
        int gameNum = (n-1)*(n-2)/2;
        int x = teamNum.get(team);
        int vertex = 1;
        int teamRadix = 1 + gameNum;
        Bag<String> bag = new Bag<>();

        if (w[x] + r[x] < w[maxI]) {
            bag.add(teams[maxI]);
            elimination.put(team, bag);
            return;
        }

        FlowNetwork flowNetwork = new FlowNetwork(1 + gameNum + n-1 + 1);

        for (int i = 0; i < n-1; i++) {
            if (i == x) continue;
            for (int j = i+1; j < n; j++) {
                if (j == x) continue;
                flowNetwork.addEdge(new FlowEdge(0, vertex, g[i][j]));
                flowNetwork.addEdge(gameToTeam(vertex, i, x, teamRadix));
                flowNetwork.addEdge(gameToTeam(vertex, j, x, teamRadix));
                vertex++;
            }
        }

        for (int i = 0; i < n; i++) {
            if (i == x) continue;
            int t = i;
            if (i > x) t--;
            if (w[x] + r[x] - w[i] > 0)
                flowNetwork.addEdge(new FlowEdge(teamRadix + t, flowNetwork.V()-1, w[x] + r[x] - w[i]));
            else {
                flowNetwork.addEdge(new FlowEdge(teamRadix + t, flowNetwork.V()-1, 0));
            }
        }

        FordFulkerson ff = new FordFulkerson(flowNetwork, 0, flowNetwork.V()-1);

        for (int i = 0; i < n; i++) {
            if (i == x) continue;
            int t = i;
            if (t > x) t--;
            if (ff.inCut(teamRadix + t)) bag.add(teams[i]);
        }

        elimination.put(team, bag);
    }

    private FlowEdge gameToTeam(int vertex, int i, int x, int teamRadix) {
        if (i > x) i--;
        return new FlowEdge(vertex, teamRadix + i, Double.MAX_VALUE);
    }

    private void teamValidate(String team) {
        if (team == null) throw new IllegalArgumentException();
        if (!teamNum.containsKey(team)) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        URL url = ClassLoader.getSystemResource("baseball_elimination");
        assert url != null;
        String dir = url.getFile();
        File files = new File(dir);

        In in = new In();
        for (File file : Objects.requireNonNull(files.listFiles(x -> x.getName().endsWith(".txt")))) {
            BaseballElimination division = new BaseballElimination(Path.of(dir, file.getName()).toString());
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
