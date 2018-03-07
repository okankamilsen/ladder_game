package ladder_game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class main {
	public static ArrayList<graphNode> words;
	static LinkedList<graphNode> path_queue;
	public static void main(String[] args) throws IOException {
		
		words = new ArrayList<graphNode>();
		FileInputStream fstream = new FileInputStream("wordList.txt");
		BufferedReader breader = new BufferedReader(new InputStreamReader(fstream));
		String line;
		
		while((line=breader.readLine())!=null)
		{
			graphNode newWord = new graphNode(line);
			words.add(newWord);
		}
		String startWord = args[0];  
		String goalWord = args[1];  
		long startT = System.nanoTime();
		System.out.println("Building Graph...");
		build_graph();
		System.out.println("Graph Built.");
		LinkedList<graphNode> s= run(startWord, goalWord);
		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		
		
		if(s==null)
			System.out.println("There is no "+goalWord);
		else {
			int k = s.size();
			for(int i=0;i<k;i++){
				writer.println(s.get(i).get_word());
			}
		}
		writer.close();
		
		long endT = System.nanoTime();
		long output = endT - startT;
        System.out.println("Elapsed time in milliseconds: " + output / 1000000);      
		breader.close();
	}
	public static LinkedList<graphNode> run(String startS, String goalS)
	{
		graphNode start_node = null;
		
		int si = 0;
		int[] node_used = new int[words.size()];
		LinkedList<graphNode> queue = new LinkedList<graphNode>();
		path_queue = new LinkedList<graphNode>();
		for(int i=0; i<words.size(); i++)
		{
			if(words.get(i).get_word().equals(startS)){
				start_node = words.get(i);
				si = i;
				break;
			}
		}
		return search(start_node,si,node_used,queue,goalS,start_node);
	}
	
	private static LinkedList<graphNode> search(graphNode node,int si,int[] node_used,LinkedList<graphNode> queue,String goalS,graphNode sNode){
		graphNode cNode;
		Map<graphNode,graphNode> prev = new HashMap<graphNode,graphNode>();
		if(node != null)
		{
			node.set_visited(true);
			node_used[si] = 1;
			queue.add(node);
			path_queue.add(node);
			
			while(queue.size() != 0)
			{
				cNode = queue.poll();
				if(cNode.get_word().equals(goalS)){
					LinkedList<graphNode> rPath = backtrace(sNode,cNode,prev);
					return rPath;
				}
				
				for(int i:cNode.get_neighbor())
				{
					graphNode neighborNode = words.get(i);
					
					if(node_used[i]!=1)
					{
						node_used[i] = 1;
						cNode.set_visited(true);
						queue.add(neighborNode);
						prev.put(neighborNode, cNode);
						
					}
				}
			}
		}
		return null;
	}
	private static LinkedList<graphNode> backtrace(graphNode start,graphNode end,Map<graphNode,graphNode> prev){
		LinkedList<graphNode> path = new LinkedList<graphNode>();
		LinkedList<graphNode> reverse_path = new LinkedList<graphNode>();
		path.addLast(end);
		while(!path.getLast().equals(start)){
			path.addLast(prev.get(path.getLast()));
		}
		int i=0;
		int s=path.size();
		while(i<s){
			reverse_path.addLast((path.getLast()));
			path.removeLast();
			i++;
		}
		return reverse_path;
	}
	
	
	private static void build_graph()
	{
		for(int i=0; i<words.size()-1; i++)
		{
			graphNode first = words.get(i);
			for(int j=i+1; j<words.size(); j++)
			{
				graphNode second = words.get(j);
				if(isNeighbor(first,second)==1)
				{
					add_neighbor(j,first);
					add_neighbor(i,second);
				}
			}
		}
	}
	
	    		
	private static int isNeighbor(graphNode first, graphNode second)
	{
		
		if(Math.abs(first.get_word().length()-second.get_word().length())!=1)
			return 0;
		
		if(first.get_word().length()<second.get_word().length())
		{
			if(compareChar(first.get_word(),second.get_word())!=1)
				return 0;
		}else
		{
			if(compareChar(second.get_word(),first.get_word())!=1)
				return 0;
		}
		
		return 1;
		
	}
	public static int compareChar(String short_word,String long_word){
		char[] shortCharArray = short_word.toCharArray();
		char[] longCharArray = long_word.toCharArray();
		int ll=long_word.length();
		int sl=short_word.length();
		int count=0;
		for(int i=0; i<sl; i++)
		{
			boolean is = false;
			for(int j=0; j<ll; j++)
			{
				if(shortCharArray[i]==longCharArray[j]){
					is=true;
					count++;
					longCharArray[j] = ' ';
					break;
				}
			}
			if(!is)
				return 0;
		}
		return 1;
	}
	
	public static void add_neighbor(int i,graphNode node)
	{
		node.get_neighbor().add(i);
	}
}
