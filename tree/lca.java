// fastest method because we are using the iterative dfs 
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;
public class Main {
    static int [][] par;
    static int [] depth;
    static List<List<Integer>> list;
    static final int LOG = 20;

    public static void main(String [] args) {
        FastScanner sc = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        if (n == -1) return;

        list = new ArrayList<>();
        for(int i = 0;i < n;i++) list.add(new ArrayList<>());

        for(int i = 0;i < n - 1;i++) {
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;
            list.get(u).add(v);
            list.get(v).add(u);
        }

        par = new int[n][LOG];
        depth = new int[n];
        for(int i = 0;i < n;i++) Arrays.fill(par[i] , -1);

        iterativeDFS(0 , n);

        for(int k = 1;k < LOG;k++) {
            for(int i = 0;i < n;i++) {
                if(par[i][k - 1] != -1) {
                    par[i][k] = par[par[i][k - 1]][k - 1];
                }
            }
        }

        int q = sc.nextInt();
        while(q-- > 0) {
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;
            out.println(lca(u , v) + 1);
        }
        out.flush();
    }

    static void iterativeDFS(int root , int n) {
        int [] stack = new int[n];
        int [] edgeIdx = new int[n];
        int top = 0;

        stack[0] = root;
        par[root][0] = -1;
        depth[root] = 0;

        while(top >= 0) {
            int u = stack[top];
            if(edgeIdx[u] < list.get(u).size()) {
                int v = list.get(u).get(edgeIdx[u]++);
                if(v != par[u][0]) {
                    par[v][0] = u;
                    depth[v] = depth[u] + 1;
                    top++;
                    stack[top] = v;
                }
            } else {
                top--;
            }
        }
    }

    static int lca(int u , int v) {
        if(depth[u] < depth[v]) {
            int temp = u; u = v; v = temp;
        }

        int diff = depth[u] - depth[v];
        for(int k = LOG - 1;k >= 0;k--) {
            if(((diff >> k) & 1) == 1) {
                u = par[u][k];
            }
        }

        if(u == v) return u;

        for(int k = LOG - 1;k >= 0;k--) {
            if(par[u][k] != par[v][k]) {
                u = par[u][k];
                v = par[v][k];
            }
        }

        return par[u][0];
    }

    static class FastScanner {
        private final InputStream in = System.in;
        private final byte [] buffer = new byte[1 << 16];
        private int ptr = 0 , buflen = 0;

        private boolean hasNextByte() {
            if(ptr < buflen) return true;
            ptr = 0;
            try { buflen = in.read(buffer , 0 , buffer.length); }
            catch(IOException e) { e.printStackTrace(); }
            return buflen > 0;
        }

        private int readByte() { return hasNextByte() ? buffer[ptr++] : -1; }

        public int nextInt() {
            int b = readByte();
            while(b <= ' ') {
                if(b == -1) return -1;
                b = readByte();
            }
            int res = 0;
            do {
                if(b < '0' || b > '9') break;
                res = res * 10 + b - '0';
                b = readByte();
            } while(true);
            return res;
        }
    }
}
