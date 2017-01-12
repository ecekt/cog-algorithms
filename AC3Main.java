//Ece K. Takmaz COGS 1903673

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class AC3Main {

	public static void main(String[] args) throws IOException {
		
		//read scene contents as:
		//First the junctions and types such as 1,A 2,Y etc.
		//Then &
		//1,2,3,2 indicates that junction 1's 2nd hand is the same edge as junction 3's second hand
		//Hand indices are given according to the clock-wise convention described by Steedman on the last page
		
		//keeps the 1,2,3,2 type of information
		ArrayList<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();
		
		//keeps junction information such as 1,A 2,Y
		ArrayList<String> junctions = new ArrayList<String>();
		
		//file where the scene contents are stored
		String path = "arcs.txt";
		
		//this method reads from the file and fills in the arraylists
		readScene(path, junctions, edges); 
		
		//this structure is to keep junction labels such as A1,A2,A3 for one junction L1,L2 etc. for another
		ArrayList<ArrayList<String>> assignedJunctionLabels = new ArrayList<ArrayList<String>>();
		
		//implements the AC3 arc consistency algorithm to eliminate inconsistent pairings
		AC3(junctions, assignedJunctionLabels, edges);
			
		//After the inconsistent pairings are eliminated, this program performs a backtracking search
		//Currently finds just one interpretation, if there is one.
		
		//All results will be stored in the arraylist of arraylists below
		ArrayList<ArrayList<String>> allResults = new ArrayList<ArrayList<String>>();
		
		//to keep track of labelled and unlabelled junctions 
		ArrayList<Integer> labelled = new ArrayList<Integer>();
		ArrayList<Integer> unlabelled = new ArrayList<Integer>();
		
		for(int j = 0; j < assignedJunctionLabels.size(); j++){
			
			unlabelled.add(j);
		}
		
		//Assignment stores one final interpretation
		ArrayList<String> assignment = new ArrayList<String>();
		
		for(int a = 0; a < assignedJunctionLabels.size(); a++){
			
			assignment.add(null);
		}
		
		//performs backtracking according to the given parameters, returns true if there is a consistent interpretation
		boolean interpretationsExist = backtracking(edges, assignedJunctionLabels, allResults, unlabelled, labelled, assignment);
							
		System.out.println("Final" + allResults);
			
		//results are written to file
		File fout = new File("results.txt");
		FileOutputStream out = new FileOutputStream(fout);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

		writer.write("After AC3: " + assignedJunctionLabels + "\n\n");
		writer.write("After backtracking: " + allResults);
	
		writer.close();		
	
	}
	
	public static void getJunctions(String input, ArrayList<String> junctions){
		
		//Gets the junctions from the file 1,A 2,L, stores junction type [A,L,A,L,T] etc.
		String [] str = input.split(",");
		
		junctions.add(str[1]);
	}
	
	public static void getEdges(String input, ArrayList<ArrayList<Integer>> edges){
		
		//gets edges from the file,such as [[1,2,3,2], [2,1,1,3]]
		
		String [] str = input.split(",");
		
		ArrayList<Integer> eqv = new ArrayList<Integer>();
		
		eqv.add(Integer.parseInt(str[0]));
		eqv.add(Integer.parseInt(str[1]));
		eqv.add(Integer.parseInt(str[2]));
		eqv.add(Integer.parseInt(str[3]));
			
		edges.add(eqv);
	}
	
	public static void readScene(String path, ArrayList<String> junctions, ArrayList<ArrayList<Integer>> edges) 

			throws IOException{
		
		//this method reads scene contents from the file
		File f = new File(path);
	    FileInputStream in = new FileInputStream(f);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    	    
	    String line = "";
	    
	    while ((line = reader.readLine()) != null ) {
	   	 
	    	if(line.contains("&")){
	    		
	    		//read edges
	    	
	    		while((line = reader.readLine()) != null){
	    			
	    			getEdges(line, edges);
	    		}
	    		
	    	}else{
	    		
	    		//read junctions
	    		
	    		getJunctions(line, junctions);
	    	}
	    	
	    }
	    
	    reader.close();
	    
	}
		
	public static ArrayList<String> getLabelsForJunctionType(String junctionType){
		
		ArrayList<String> edgeSubLabels = null;
		
		//TYPE 1 2 3
		//order is as in given steedman's chapter (clock-wise)
		//returns [<,+,>] if the parameter is "A1" for instance
		
		// < outgoing
		// > incoming
		
		if(junctionType.equals("A1")){
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("<");
			edgeSubLabels.add("+");
			edgeSubLabels.add(">");		
			
		}else if(junctionType.equals("A2")){
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("+");
			edgeSubLabels.add("-");
			edgeSubLabels.add("+");
		
		}else if(junctionType.equals("A3")){	
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("-");
			edgeSubLabels.add("+");
			edgeSubLabels.add("-");
			
		}else if(junctionType.equals("Y1")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add(">");
			edgeSubLabels.add("<");
			edgeSubLabels.add("-");
		
		}else if(junctionType.equals("Y2")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("<");
			edgeSubLabels.add("-");
			edgeSubLabels.add(">");

		}else if(junctionType.equals("Y3")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("-");
			edgeSubLabels.add(">");
			edgeSubLabels.add("<");

		}else if(junctionType.equals("Y4")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("-");
			edgeSubLabels.add("-");
			edgeSubLabels.add("-");
			
		}else if(junctionType.equals("Y5")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("+");
			edgeSubLabels.add("+");
			edgeSubLabels.add("+");
		
		}else if(junctionType.equals("L1")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("<");
			edgeSubLabels.add(">");
			
		}else if(junctionType.equals("L2")){
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add(">");
			edgeSubLabels.add("<");

		}else if(junctionType.equals("L3")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add(">");
			edgeSubLabels.add("+");

		}else if(junctionType.equals("L4")){
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("+");
			edgeSubLabels.add("<");

		}else if(junctionType.equals("L5")){
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("-");
			edgeSubLabels.add(">");

		}else if(junctionType.equals("L6")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("<");
			edgeSubLabels.add("-");
			
		}else if(junctionType.equals("T1")){
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("<");
			edgeSubLabels.add(">");
			edgeSubLabels.add("+");
			
		}else if(junctionType.equals("T2")){
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("<");
			edgeSubLabels.add(">");
			edgeSubLabels.add("-");

		}else if(junctionType.equals("T3")){
			
			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("<");
			edgeSubLabels.add(">");
			edgeSubLabels.add("<");
			
		}else if(junctionType.equals("T4")){

			edgeSubLabels = new ArrayList<String>();
			edgeSubLabels.add("<");
			edgeSubLabels.add(">");
			edgeSubLabels.add(">");
		}
		
		return edgeSubLabels;
		
	}

	public static void assignLabelsToJunctions(ArrayList<String> junctions, ArrayList<ArrayList<String>> assignedLabels){
		
		//given junction types, assigns possible initial sub-labels
		//if junction type is A, assigned labels is [A1,A2,A3]
		
		for(int j = 0; j < junctions.size(); j++){
			
			ArrayList<String> labelset = new ArrayList<String>();
			
			if(junctions.get(j).equals("A")){
				
				labelset.add("A1");
				labelset.add("A2");
				labelset.add("A3");
							
				
			}else if(junctions.get(j).equals("Y")){
				
				labelset.add("Y1");
				labelset.add("Y2");
				labelset.add("Y3");
				labelset.add("Y4");
				labelset.add("Y5");
				
			}else if(junctions.get(j).equals("L")){
				
				labelset.add("L1");
				labelset.add("L2");
				labelset.add("L3");
				labelset.add("L4");
				labelset.add("L5");
				labelset.add("L6");
				
			}else if(junctions.get(j).equals("T")){
				
				labelset.add("T1");
				labelset.add("T2");
				labelset.add("T3");
				labelset.add("T4");
				
			}
			
			assignedLabels.add(labelset);
			
		}
		
	}
	
	public static void checkConsistency(ArrayList<String> junctions, ArrayList<Integer> edge, 
									   ArrayList<Integer> pair, ArrayList<ArrayList<String>> assignedLabels,
									   ArrayList<ArrayList<Integer>> queue, 
									   ArrayList<ArrayList<Integer>> edges){
		
		//pair 0 and pair 1 have labels 
		//assigned labels such as [A1,A2,A3],[L1,L2,..]
		//edge is either 0 i0 1 i1 or 1 i1 0 i0 [1,2,3,1]
		
		int index0 = 0;
		int index1 = 0;
		
		//1 and 3 indicate hands of a junction
		//0 and 2 are the junction numbers
		
		//Check for symmetrical pairs as well
		if(edge.get(0) == pair.get(0) && edge.get(2) == pair.get(1)){
			
			index0 = edge.get(1);
			index1 = edge.get(3);
			
		}else if(edge.get(0) == pair.get(1) && edge.get(2) == pair.get(0)){
			
			index0 = edge.get(3);
			index1 = edge.get(1);
			
		}
		
		System.out.println("p " + pair.get(0) + " " + index0 + " " + pair.get(1) + " " + index1);
		
		//this arraylist will keep the consistent labels and they will not be removed
		ArrayList<String> dontRemove = new ArrayList<String>();
		
		//HERE CHECK FOR LABEL CONSISTENCIES
		
		boolean checkCons = false;
		for(int j = 0; j < assignedLabels.get(pair.get(0)-1).size(); j++){
			
			for(int n = 0; n < assignedLabels.get(pair.get(1)-1).size(); n++){
				
				//get labels for a specific hand of junction 0 and junction 1
				//then compare them
				String label0 = getLabelsForJunctionType(assignedLabels.get(pair.get(0)-1).get(j)).get(index0 - 1);
				String label1 = getLabelsForJunctionType(assignedLabels.get(pair.get(1)-1).get(n)).get(index1 - 1);
				
				//incoming for one label means outgoing for another
				//if they are the inverse of each other, they are consistent
				if((label0.equals(">") && label1.equals("<")) 
						|| (label0.equals("<") && label1.equals(">"))){
					
					System.out.println(assignedLabels.get(pair.get(0)-1).get(j) + " " + 				
					label0	+ " " + assignedLabels.get(pair.get(1)-1).get(n) + " " + label1);

					checkCons = true;
					if(checkCons && !dontRemove.contains(assignedLabels.get(pair.get(0)-1).get(j))){
						
							//consistent, don't remove
							dontRemove.add(assignedLabels.get(pair.get(0)-1).get(j));
							
					}
					
				}else if ((label0.equals("+") && label1.equals("+")) 
						||(label0.equals("-") && label1.equals("-"))){
					
					//+ + is consistent
					//- - is consistent
					System.out.println(assignedLabels.get(pair.get(0)-1).get(j) + " " + 				
					label0	+ " " + assignedLabels.get(pair.get(1)-1).get(n) + " " + label1);

					checkCons = true;
					if(checkCons && !dontRemove.contains(assignedLabels.get(pair.get(0)-1).get(j))){
						
							//consistent, don't remove
							dontRemove.add(assignedLabels.get(pair.get(0)-1).get(j));
							
					}
				}
			}
		}
		
		System.out.println("don't remove " + dontRemove);
		System.out.println("labels " + assignedLabels.get(pair.get(0)-1));
		
		//this arraylist will keep the labels to be removed because they were found to be inconsistent
		ArrayList<String> remove = new ArrayList<String>();
		
		//remove others
		if(dontRemove.size() < assignedLabels.get(pair.get(0)-1).size()){
			
			//there are some junction types to be removed
			for(int r = 0; r < assignedLabels.get(pair.get(0)-1).size(); r++){
				
				if(!dontRemove.contains(assignedLabels.get(pair.get(0)-1).get(r))){
					
					//if the junction number is not in dontRemove list, add it to remove list
					remove.add(assignedLabels.get(pair.get(0)-1).get(r));
					
				}
			}
			
			System.out.println("remove " + remove);
			
			for(int m = 0; m < remove.size(); m++){
				
				//remaining labels after the removal of inconsistent ones
				//junction numbers (-1 is necessary as junction numbers start from 1 whereas indices from 0)
				assignedLabels.get(pair.get(0)-1).remove(remove.get(m));
				System.out.println("labels after removal " + assignedLabels.get(pair.get(0) - 1));
			}
			
			//add to queue all k,i if not already in the queue, excluding j,i (and i,i)
			// i,j 1,2: add if not existing 3,1 4,1
			
			ArrayList<Integer> arcPair = new ArrayList<Integer>();
			
			System.out.println("pair " + pair);
			System.out.println("qq " + queue);
			
			//neighbours gives all k excluding j
			ArrayList<Integer> neighboursToAdd = getNeighbours(edges, pair.get(0), pair.get(1));
			
			System.out.println("neighbours to add " + neighboursToAdd);
			for(int t = 0; t < neighboursToAdd.size(); t++){

				arcPair = new ArrayList<Integer>(); //this is [k,i]
				arcPair.add(neighboursToAdd.get(t)); //this is k
				arcPair.add(pair.get(0)); //this is i
				
				if(!queue.contains(arcPair)){
				
					//add to the end of the queue all [k,i] pairs
					queue.add(arcPair);
			
				}
			}
			
		
		}
		
	}
		
	public static ArrayList<Integer> getNeighbours(ArrayList<ArrayList<Integer>> edges, int junction, int alreadyOpened){

		//DOES NOT GIVE ALL NEIGHBOURS, FOR THAT USE getAdjacents
		
		//after i,j is removed
		//returns neighbours of i, excluding j
		//edges don't have to include symmetrical pairs so we check for it as well, hence both 0 and 2
		//this is for the AC3 algorithm to make use of in the queue
		
		
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		
		for(int i = 0; i < edges.size(); i++){
			
			if(edges.get(i).get(0) == junction){
				
				//already opened is j, don't include it 
				//if k already exists, don't add it
				if((edges.get(i).get(2) != alreadyOpened) && (!neighbours.contains(edges.get(i).get(2)))){
					
					neighbours.add(edges.get(i).get(2));
				}
				
			}else if(edges.get(i).get(2) == junction){
				
				if((edges.get(i).get(0) != alreadyOpened) && (!neighbours.contains(edges.get(i).get(0)))){
					
					neighbours.add(edges.get(i).get(0));
				}
				
			}
		}
		
		return neighbours;
	}
	
	public static ArrayList<ArrayList<Integer>> getAdjacents(ArrayList<ArrayList<Integer>> edges, int noOfJunctions){
		
		//all neighbours of all junctions
		//checks for symmetrical pairs as well
		
		ArrayList<ArrayList<Integer>> neighbours = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < noOfJunctions; i++){			

			//neighbours of just one junction
			ArrayList<Integer> neighboursForOne = new ArrayList<Integer>();
	
			for(int e = 0; e < edges.size(); e++){
		
				if(edges.get(e).get(0) == (i + 1)){
					
					if(!neighboursForOne.contains(edges.get(e).get(2))){
						
						neighboursForOne.add(edges.get(e).get(2));
					}
					
				}else if(edges.get(e).get(2) == (i + 1)){
					
					if(!neighboursForOne.contains(edges.get(e).get(0))){
						
						neighboursForOne.add(edges.get(e).get(0));
					}
				}
				
			}
			
			neighbours.add(neighboursForOne);
			
		}
				
		return neighbours;
	}

	public static ArrayList<ArrayList<Integer>> extractArcs(ArrayList<ArrayList<Integer>> edges){
		
		//extract arcs from all edges 
		//[1,2] from [1,3,2,1] (0th and 2nd indices)
		
		ArrayList<ArrayList<Integer>> arcs = new ArrayList<ArrayList<Integer>>();
		
		for(int e = 0; e < edges.size(); e++){
			
			ArrayList<Integer> pair = new ArrayList<Integer>();
			pair.add(edges.get(e).get(0));
			pair.add(edges.get(e).get(2));

			//add symmetrical pairs
			ArrayList<Integer> symPair = new ArrayList<Integer>();
			symPair.add(edges.get(e).get(2));
			symPair.add(edges.get(e).get(0));
			
			if(!arcs.contains(pair) || !arcs.contains(symPair)){
				
				//add if not alredy existing
				arcs.add(pair);
				arcs.add(symPair);
							
			}
		}
		
		return arcs;
	}
	
	public static void AC3(ArrayList<String> junctions, ArrayList<ArrayList<String>> assignedLabels, ArrayList<ArrayList<Integer>> edges){
		
		//First assigns possible labels to junctions [A1,A2,A3] etc.
		assignLabelsToJunctions(junctions, assignedLabels);
		
		//extract arcs from edges 0 and edges 2 [[1,2],[2,1],[1,3],[3,1]]
		//adds symmetrical pairs as well		
		ArrayList<ArrayList<Integer>> arcs = extractArcs(edges);
		
		//queue to keep arc pairs
		ArrayList<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < arcs.size(); i++){
		
			queue.add(arcs.get(i));
		}
		
		//a pair is just a representation of an arc [1,2]
		ArrayList<Integer> pair;
		
		while(queue.size() != 0){
		
			System.out.println(queue);
			
			//remove the first element
			pair = queue.remove(0);		
							
			for(int r = 0; r < edges.size(); r++){
				
				//For all edges
				//pair 0 pair 1
				//edge 0 edges 1 edge 2 edge 3
				//edge 1 and 3 are hands of junctions
				//edge 0 and edge 2 are junction numbers 
				//so compare pair 0 pair 1 vs. edge 0 edge 2 also  the symmetricals
				
				if((edges.get(r).get(0) == pair.get(0) && edges.get(r).get(2) == pair.get(1)) ||		
						(edges.get(r).get(0) == pair.get(1) && edges.get(r).get(2) == pair.get(0))){
					
					//check consistency and equivalence via going through assigned labels 
					
					checkConsistency(junctions, edges.get(r), pair, assignedLabels, queue, edges);
					
				}
			}
		}
		
		System.out.println("Final Assigned Labels " + assignedLabels);	
		
	}
	
	public static boolean checkConsistencyBacktracking(int junction, String jl, ArrayList<ArrayList<Integer>> edges,
														ArrayList<ArrayList<Integer>> adjacency, ArrayList<String> assigned){
		
		//Checks consistency for the backtracking algorithm
		boolean consistent = true;
		
		//such as [<,+,>] for A1
		ArrayList<String> edgeLabel = getLabelsForJunctionType(jl);
		
		//junction and assigned starts from 0
		//adjacency and edges start from 1

		//check junction's neighbours to see if they are assigned some labels
		//if yes, then check their consistency
		//if no return true

		int check0 = -1;
		int check1 = -1;
		
		//check consistency for all adjacent junctions
		for(int a = 0; a < adjacency.get(junction).size(); a++){
			
			if(assigned.get(adjacency.get(junction).get(a) - 1) != null){
				
				//such as [<,>] for L1
				ArrayList<String> edgeLabelNeighbour = getLabelsForJunctionType(assigned.get(adjacency.get(junction).get(a) - 1));
				
				//some labels assigned to neighbours
				System.out.println("Selected edge" + edgeLabel); //such as [<,+,>]
				System.out.println("Neighbour " + edgeLabelNeighbour); //such as [<,>]
				

				for(int r = 0; r < edges.size(); r++){
					
					//these are hands of each junction
					//get them from edges list, if the junctions are connected
					check0 = -1;
					check1 = -1;
					
					if((edges.get(r).get(0) == (junction + 1)) && (edges.get(r).get(2) == adjacency.get(junction).get(a))){
						
						check0 = edges.get(r).get(1)-1;
						check1 = edges.get(r).get(3)-1;
						
					}else if((edges.get(r).get(2) == (junction + 1)) && (edges.get(r).get(0) == adjacency.get(junction).get(a))){
						
						check0 = edges.get(r).get(3)-1;
						check1 = edges.get(r).get(1)-1;
					}
					
					if(check0 != -1 && check1 != -1){

						//check0 hands are assined novel values
						//System.out.println(edgeLabel.get(check0) + edgeLabelNeighbour.get(check1));
						
						//+ +, - -, > <, < > are consistent labelings 
						// > incoming for one junction is < outgoing for another
						if((edgeLabel.get(check0).equals(">") && edgeLabelNeighbour.get(check1).equals("<")) 
						|| (edgeLabel.get(check0).equals("<") && edgeLabelNeighbour.get(check1).equals(">"))
								|| (edgeLabel.get(check0).equals("+") && edgeLabelNeighbour.get(check1).equals("+")) 
								|| (edgeLabel.get(check0).equals("-") && edgeLabelNeighbour.get(check1).equals("-"))){
						
							System.out.println("true" + edgeLabel.get(check0) + edgeLabelNeighbour.get(check1));
							//Don't need to do anything as consistent = true 
						
						}else{
						
						System.out.println("FALSE" + edgeLabel.get(check0) + edgeLabelNeighbour.get(check1));
						
						//given junction number and junction label are not consistent with the remaining assignments
							consistent = false;
							return consistent;
						}
					}
				}
			}
			
		}
		
		return consistent;
	}
	public static boolean backtracking(ArrayList<ArrayList<Integer>> edges, ArrayList<ArrayList<String>> junctionLabels, ArrayList<ArrayList<String>> allResults,
									  ArrayList<Integer> unlabelled, ArrayList<Integer> labelled, ArrayList<String> assignment){
				
		//implements backtracking in a recursive way
		
		boolean success = false;
		
		//if there are no junction labels after the AC3, no consistent interpretation is found
		//hence return false
		if(junctionLabels.isEmpty()){
			
			return false;
			
		}
		
		//gets the adjacent junctions of a junction for 1 it returns [2,3,4] etc. (not starting on 0)
		ArrayList<ArrayList<Integer>> adjacency = getAdjacents(edges, junctionLabels.size());
		
		//selects the first unlabelled junctions
		int selected = unlabelled.get(0);
		
		System.out.println("selected " + selected);
		
		for(int n = 0; n < junctionLabels.get(selected).size(); n++){
			
			if(unlabelled.isEmpty()){
				
				//all junctions are labelled
				return success;				
			}

			//select the first possible junction type A1 for instance
			String jl = junctionLabels.get(selected).get(n);
			System.out.println(n + " " + jl);
			
			//check if the possible junction label is consistent with other assigned values
			if(checkConsistencyBacktracking(selected, jl, edges, adjacency, assignment)){
		
				//remove the selected junction from the unlabelled list
				//as it was to be consistent with prior label assignments to other junctions
				unlabelled.remove(0);
				
				//add the selected junction to the labelled list
				labelled.add(selected);
				
				System.out.println(unlabelled +"\n"+ labelled);
				//selected starts from 0
				//System.out.println(selected);
				
				//add the junction label to the assignment list to the correct index indicating the junction
				//[A1, null, null, null] 
				assignment.set(selected, jl);
				System.out.println("Assignment" + assignment);
				
				if(labelled.size() == adjacency.size()){
					allResults.add(assignment);
					//if all the junctions are assigned a label, return true
					//this is to find more than one assignments later
					return true;
				}
				
				//recursively do the same things for an altered list of unlabelled and labelled lists and assignment
				success = backtracking(edges, junctionLabels, allResults, unlabelled, labelled, assignment);
				
			}/*else{
				
				//here back up!
				return false;
			}*/
			
		}
		
		return success;
	}
}
