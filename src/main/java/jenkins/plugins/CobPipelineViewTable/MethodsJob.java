package jenkins.plugins.CobPipelineViewTable;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import jenkins.model.Jenkins;

import org.jvnet.solaris.libzfs.ACLBuilder.PermissionBuilder;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import hudson.matrix.MatrixProject;
import hudson.maven.MavenModule;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.security.Permission;


public class MethodsJob implements Interface{
	// view specifc entries
	private String keyword_pipestarter = "pipe_starter";
	private String keyword_pipestarter_manual = "pipe_starter_manual";
	private String keyword_pipestarter_manual_all = "pipe_starter_manual_all";
	// --------------------
	

	// get specific job links
	public String getJobName(Job job, String job_name_short){
		String job_specific = "";
		
		MethodsGeneral ex_gen = new MethodsGeneral();		
		
		if(ex_gen.isKeyWordInString(keyword_pipestarter, job.getName()) && isKeyWordInJobList(job_specific)){
			job_specific = ex_gen.mergeString(ex_gen.getFirstPartOutOfString(job.getName(), "__"), job_name_short, "__");	
		}
		
		return job_specific;
	}
	
	
	// get every job of a view
	public List<String> getJobsOfCobPipelineView(){
		List<String> output_list = new ArrayList<String>();
		
		for(TopLevelItem job : hudson.model.Hudson.getInstance().getView("cob_pipeline_view").getItems()){ 
			Job jobcast = (Job) job;	// Cast a toplevelitem to job level
			
			output_list.add(job.getName());			
		} 	
		
		return output_list;
	}
	
	
	// set Database for all Jobs which are visible in the Cob-Pipeline-View: Don't forget to set the view settings: permissions for jobs -> build, workspace
	public void setDatabaseJobs(){
		Integer loop_size;
		Integer loop = 0;

		DatabaseJobs memory = DatabaseJobs.getInstance();	
		loop_size = getJobsOfCobPipelineView().size();
				
		memory.initDatabaseJobs(loop_size);	// init. storage
		
		while(loop < loop_size){
			memory.setDatabaseJobs(loop, 0, getJobsOfCobPipelineView().get(loop));
			loop ++;			
		}						
	}
	
	
	// Create new jobs to list needed data to jenkins view
	public String createJob(String matrix_repository){
		String output ="createJob: no exception thrown";
		String job_short_name = "prio_build";	// the definition of job structure is fixed, so we get the value always from the same topic
		String matrix_project;
		String job_name;
		String user_id;
		
		String matrix_project_headlines[];
		
		List<String> user_list = new ArrayList();		
		user_list = getAllUsersOfTopLevel();
		
		Integer loop;
		Integer loop_list = 0;
		
		MethodsGeneral ex_gen = new MethodsGeneral();
		MethodsHudsonCore ex_hudcor = new MethodsHudsonCore();
		MethodsMatrix ex_conmat = new MethodsMatrix();
		
		try{ 
			while(loop_list < user_list.size()){	// create new jobs for each user_id		
				user_id = user_list.get(loop_list);
				loop = 0;
				
				matrix_project = ex_gen.mergeString(user_id, job_short_name, "__");
				matrix_project_headlines = ex_conmat.getMatrixProjectHeadlines(matrix_project);
				
				while(loop < matrix_project_headlines.length){
					job_name = ex_gen.mergeString(user_id, matrix_project_headlines[loop], "__");
					
					if(isKeyWordInJobList(job_name) == false){
						Hudson.getInstance().createProject(FreeStyleProject.class, job_name);
					}				
					
					loop ++;
				}	
				
				loop_list ++;
			}
		}
		catch (Exception e){
			output = e.toString();
		}		
		
		return output;
	}
	
	
	// get every user_id and save it to a string array list
	public List<String> getAllUsersOfTopLevel(){
		Boolean save_it;			
		String user_id;		
		List <String> output = new ArrayList();			
		MethodsGeneral ex_gen = new MethodsGeneral();	
		
		try{
			for(TopLevelItem job : hudson.model.Hudson.getInstance().getView("All").getItems()){ 
				Job jobcast = (Job) job;	// Cast a toplevelitem to job level
				
				save_it = true;
				user_id = ex_gen.getFirstPartOutOfString(jobcast.getName(), "__"); 
	
				if(user_id != null){
					
					// if user_id doesn't exists	
					if(output.contains(user_id) == false){
						output.add(user_id);	
					}							
				}
			} 
		}
		catch(Exception e){
			output.add(0,e.toString());
		}		
		
		return output;
	}
	
	
	// get build status of job
	public String getJobBuildStatus(String job_specific){
		String iconurl = "";
		
		for(TopLevelItem job : hudson.model.Hudson.getInstance().getView("All").getItems()){ 
			Job jobcast = (Job) job;	// Cast a toplevelitem to job level
						
			if(jobcast.getName().equals(job_specific)){		// looking for specific job
				iconurl = jobcast.getBuildStatusUrl();
			}					 		 
		} 
		
		return iconurl;
	}
	
	
	// Search keyword in job.getName() list, EQUAL
	public Boolean isKeyWordInJobList(String keyword){
		Boolean output = false;

		for(TopLevelItem job : hudson.model.Hudson.getInstance().getView("All").getItems()){ 
			   String job_name = job.getName(); 			   
			   
			   if (keyword.equals(job_name))	//EQUAL
				   output = true;			 		 
		} 
	
		return output;
	}

	
	/* - Check vertical and horizontal statements to define if icon should/shouldn't be shown. 
	* - This method is used for two different purposes because display style of the html links should be called first before getting iconurl
	*/	
	public String getJobStatusIcon(String user_id, String matrix_repository, String job_name_short) {
		String iconurl = "";

		String matrix_project;

		MethodsGeneral ex_gen = new MethodsGeneral();
		MethodsMatrix ex_mat = new MethodsMatrix();

		matrix_project = ex_gen.mergeString(user_id, job_name_short, "__");			// fmw-sb__prio_build		
		
		try{				
			if(isKeyWordInJobList(matrix_project)){ 
				if(ex_mat.getWorstCaseStatus(matrix_project, matrix_repository) == null){	// there is no icon for this repository
					iconurl = null;
				}
				else{
					iconurl = ex_mat.getWorstCaseStatus(matrix_project, matrix_repository); // get worst case of matrix, "," to differ between similar expressions like: cob_command_tools and cob_command_tools_xyz																
				}
			}
		}
		catch(Exception e){
			iconurl = e.toString();
		}

		return iconurl;
	}	
	
	
	@JavaScriptMethod
	public String getJobFullNameOutOfArrayList(Integer index){
		String output = "getJobFullNameOutOfArrayList: no exception thrown";		
		
		MethodsMatrix ex_mat = new MethodsMatrix();
		
		try{
			// Check End of List
			if(index < ex_mat.getMatrixProjectHeadlinesOfAllUsers().size()){
				output = ex_mat.getMatrixProjectHeadlinesOfAllUsers().get(index);
			}
			else{
				output = "end of list";
			}
		}
		catch(Exception err){
			output = err.toString();
		}
		
		return output;		
	}
	
		
	// get link of the matrix repository
	public String getJobFullNameLink(Integer index){
		MethodsHudsonCore ex_hud = new MethodsHudsonCore();
		return ex_hud.getCurrentServerName() + "CobPipelineViewDiagramms/?" + getUrlAttachement(index); 
	}
	
		
	public String getUrlAttachement(Integer index){
		String output;
		String headline;
		String username;
		String repository;
		
		String prio_build;
		String prio_nongraphics_test;
		String prio_graphics_test;
		String regular_build;
		String regular_nongraphics_test;
		String regular_graphics_test;
		
		String prio_build_state;
		String prio_nongraphics_test_state;
		String prio_graphics_test_state;
		String regular_build_state;
		String regular_nongraphics_test_state;
		String regular_graphics_test_state;
				
		MethodsMatrix ex_mat = new MethodsMatrix();
		MethodsGeneral ex_gen = new MethodsGeneral();
		MethodsHudsonCore ex_hud = new MethodsHudsonCore();
		
		username = ex_mat.getDataOutOfDatabase(index, -1, "username");
		repository = ex_mat.getDataOutOfDatabase(index, -1, "repository");
		
		headline = username + "_" + repository;
		
	/* Diagram Links */
		prio_build = ex_gen.checkUrl(ex_hud.getCurrentServerName() + "job/" + username + "__prio_build/warnings16/trendGraph/");
		prio_nongraphics_test = ex_gen.checkUrl(ex_hud.getCurrentServerName() + "job/" + username + "__prio_nongraphics_test/test/");
		prio_graphics_test = ex_gen.checkUrl(ex_hud.getCurrentServerName() + "job/" + username + "__prio_graphics_test/test/");
		regular_build = ex_gen.checkUrl(ex_hud.getCurrentServerName() + "job/" + username + "__regular_build/warnings16/trendGraph/png");
		regular_nongraphics_test = ex_gen.checkUrl(ex_hud.getCurrentServerName() + "job/" + username + "__regular_nongraphics_test/test/trend");
		regular_graphics_test = ex_gen.checkUrl(ex_hud.getCurrentServerName() + "job/" + username + "__regular_graphics_test/test/trend");
		
		
	/* Job States */
		prio_build_state = ex_mat.getDataOutOfDatabase(index, 0, "icon");
		prio_nongraphics_test_state = ex_mat.getDataOutOfDatabase(index, 2, "icon");
		prio_graphics_test_state = ex_mat.getDataOutOfDatabase(index, 3, "icon");
		regular_build_state = ex_mat.getDataOutOfDatabase(index, 1, "icon");	
		regular_nongraphics_test_state = ex_mat.getDataOutOfDatabase(index, 4, "icon");
		regular_graphics_test_state = ex_mat.getDataOutOfDatabase(index, 5, "icon");
				
		output = 	"headline=" + headline + "+&" +
					"prio_build=" + prio_build + "+&" +
					"prio_nongraphics_test=" + prio_nongraphics_test + "+&" +
					"prio_graphics_test=" + prio_graphics_test + "+&" +
					"regular_build=" + regular_build+ "+&" +
					"regular_nongraphics_test=" + regular_nongraphics_test + "+&" +
					"regular_graphics_test=" + regular_graphics_test + "+&" +
					"prio_build_state=" + prio_build_state + "+&" +
					"prio_nongraphics_test_state=" + prio_nongraphics_test_state + "+&" +
					"prio_graphics_test_state=" + prio_graphics_test_state + "+&" + 
					"regular_build_state=" + regular_build_state + "+&" +
					"regular_nongraphics_test_state=" + regular_nongraphics_test_state + "+&" +
					"regular_graphics_test_state=" + regular_graphics_test_state;
						
				/*
				"itf_servername=" + itf_servername + "+&" +
				"matrix_repository=" + ex_gen.getLastPartOutOfString(ex_mat.getDataOutOfDatabase(index, -1, "else"), " : ", 1) + "+&" +
				"user_id=" + ex_gen.getFirstPartOutOfString(ex_mat.getDataOutOfDatabase(index, -1, "else")," :");
				*/
		
		return output;		
	}
}
