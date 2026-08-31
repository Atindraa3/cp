public class SegmentTree {
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
