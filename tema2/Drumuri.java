import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Drumuri {
	static class Task {
		public static final String INPUT_FILE = "drumuri.in";
		public static final String OUTPUT_FILE = "drumuri.out";

		static class Edge {
			int destination;
			long cost;
			Edge(int destination, long cost) {
				this.destination = destination;
				this.cost = cost;
			}
		}
		int N, M;
		List<Edge>[] graph;
		int x, y, z;
		List<Edge>[] reverseGraph;

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

				graph = new ArrayList[N];
				reverseGraph = new ArrayList[N];
				int i;
				for (i = 0; i < N; i++) {
					graph[i] = new ArrayList<>();
					reverseGraph[i] = new ArrayList<>();
				}
				int j;
				for (j = 0; j < M; j++) {
					String[] informations = br.readLine().split(" ");
					int a = Integer.parseInt(informations[0]) - 1;
					int b = Integer.parseInt(informations[1]) - 1;
					long c = Long.parseLong(informations[2]);
					graph[a].add(new Edge(b, c));
					reverseGraph[b].add(new Edge(a, c));
				}

				String[] lastLine = br.readLine().split(" ");
				x = Integer.parseInt(lastLine[0]) - 1;
				y = Integer.parseInt(lastLine[1]) - 1;
				z = Integer.parseInt(lastLine[2]) - 1;
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
				pw.printf("%d\n", result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private long[] dijkstra(List<Edge>[] graph, int start) {
			//distanta minima
			long[] dist = new long[N];
			//initializez cu o valoare mare caci nu e calculata distanta min
			Arrays.fill(dist, Long.MAX_VALUE);
			dist[start] = 0;
			//vreau sa ordoneze muchiile pe baza costului crescator
			PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingLong(e -> e.cost));
			//incep de la un nod de start
			queue.add(new Edge(start, 0));

			while (!queue.isEmpty()) {
				Edge current;
				//extrag muchia cu cel mai mic cost
				current = queue.poll();
				int node;
				long cost;
				node = current.destination;
				cost = current.cost;
				if (cost > dist[node]) {
					continue;
				}
				//iterez prin toate muchiile nodului curent extras
				for (Edge edge : graph[node]) {
					int new_destination = edge.destination;
					long newDist = dist[node] + edge.cost;
					if (newDist < dist[new_destination]) {
						//am gasit un cost/distanta mai mic
						dist[new_destination] = newDist;
						queue.add(new Edge(new_destination, newDist));
					}
				}
			}
			return dist;
		}

		private long getResult() {
			long[] edgeFromX;
			long minimCost = Long.MAX_VALUE;
			//muchie din x
			edgeFromX = dijkstra(graph, x);
			long[] edgeFromY;
			edgeFromY = dijkstra(graph, y);
			long[] edgeToZ;
			edgeToZ = dijkstra(reverseGraph, z);
			int i;
			for (i = 0; i < N; i++) {
				//verific daca exista drumuri intre i-x/y/z
				if (edgeFromX[i] != Long.MAX_VALUE && edgeFromY[i] 
								!= Long.MAX_VALUE && edgeToZ[i] != Long.MAX_VALUE) {
					long cost = edgeFromX[i] + edgeFromY[i] + edgeToZ[i];
					if (cost < minimCost) {
						minimCost = cost;
					}
				}
			}
			if (minimCost == Long.MAX_VALUE) {
				return -1;
			} else {
				return minimCost;
			}
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}