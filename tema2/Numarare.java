import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Numarare {
	static class Task {
		public static final String INPUT_FILE = "numarare.in";
		public static final String OUTPUT_FILE = "numarare.out";
		public static final int MOD = 1000000007;

		List<List<Integer>> commonGraph, graph1, graph2;
		int N, M;

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String[] firstLine = br.readLine().split(" ");
				N = Integer.parseInt(firstLine[0]);
				M = Integer.parseInt(firstLine[1]);
				commonGraph = new ArrayList<>(N + 1);
				graph1 = new ArrayList<>(N + 1);
				graph2 = new ArrayList<>(N + 1);
				int i;
				for (i = 0; i <= N; i++) {
					commonGraph.add(new ArrayList<>());
					graph1.add(new ArrayList<>());
					graph2.add(new ArrayList<>());
				}
				int j;
				for (j = 0; j < M; j++) {
					String[] edge = br.readLine().split(" ");
					int x = Integer.parseInt(edge[0]);
					int y = Integer.parseInt(edge[1]);
					graph1.get(x).add(y);
				}
				int k;
				for (k = 0; k < M; k++) {
					String[] edge = br.readLine().split(" ");
					int x = Integer.parseInt(edge[0]);
					int y = Integer.parseInt(edge[1]);
					graph2.get(x).add(y);
				}
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
				pw.printf("%d\n", result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getResult() {
			//sortez listele de adiacenta din cele 2 grafuri
			for (int i = 1; i <= N; i++) {
				Collections.sort(graph1.get(i));
				Collections.sort(graph2.get(i));
				//pt fiecare nod vecin al unui nod dat verific daca se afla 
				//in lista de adiacenta din graf 2
				for (int node_vecin : graph1.get(i)) {
					if (Collections.binarySearch(graph2.get(i), node_vecin) >= 0) {
						//creez un graf comun cu aceste noduri
						commonGraph.get(i).add(node_vecin);
					}
				}
			}
			int[] dp;
			int k;
			dp = new int[N + 1];
			dp[1] = 1;
			for (k = 1; k <= N; k++) {
				for (int j : commonGraph.get(k)) {
					dp[j] = (dp[j] + dp[k]) % MOD;
				}
			}
			return dp[N];
		}
	}
	public static void main(String[] args) {
		new Task().solve();
	}
}