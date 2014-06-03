package jenkins.plugins.CobPipelineViewTable;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseJobs {

	public static ArrayList<ArrayList<String>> memory = new ArrayList<ArrayList<String>>();
			
	protected DatabaseJobs() {
		// Exists only to defeat instantiation
	}
	
	public static volatile DatabaseJobs instance = null;

	public static void reset() {	
	    instance = new DatabaseJobs();
	}
			
	public static DatabaseJobs getInstance() {
		if(instance == null) {
			instance = new DatabaseJobs();
		}		    
		return instance;
	}
	
	public void initDatabaseJobs(Integer memory_size){		
		for ( int i=0; i < memory_size; i++){
			memory.add( new ArrayList<String>());	
		}		
	}
	
	public void setDatabaseJobs(Integer id_row, Integer id_column, String str1){
		memory.get(id_row).add(id_column, str1);
		
	}
	
	public ArrayList<ArrayList<String>> getDatabaseJobs(){		
		return memory;
	}
	
	public Integer getSize(){
		return memory.size();		
	}
	
	public void delDatabaseJobs(){ 
		memory.clear();			// delete every entry
		memory.removeAll(null); 	// just delete the size
	}
	
}
