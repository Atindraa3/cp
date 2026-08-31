import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;
public class MosAlgorithm {
    static int BLOCK_SIZE;

    static class Query implements Comparable<Query> {
        int l, r, id;

        public Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }

        @Override
        public int compareTo(Query o) {
            int b1 = this.l / BLOCK_SIZE;
            int b2 = o.l / BLOCK_SIZE;
            if (b1 != b2) return Integer.compare(b1, b2);
            return (b1 % 2 == 1) ? Integer.compare(o.r, this.r) : Integer.compare(this.r, o.r);
        }
    }

    static int[] arr;
    static int currentAns = 0;

    static void add(int idx) {
        int val = arr[idx];
    }

    static void remove(int idx) {
        int val = arr[idx];
    }

    static int getAnswer() {
        return currentAns;
    }

    public static void main(String[] args) {
        FastScanner sc = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        if (n == -1) return;
        int q = sc.nextInt();

        arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();

        BLOCK_SIZE = Math.max(1, (int) (n / Math.sqrt(q)));

        Query[] queries = new Query[q];
        for (int i = 0; i < q; i++) {
            int l = sc.nextInt() - 1;
            int r = sc.nextInt() - 1;
            queries[i] = new Query(l, r, i);
        }

        Arrays.sort(queries);

        int[] ans = new int[q];
        int l = 0, r = -1;

        for (Query qry : queries) {
            while (l > qry.l) { l--; add(l); }
            while (r < qry.r) { r++; add(r); }
            while (l < qry.l) { remove(l); l++; }
            while (r > qry.r) { remove(r); r--; }
            ans[qry.id] = getAnswer();
        }

        for (int i = 0; i < q; i++) out.println(ans[i]);
        out.flush();
    }

    static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, buflen = 0;

        private boolean hasNextByte() {
            if (ptr < buflen) return true;
            ptr = 0;
            try { buflen = in.read(buffer, 0, buffer.length); }
            catch (IOException e) { e.printStackTrace(); }
            return buflen > 0;
        }

        private int readByte() { return hasNextByte() ? buffer[ptr++] : -1; }

        public int nextInt() {
            int b = readByte();
            while (b <= ' ') {
                if (b == -1) return -1;
                b = readByte();
            }
            int res = 0;
            do {
                if (b < '0' || b > '9') break;
                res = res * 10 + b - '0';
                b = readByte();
            } while (true);
            return res;
        }
    }
}
