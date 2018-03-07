package ladder_game;

import java.util.ArrayList;

public class graphNode {
	private String word;
	private boolean visited;
	private ArrayList<Integer> neighbor;
	
	public graphNode(String word)
	{
		this.word = word;
		visited = false;
		this.neighbor = new ArrayList<Integer>();
	}
	
	public String get_word() {
		return word;
	}
	public void set_word(String word) {
		this.word = word;
	}
	public boolean is_visited() {
		return visited;
	}
	public void set_visited(boolean visited) {
		this.visited = visited;
	}
	public ArrayList<Integer> get_neighbor() {
		return neighbor;
	}
	public void set_neighbor(ArrayList<Integer> neighbor) {
		this.neighbor = neighbor;
	}

}
