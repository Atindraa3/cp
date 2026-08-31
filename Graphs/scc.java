import java.util.*;

public class SCC {
    static List<List<Integer>> adj;
    static List<List<Integer>> revAdj;
    static Stack<Integer> stack;
    static boolean[] visited;
    static int comp[];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Need to find scc and if size of scc is 1 return true
        adj = new ArrayList<>();
        int n = sc.nextInt();
        int m = sc.nextInt();
        revAdj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
            revAdj.add(new ArrayList<>());
        }
        comp = new int[n + 1];
        stack = new Stack<>();
        visited = new boolean[n + 1];
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            adj.get(u).add(v);
            revAdj.get(v).add(u);
        }

        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                dfs(i);
            }
        }
        // now I have the order in which it starts and in which it finish
        visited = new boolean[n + 1];
        int scc = 0;
        while (!stack.isEmpty()) {

            int node = stack.pop();
            if (!visited[node]) {
                scc++;
                dfs2(node, scc);
            }

        }
        
        System.out.println(scc);
        for (int i = 1; i <= n; i++) {
            System.out.print(comp[i] + " ");
        }
    }

    static void dfs(int node) {
        visited[node] = true;
        for (int x : adj.get(node)) {
            if (!visited[x]) {
                dfs(x);
            }
        }
        stack.add(node);
    }
    
    static void dfs2(int node, int compId) {
        comp[node] = compId;
        visited[node] = true;
        for (int x : revAdj.get(node)) {
            if (!visited[x]) {
                dfs2(x, compId);
            }
        }
    }

}
