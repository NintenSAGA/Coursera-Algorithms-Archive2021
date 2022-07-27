package wordnet;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.nio.file.Path;
import java.util.HashMap;

public class WordNet {
    private final String[] synSets;
    private final HashMap<String, Bag<Integer>> nounSubsets;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        In synIn = new In(synsets), hyperIn = new In(hypernyms);
        String[] synArray = synIn.readAllLines();

        int synNum = synArray.length;
        Digraph digraph = new Digraph(synNum);

        this.synSets = new String[synNum];
        this.nounSubsets = new HashMap<>();

        for (int i = 0; i < synNum; i++) {
            synSets[i] = synArray[i].split(",")[1];
            for (String noun : synSets[i].split(" ")) {
                if (!nounSubsets.containsKey(noun))
                    nounSubsets.put(noun, new Bag<>());
                nounSubsets.get(noun).add(i);
            }
        }

        while (!hyperIn.isEmpty()) {
            String[] args = hyperIn.readLine().split(",");
            for (int i = 1; i < args.length; i++) {
                digraph.addEdge(Integer.parseInt(args[0]), Integer.parseInt(args[i]));
            }
        }

        // check DAG
        if (!new Topological(digraph).hasOrder()) throw new IllegalArgumentException();
        // check single root
        boolean rooted = false;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) {
                if (rooted) throw new IllegalArgumentException();
                rooted = true;
            }
        }
        if (!rooted) throw new IllegalArgumentException();

        this.sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        Queue<String> ret = new Queue<>();
        for (String noun : nounSubsets.keySet()) ret.enqueue(noun);
        return ret;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounSubsets.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return sap.length(nounSubsets.get(nounA), nounSubsets.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return synSets[sap.ancestor(nounSubsets.get(nounA), nounSubsets.get(nounB))];
    }
}