import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FastScanner sc = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);
        solve(sc, out);
        out.flush();
    }
    
    // Custom lightweight primitive dynamic array (int[])
    static class IntList {
        int[] data;
        int size;

        public IntList() {
            data = new int[4];
            size = 0;
        }

        public void add(int val) {
            if (size == data.length) {
                int[] next = new int[data.length << 1];
                System.arraycopy(data, 0, next, 0, size);
                data = next;
            }
            data[size++] = val;
        }

        public void removeValue(int val) {
            for (int i = 0; i < size; i++) {
                if (data[i] == val) {
                    data[i] = data[size - 1]; // Swap with last element
                    size--;
                    return;
                }
            }
        }
    }

    static IntList[] adj;
    static IntList[] heavyEdges;
    static boolean[] isHeavy;
    static int[] heavyAns;
    static boolean[] isOnline;
    static final int S = 400; // Sqrt threshold
    
    static void solve(FastScanner sc, PrintWriter out) throws IOException {
        int n = sc.nextInt();
        int m = sc.nextInt();
        int q = sc.nextInt();
        
        isOnline = new boolean[n + 1];
        heavyAns = new int[n + 1];
        isHeavy = new boolean[n + 1];
        adj = new IntList[n + 1];
        heavyEdges = new IntList[n + 1];
        
        for (int i = 0; i <= n; i++) {
            adj[i] = new IntList();
            heavyEdges[i] = new IntList();
        }
        
        int o = sc.nextInt();
        for (int i = 0; i < o; i++) {
            isOnline[sc.nextInt()] = true;
        }
        
        for (int i = 0; i < m; i++) {
            addEdge(sc.nextInt(), sc.nextInt());
        }
        
        for (int i = 0; i < q; i++) {
            char ch = sc.nextChar();
            if (ch == 'O') {
                int user = sc.nextInt();
                isOnline[user] = true;
                IntList heavies = heavyEdges[user];
                for (int k = 0; k < heavies.size; k++) {
                    heavyAns[heavies.data[k]]++;
                }
            } else if (ch == 'F') {
                int user = sc.nextInt();
                isOnline[user] = false;
                IntList heavies = heavyEdges[user];
                for (int k = 0; k < heavies.size; k++) {
                    heavyAns[heavies.data[k]]--;
                }
            } else if (ch == 'A') {
                addEdge(sc.nextInt(), sc.nextInt());
            } else if (ch == 'D') {
                removeEdge(sc.nextInt(), sc.nextInt());
            } else if (ch == 'C') {
                int node = sc.nextInt();
                if (isHeavy[node]) {
                    out.println(heavyAns[node]);
                } else {
                    int res = 0;
                    IntList neighbors = adj[node];
                    for (int k = 0; k < neighbors.size; k++) {
                        if (isOnline[neighbors.data[k]]) res++;
                    }
                    out.println(res);
                }
            }
        }
    }
    
    static void addEdge(int u, int v) {
        adj[u].add(v);
        adj[v].add(u);
        
        if (isHeavy[u]) {
            heavyEdges[v].add(u);
            if (isOnline[v]) heavyAns[u]++;
        }
        if (isHeavy[v]) {
            heavyEdges[u].add(v);
            if (isOnline[u]) heavyAns[v]++;
        }
        
        checkPromotion(u);
        checkPromotion(v);
    }
    
    static void removeEdge(int u, int v) {
        adj[u].removeValue(v);
        adj[v].removeValue(u);
        
        if (isHeavy[u]) {
            if (isOnline[v]) heavyAns[u]--;
            heavyEdges[v].removeValue(u);
        }
        if (isHeavy[v]) {
            if (isOnline[u]) heavyAns[v]--;
            heavyEdges[u].removeValue(v);
        }
    }
    
    static void checkPromotion(int node) {
        if (!isHeavy[node] && adj[node].size >= S) {
            isHeavy[node] = true;
            heavyAns[node] = 0;
            IntList neighbors = adj[node];
            for (int k = 0; k < neighbors.size; k++) {
                int v = neighbors.data[k];
                if (isOnline[v]) heavyAns[node]++;
                heavyEdges[v].add(node);
            }
        }
    }

    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int head = 0;
        private int tail = 0;

        private int read() throws IOException {
            if (head >= tail) {
                head = 0;
                tail = in.read(buffer, 0, buffer.length);
                if (tail <= 0) return -1;
            }
            return buffer[head++];
        }

        public char nextChar() throws IOException {
            int c = read();
            while (c <= ' ') c = read();
            return (char) c;
        }

        public int nextInt() throws IOException {
            int c = read();
            while (c <= ' ') c = read();
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = read();
            }
            int res = 0;
            while (c >= '0' && c <= '9') {
                res = res * 10 + c - '0';
                c = read();
            }
            return neg ? -res : res;
        }
    }
}
