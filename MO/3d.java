import java.io.InputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;

public class MosWithUpdate {
    static int BLOCK_SIZE;

    static class Query implements Comparable<Query> {
        int l, r, t, id;

        public Query(int l, int r, int t, int id) {
            this.l = l;
            this.r = r;
            this.t = t;
            this.id = id;
        }

        @Override
        public int compareTo(Query o) {
            int b1_l = this.l / BLOCK_SIZE;
            int b2_l = o.l / BLOCK_SIZE;
            if (b1_l != b2_l) return Integer.compare(b1_l, b2_l);

            int b1_r = this.r / BLOCK_SIZE;
            int b2_r = o.r / BLOCK_SIZE;
            if (b1_r != b2_r) {
                return (b1_l % 2 == 1) ? Integer.compare(b2_r, b1_r) : Integer.compare(b1_r, b2_r);
            }
            return (b1_r % 2 == 1) ? Integer.compare(o.t, this.t) : Integer.compare(this.t, o.t);
        }
    }

    static class Update {
        int pos, val;

        public Update(int pos, int val) {
            this.pos = pos;
            this.val = val;
        }
    }

    static int[] arr;
    static Update[] updates;
    static int currentAns = 0;

    static void add(int idx) {
        int val = arr[idx];
    }

    static void remove(int idx) {
        int val = arr[idx];
    }

    static void applyUpdate(int uIdx, int l, int r) {
        int pos = updates[uIdx].pos;
        int val = updates[uIdx].val;

        if (pos >= l && pos <= r) {
            remove(pos);
            updates[uIdx].val = arr[pos];
            arr[pos] = val;
            add(pos);
        } else {
            updates[uIdx].val = arr[pos];
            arr[pos] = val;
        }
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

        BLOCK_SIZE = Math.max(1, (int) Math.pow(n, 2.0 / 3.0));

        Query[] queries = new Query[q];
        Update[] updateList = new Update[q + 1];
        int qCount = 0, uCount = 0;

        for (int i = 0; i < q; i++) {
            int type = sc.nextInt();
            if (type == 1) {
                int l = sc.nextInt() - 1;
                int r = sc.nextInt() - 1;
                queries[qCount] = new Query(l, r, uCount, qCount);
                qCount++;
            } else {
                int pos = sc.nextInt() - 1;
                int val = sc.nextInt();
                uCount++;
                updateList[uCount] = new Update(pos, val);
            }
        }

        queries = Arrays.copyOf(queries, qCount);
        updates = updateList;

        Arrays.sort(queries);

        int[] ans = new int[qCount];
        int l = 0, r = -1, t = 0;

        for (Query qry : queries) {
            while (l > qry.l) { l--; add(l); }
            while (r < qry.r) { r++; add(r); }
            while (l < qry.l) { remove(l); l++; }
            while (r > qry.r) { remove(r); r--; }

            while (t < qry.t) { t++; applyUpdate(t, l, r); }
            while (t > qry.t) { applyUpdate(t, l, r); t--; }

            ans[qry.id] = getAnswer();
        }

        for (int i = 0; i < qCount; i++) out.println(ans[i]);
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
