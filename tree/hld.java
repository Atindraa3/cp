import java.util.*;

public class HLD {
    static int[] head;
    static int[] heavy;
    static int[] depth;
    static int[] subtrees;
    static int[] parent;
    static int[] pos;
    static int ptr = 0;
    static List<List<Integer>> adj;
    static SegmentTree seg;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int val[] = new int[n];
        for (int i = 0; i < n; i++) {
            val[i] = sc.nextInt();
        }
        pos = new int[n];
        parent = new int[n];
        head = new int[n];
        heavy = new int[n];
        depth = new int[n];
        subtrees = new int[n];
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++)
            adj.add(new ArrayList<>());
        for (int i = 0; i < n - 1; i++) {
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        dfs(0, -1);
        // System.out.println(Arrays.toString(subtrees));
        // System.out.println(Arrays.toString(heavy));
        // System.out.println(Arrays.toString(depth));
        // System.out.println(Arrays.toString(parent));
        // System.out.println(Arrays.toString(head));
        decompose(0, 0);
        seg = new SegmentTree(val);
        for (int i = 0; i < n; i++) {
            seg.update(pos[i], val[i]);
        }
        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            if (a == 2) {
                int u = sc.nextInt() - 1;
                int v = sc.nextInt() - 1;
                System.out.print(queryMax(u, v) + " ");
            }
            else {
                int node = sc.nextInt() - 1;
                int value = sc.nextInt();
                updateNode(node, value);
            }

        }

    }

    public static void updateNode(int node, int value) {
        seg.update(pos[node], value);
    }

    public static int queryMax(int u, int v) {
        int ans = Integer.MIN_VALUE;
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            // now always the depth u would be max to ensure we would not reach more than head
            // so we can go the the one head with min depth
            ans = Math.max(seg.query(pos[head[u]], pos[u]).max, ans);
            u = parent[head[u]];
        }
        // if both have same head
        if (depth[u] > depth[v]) {
            int tem = u;
            u = v;
            v = tem;
        }
        // now u is greater than v
        if (pos[u] + 1 <= pos[v]) {
            ans = Math.max(ans , seg.query(pos[u] + 1, pos[v]).max);
        }

        return ans;
    }

    public static void dfs(int node, int par) {
        subtrees[node]++;
        for (int child : adj.get(node)) {
            if (child == par)
                continue;
            parent[child] = node;
            depth[child] = 1 + depth[node];
            dfs(child, node);

            subtrees[node] += subtrees[child];
        }
        int heavyEdge = -1;
        int weight = 0;
        for (int child : adj.get(node)) {
            if (child == par)
                continue;
            if (subtrees[child] > weight) {
                heavyEdge = child;
                weight = subtrees[child];
            }
        }
        heavy[node] = heavyEdge;
    }

    static void decompose(int node, int h) {
        head[node] = h;
        pos[node] = ptr++;
        int heavyChild = heavy[node];
        if (heavyChild != -1)
            decompose(heavyChild, h);
        for (int child : adj.get(node)) {
            if (child == parent[node] || child == heavyChild)
                continue;
            decompose(child, child);
        }
    }
    
    public static class SegmentTree {
        static Node[] tree;
        static int n;

        static class Node {
            int max;
            int min;
            int sum;

            Node() {
                this.max = Integer.MIN_VALUE;
                this.min = Integer.MAX_VALUE;
                this.sum = 0;
            }

            Node(int val) {
                this.max = val;
                this.min = val;
                this.sum = val;
            }
        }

        SegmentTree(int[] arr) {
            n = arr.length;
            tree = new Node[4 * n];
            build(1, 0, n - 1, arr);
        }

        private void build(int node, int l, int r, int[] arr) {
            if (l == r) {

                tree[node] = new Node(arr[l]);
                return;
            }
            int mid = (l + r) / 2;
            build(2 * node, l, mid, arr);
            build(2 * node + 1, mid + 1, r, arr);
            tree[node] = merge(tree[2 * node], tree[2 * node + 1]);
        }

        public void update(int index, int val) {
            update(1, 0, n - 1, index, val);
        }

        private void update(int node, int l, int r, int index, int val) {
            if (l == r) {

                tree[node] = new Node(val);
                return;
            }
            int mid = (l + r) / 2;
            if (index <= mid)
                update(2 * node, l, mid, index, val);
            else
                update(2 * node + 1, mid + 1, r, index, val);

            tree[node] = merge(tree[2 * node], tree[2 * node + 1]);
        }

        Node merge(Node a, Node b) {

            if (a.min == Integer.MAX_VALUE)
                return b;
            if (b.min == Integer.MAX_VALUE)
                return a;

            Node res = new Node();
            res.min = Math.min(a.min, b.min);
            res.max = Math.max(a.max, b.max);
            res.sum = a.sum + b.sum;
            return res;
        }

        Node query(int l, int r) {
            return query(1, 0, n - 1, l, r);
        }

        Node query(int node, int l, int r, int s, int e) {
            if (l > e || r < s) {
                return new Node();
            }
            if (s <= l && r <= e) {
                return tree[node];
            }
            int mid = (l + r) / 2;
            return merge(query(node * 2, l, mid, s, e),
                    query(node * 2 + 1, mid + 1, r, s, e));
        }
    }
}
