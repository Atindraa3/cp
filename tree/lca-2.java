import java.util.*;

public class LCAQueries {
    static int[][] par;
    static int maxK = 21;
    static List<List<Integer>> adj;
    static int[] depth;
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        par = new int[n + 1][maxK];
        depth = new int[n + 1];
        adj = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            adj.add(new ArrayList<>());
        par[1][0] = 1;
        for (int i = 2; i <= n; i++) {
            par[i][0] = sc.nextInt();
            adj.get(i).add(par[i][0]);
            adj.get(par[i][0]).add(i);
        }
        for (int j = 1; j < maxK; j++) {
            for (int i = 1; i <= n; i++) {
                par[i][j] = par[par[i][j - 1]][j - 1];
            }
        }
        dfs(1, 0);
        // System.out.println(Arrays.toString(depth));
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            System.out.println(LCA(u , v));
        }

    }

    static void dfs(int node, int par) {
        depth[node] = depth[par] + 1;
        for (int child : adj.get(node)) {
            if (child == par)
                continue;
            dfs(child, node);
        }
    }

    static int LCA(int u, int v) {
        if(u == v)
            return u;
        if(depth[u] < depth[v])
            return LCA(v, u);
        int diff = depth[u] - depth[v];
        for (int i = maxK - 1; i >= 0; i--) {
            if ((diff & (1 << i)) != 0) {
                u = par[u][i];
            }
        }
        if (u == v)
            return v;
        // now they both are on same level
        for (int i = maxK - 1; i >= 0; i--) {
            if (par[u][i] != par[v][i]) {
                u = par[u][i];
                v = par[v][i];
            }
        }
        return par[u][0];
    }
}
