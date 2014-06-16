package jenkins.plugins.CobPipelineViewTable;

public class MethodsBuildableJob implements Interface{

	// get buildable job name
	public String getBuildableJobName(Integer index){
		String output = "";
	
		DatabaseJobs memory = DatabaseJobs.getInstance();	
		MethodsGeneral ex_gen = new MethodsGeneral();
		
		output = ex_gen.getLastPartOutOfString(memory.getDatabaseJobs().get(index).get(0), "__", 2); 	// get jobname out of full_name
		
		return output; 		
	}
	
	
	// get buildable job user
	public String getBuildableJobUser(Integer index){
		String output = "";

		DatabaseJobs memory = DatabaseJobs.getInstance();	
		MethodsGeneral ex_gen = new MethodsGeneral();
		
		output = ex_gen.getFirstPartOutOfString(memory.getDatabaseJobs().get(index).get(0), "__");
		
		return output;
	}

	
	// get buildable job button image
	public String getBuildableJobButtonImage(){	
		MethodsHudsonCore ex_hud = new MethodsHudsonCore();
		return ex_hud.getCurrentServerName() + "/images/16x16/clock.png";	
	}
	
	
	// get buildable button link
	public String getBuildableJobButtonLink(Integer index){		
		String output = "";

		DatabaseJobs memory = DatabaseJobs.getInstance();	
		MethodsHudsonCore ex_hud = new MethodsHudsonCore();
		
		output = ex_hud.getCurrentServerName() + "job/" + memory.getDatabaseJobs().get(index).get(0) + "/build?delay=0sec"; 
		
		return output;	
	}

}
