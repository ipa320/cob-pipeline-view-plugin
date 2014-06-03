package jenkins.plugins.CobPipelineViewTable;


import java.security.Timestamp;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import hudson.views.ListViewColumn;
import hudson.Extension;
import hudson.model.Descriptor;



/*
 * 
 * Needs
 * 		- List Matrix Jobs
 * 
 */

/**		
 * 
 * @author Stefan Bauer
 * GUI of ListView
 * 
 */

public class CobPipelineViewTable extends ListViewColumn implements Interface{

// TEST ARREA //
//------------------------------------------------------------------------------------------------------------	
	@JavaScriptMethod	// Output Function: to check things
	public String getFakeComment1(Integer index_row){		
		String output = "getFakeComment1: not used";
		
		MethodsJob ex_job = new MethodsJob();
		MethodsBuildableJob ex_bui = new MethodsBuildableJob();
		DatabaseJobs memory = DatabaseJobs.getInstance();	
		MethodsGeneral ex_gen = new MethodsGeneral();

		output = ex_gen.checkUrl("hallo");
		
		//output = ex_gen.getTimeDifference(ex_gen.getTimeCurrent(), 0L);

		//output = ex_job.getJobsOfCobPipelineView().get(index_row);
				
		return output;
	}			
	
	@JavaScriptMethod	// Output Function: to check things
	public String getFakeComment2(Integer index_row){		
		String output = "getFakeComment2: not used";
	
		DatabaseJobs memory = DatabaseJobs.getInstance();
		MethodsMatrix ex_mat = new MethodsMatrix();
		
		//output = memory.getDatabaseJobs().get(index_row).get(0);
		// is buildable or not
				/*
				TopLevelItem project; 
				project.getAllJobs()output
				job sicher;
				//Job<Job<JobT,RunT>, Run<JobT,RunT>> sicher;
				sicher.isBuildable();
				*/
				/*
				Job<Job<Job,RunT>, Run<JobT,RunT>> jobname;
				jobname.isBuildable();
				*/
		
		//output = ex_mat.getDataOutOfDatabase(index_row, 2, "icon");
		
		return output;
	}	
//-------------------------------------------------------------------------------------------------------------	
// END OF TEST AREA //	
	
	/**
     * ONLOAD() 
     */
	

	public Integer getSizeOfDatabaseRepositorys(){
		DatabaseRepositorys memory = new DatabaseRepositorys();
		return memory.getSize();
	}
	
	public Integer getSizeOfDatabaseJobs(){
		DatabaseJobs memory = new DatabaseJobs();
		return memory.getSize();
	}
		
	@JavaScriptMethod
	public Integer getMatrixProjectHeadlinesSize(){    	
	    MethodsMatrix ex_mat = new MethodsMatrix();    	
	    return 6;//ex_mat.getMatrixProjectHeadlinesSize();
	}   
	
	@JavaScriptMethod  	// for Repositorys and Jobs
	public boolean getInitialize(){
		MethodsMatrix ex_mat = new MethodsMatrix();    	
		MethodsJob ex_job = new MethodsJob();
	    
		DatabaseRepositorys memory_repositorys = new DatabaseRepositorys();	
		DatabaseJobs memory_jobs = new DatabaseJobs();
		
		memory_repositorys.delDatabaseRepositorys();	// delete database entries				
		memory_jobs.delDatabaseJobs();
		
		ex_mat.setDatabaseRepositorys();	// add all elements
		ex_job.setDatabaseJobs();
		
		return true;
	}

			
	@JavaScriptMethod
	public String getSummary(Integer index_row){   
		MethodsMatrix ex_mat = new MethodsMatrix();		
	    return ex_mat.getDataOutOfDatabase(index_row, 9, "icon");
	} 
	
	/*public String getWeather(Integer index){
		String output = "";
		
		if(index < getSize_memory_2D()){ 	// exclude empty memory
			 output = "weather";
		}
		
	    return output;		
	}*/

	@JavaScriptMethod
	public String getUserName(Integer index_row){
		MethodsMatrix ex_mat = new MethodsMatrix();		
		return ex_mat.getDataOutOfDatabase(index_row, -1, "username");
	}
	
    @JavaScriptMethod
    public String getRepository(Integer index_row){    	
    	MethodsMatrix ex_mat = new MethodsMatrix();		
		return ex_mat.getDataOutOfDatabase(index_row, -1, "repository");
    }       
    
    @JavaScriptMethod
    public String getRepositoryLink(Integer index){       	    	
    	String output = "javascript: void(0)";
		
		if(index < getSizeOfDatabaseRepositorys()){ 	// exclude empty memory
			 MethodsJob ex_job = new MethodsJob();   
			 output = ex_job.getJobFullNameLink(index);
		}
		
	    return output;    	
    } 
    
    @JavaScriptMethod
    public String getMatrixProjectLink(Integer index, String headline_table){       	
    	String output = "";
		
		if(index < getSizeOfDatabaseRepositorys()){ 	// exclude empty memory
			MethodsMatrix ex_mat = new MethodsMatrix();
			MethodsHudsonCore ex_hud = new MethodsHudsonCore();
			output = ex_hud.getCurrentServerName() + "view/All/job/" + ex_mat.getMatrixProjectHeadlineUser(index) + "__" + headline_table + "/";
		}
		
	    return output; 
    } 
    
    @JavaScriptMethod
    public String getMatrixProjectHeadline(Integer index){    	
    	String output = "";
		
		if(index < getSizeOfDatabaseRepositorys()){ 	// exclude empty memory
			 MethodsMatrix ex_mat = new MethodsMatrix();
			 output = ex_mat.getMatrixProjectHeadline(index);
		}
		
	    return output;
    }
    
    @JavaScriptMethod
    public String getMatrixProjectHeadlineUser(Integer index){ 
    	String output = "";
		
		if(index < getSizeOfDatabaseRepositorys()){ 	// exclude empty memory
			 MethodsMatrix ex_mat = new MethodsMatrix();
			 output = ex_mat.getMatrixProjectHeadlineUser(index);
		}
		
	    return output;
    }
    
    @JavaScriptMethod
    public String getRepositoryStatusIconOutOfDatabase(Integer index_row, Integer index_column){
    	String output = "";
		
		if(index_row < getSizeOfDatabaseRepositorys()){ 	// exclude empty memory
			 MethodsMatrix ex_mat = new MethodsMatrix();
			 output = ex_mat.getDataOutOfDatabase(index_row, index_column, "icon");
		}
		
	    return output;
    }

	@JavaScriptMethod
	public String getLastSuccess(Integer index_row, Integer index_column){		
		String output = "";
		
		if(index_row < getSizeOfDatabaseRepositorys()){ 	// exclude empty memory
			 MethodsMatrix ex_mat = new MethodsMatrix();
			 output = ex_mat.getDataOutOfDatabase(index_row, index_column, "success");	
		}
		
	    return output;
	}
	
	@JavaScriptMethod
	public String getLastFailure(Integer index_row, Integer index_column){			
		String output = "";
		
		if(index_row < getSizeOfDatabaseRepositorys()){ 	// exclude empty memory
			 MethodsMatrix ex_mat = new MethodsMatrix();
			 output = ex_mat.getDataOutOfDatabase(index_row, index_column, "failure");	
		}
		
	    return output;
	}
	
	@JavaScriptMethod
	public String getLastDuration(Integer index_row, Integer index_column){			
		String output = "";
		
		if(index_row < getSizeOfDatabaseRepositorys()){ 	// exclude empty memory
			 MethodsMatrix ex_mat = new MethodsMatrix();
			 output = ex_mat.getDataOutOfDatabase(index_row, index_column, "duration");
		}
		
	    return output;
	}

	
  

    /**
     * BuildableJob 
     */
    @JavaScriptMethod
    public String getBuildableJobUser(Integer index){    	
    	MethodsBuildableJob ex_bui = new MethodsBuildableJob();    	
    	return ex_bui.getBuildableJobUser(index);
    }
    
    @JavaScriptMethod
    public String getBuildableJobName(Integer index){    	
    	MethodsBuildableJob ex_bui = new MethodsBuildableJob();    	
    	return ex_bui.getBuildableJobName(index);
    }
    
    @JavaScriptMethod
    public String getBuildableJobButtonImage(){    	
    	MethodsBuildableJob ex_bui = new MethodsBuildableJob();    	
    	return ex_bui.getBuildableJobButtonImage();
    }
    
    @JavaScriptMethod
    public String getBuildableJobButtonLink(Integer index){    	
    	MethodsBuildableJob ex_bui = new MethodsBuildableJob();    	
    	return ex_bui.getBuildableJobButtonLink(index);
    }
	
	
			
	@Extension
	public static final Descriptor<ListViewColumn> DESCRIPTOR = new DescriptorImpl();	// Includes metadata about configurable instance

	@Override
	public Descriptor<ListViewColumn> getDescriptor(){
		return DESCRIPTOR;
	}

	private static class DescriptorImpl extends Descriptor<ListViewColumn> {
		
		@Override		// List addon to filter criteria of view
		public ListViewColumn newInstance(StaplerRequest req, JSONObject formData) throws FormException {
			return new CobPipelineViewTable();
		}
		
		@Override		// Set name of addon for view configuration
		public String getDisplayName() {
			return "Cob Pipeline View Table";
		}		
	}
}
