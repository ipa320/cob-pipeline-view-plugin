package jenkins.plugins.CobPipelineViewTable;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseRepositorys {

	public static ArrayList<ArrayList<String>> memory = new ArrayList<ArrayList<String>>();
			
	protected DatabaseRepositorys() {
		// Exists only to defeat instantiation
	}
	
	public static volatile DatabaseRepositorys instance = null;

	public static void reset() {	
	    instance = new DatabaseRepositorys();
	}
			
	public static DatabaseRepositorys getInstance() {
		if(instance == null) {
			instance = new DatabaseRepositorys();
		}		    
		return instance;
	}
	
	public void initDatabaseRepositorys(Integer memory_size){		
		for ( int i=0; i < memory_size; i++){
			memory.add( new ArrayList<String>());	
		}		
	}
	
	public void setDatabaseRepositorys(Integer id_row, Integer id_column, String str1){
		memory.get(id_row).add(id_column, str1);
		
	}
	
	public ArrayList<ArrayList<String>> getDatabaseRepositorys(){		
		return memory;
	}
	
	public Integer getSize(){
		return memory.size();		
	}
	
	public void delDatabaseRepositorys(){ 
		memory.clear();			// delete every entry
		memory.removeAll(null); 	// just delete the size
	}
	
}
