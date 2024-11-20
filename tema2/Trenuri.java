import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Trenuri {
	static class Task {
		public static final String INPUT_FILE = "trenuri.in";
		public static final String OUTPUT_FILE = "trenuri.out";
		int M;
		Map<String, List<String>> graph;
		String end_city, first_city;

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
				String[] line = br.readLine().split(" ");
				first_city = line[0];
				end_city = line[1];
				M = Integer.parseInt(br.readLine());
				int i;
				graph = new HashMap<>();
				for (i = 0; i < M; i++) {
					String[] route = br.readLine().split(" ");
					String start = route[0];
					graph.putIfAbsent(start, new ArrayList<>());
					String destination = route[1];
					graph.get(start).add(destination);
				}
				br.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} 
		}

		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new FileWriter(OUTPUT_FILE));
				pw.println(result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getResult() {
			List<String> newOrder;
			Set<String> allCities = new HashSet<>(graph.keySet());
			//set cu toate orasele existente
			for (List<String> vecini : graph.values()) {
				allCities.addAll(vecini);
			}
			//obtin noua ordine a nodurilor
			newOrder = topologicalSort(graph, allCities);
			//aici salvez orasul si indicele sau corespunzator in ord.topologica
			Map<String, Integer> indexMap = new HashMap<>();
			int i;
			for (i = 0; i < newOrder.size(); i++) {
				indexMap.put(newOrder.get(i), i);
			}
			int[] dist;
			dist = new int[allCities.size()];
			//initializez cu -1 ca distanta n a fost calculata inca
			Arrays.fill(dist, -1);
			dist[indexMap.get(first_city)] = 0;
			
			for (String city : newOrder) {
				//daca n a fost vizitat
				if (dist[indexMap.get(city)] != -1) {
					//veirific daca are vecini
					if (graph.containsKey(city)) {
						for (String vecin : graph.get(city)) {
							if (dist[indexMap.get(vecin)] < dist[indexMap.get(city)] + 1) {
								dist[indexMap.get(vecin)] = dist[indexMap.get(city)] + 1;
							}
						}
					}
				}
			}
			return dist[indexMap.get(end_city)] + 1;
		}

		private List<String> topologicalSort(Map<String, List<String>> graph, Set<String> cities) {
			//mapez fiecare oras(nod) la nr de muchii care intra in el
			Map<String, Integer> nrMuchii = new HashMap<>();
			for (String city : cities) {
				nrMuchii.put(city, 0);
			}
			for (String x : graph.keySet()) {
				for (String v : graph.get(x)) {
					nrMuchii.put(v, nrMuchii.get(v) + 1);
				}
			}
			//adaug noduri fara muchii care intra in ele
			Queue<String> queue = new LinkedList<>();
			for (String city : nrMuchii.keySet()) {
				if (nrMuchii.get(city) == 0) {
					queue.add(city);
				}
			}
			
			List<String> newOrder = new ArrayList<>();
			while (!queue.isEmpty()) {
				String x = queue.poll();
				newOrder.add(x);
				
				if (graph.containsKey(x)) {
					//parcurg vecinii nodului extras
					for (String v : graph.get(x)) {
						nrMuchii.put(v, nrMuchii.get(v) - 1);
						if (nrMuchii.get(v) == 0) {
							queue.add(v);
						}
					}
				}
			}
			return newOrder;
		}
	}
	public static void main(String[] args) {
		new Task().solve();
	}
}