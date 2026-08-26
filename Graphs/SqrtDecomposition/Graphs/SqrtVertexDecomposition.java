import java.io.*;
import java.util.*;

// first thing to do is assign the S acc to the size of the n and the query 
// the one thing is for heavy we would always have the answer , we should not iterate for the heavy nodes 
// for the light node we can just brute force 
// we are using this IntList because iterating in the hashset causing new creation of Iterator at every step so it is easily causing tle 
// we add edge , remove edge , promote a edge if it is heavyedge and dont want to demote any edge to light because it is waste of computation , whatever is the degree the heavy would be fine 



public class SqrtVertexDecomposition {

    static class IntList {
        int[] data = new int[4];
        int size = 0;

        void add(int val) {
            if (size == data.length) {
                int[] next = new int[data.length << 1];
                System.arraycopy(data, 0, next, 0, size);
                data = next;
            }
            data[size++] = val;
        }

        void removeValue(int val) {
            for (int i = 0; i < size; i++) {
                if (data[i] == val) {
                    data[i] = data[size - 1]; 
                    size--;
                    return;
                }
            }
        }
    }

    static final int S = 400; // Sqrt threshold (adjust based on total edges + queries
    static IntList[] adj;          // Full adjacency list
    static IntList[] heavyEdges;   // Contains ONLY heavy neighbors of node i
    static boolean[] isHeavy;      // True if degree >= S
    static int[] heavyAns;         // Precalculated answer for heavy nodes
    static boolean[] state;        // Current active/online state of node i

    public static void init(int n) {
        adj = new IntList[n + 1];
        heavyEdges = new IntList[n + 1];
        isHeavy = new boolean[n + 1];
        heavyAns = new int[n + 1];
        state = new boolean[n + 1];
        
        for (int i = 0; i <= n; i++) {
            adj[i] = new IntList();
            heavyEdges[i] = new IntList();
        }
    }

    // Toggle State (e.g., Online/Offline or Color Change)
    public static void toggleState(int u, boolean newState) {
        if (state[u] == newState) return;
        state[u] = newState;
        int delta = newState ? 1 : -1;
        
        IntList heavies = heavyEdges[u];
        for (int i = 0; i < heavies.size; i++) {
            heavyAns[heavies.data[i]] += delta;
        }
    }

    // Add Edge with Dynamic Heavy Promotion
    public static void addEdge(int u, int v) {
        adj[u].add(v);
        adj[v].add(u);

        if (isHeavy[u]) {
            heavyEdges[v].add(u);
            if (state[v]) heavyAns[u]++;
        }
        if (isHeavy[v]) {
            heavyEdges[u].add(v);
            if (state[u]) heavyAns[v]++;
        }

        checkPromotion(u);
        checkPromotion(v);
    }

    // Remove Edge (No demotion needed)
    public static void removeEdge(int u, int v) {
        adj[u].removeValue(v);
        adj[v].removeValue(u);

        if (isHeavy[u]) {
            if (state[v]) heavyAns[u]--;
            heavyEdges[v].removeValue(u);
        }
        if (isHeavy[v]) {
            if (state[u]) heavyAns[v]--;
            heavyEdges[u].removeValue(v);
        }
    }

    // Query Answer for Node u
    public static int query(int u) {
        if (isHeavy[u]) {
            return heavyAns[u];
        }
        int count = 0;
        IntList neighbors = adj[u];
        for (int i = 0; i < neighbors.size; i++) {
            if (state[neighbors.data[i]]) count++;
        }
        return count;
    }

    // Promote node from Light to Heavy once degree hits threshold S
    private static void checkPromotion(int u) {
        if (!isHeavy[u] && adj[u].size >= S) {
            isHeavy[u] = true;
            heavyAns[u] = 0;
            IntList neighbors = adj[u];
            for (int i = 0; i < neighbors.size; i++) {
                int v = neighbors.data[i];
                if (state[v]) heavyAns[u]++;
                heavyEdges[v].add(u);
            }
        }
    }
}
