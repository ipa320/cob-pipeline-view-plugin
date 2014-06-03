package jenkins.plugins.CobPipelineViewTable;


import hudson.matrix.MatrixRun;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import jenkins.model.Jenkins;


public class MethodsMatrix implements Interface {

	// Connect to Matrix and get WorstCaseStatus grey, blue, red
	public String getWorstCaseStatus(String matrix_project, String matrix_repository){
		String output = null;		// do not display icon if it's still null
		Boolean b_prio = false; 	// help variable
		
		try{
			if((MatrixProject) Jenkins.getInstance().getItem(matrix_project)!=null){	// if matrixProject is available
				output ="grey.png";				
				
				MethodsGeneral ex_gen = new MethodsGeneral();
				
				MatrixProject matrixProject = (MatrixProject) Jenkins.getInstance().getItem(matrix_project); 	// open matrix project
				MatrixBuild lastMatrixBuild = matrixProject.getLastBuild();				
				
				if (lastMatrixBuild != null) {		// check last build (#) of the matrix job
					for (MatrixRun run : lastMatrixBuild.getRuns()) {	
						if(ex_gen.isKeyWordInString(matrix_repository + ",", run.toString())){		// find the correct matrix entry; set: (cob_android, fmw-sb__prio_build » amd64,prio_build,cob_command_tools__xyz,hydro,precise #4)
														
							if(run.getResult().toString().equals("FAILURE")){	// worst case
								output = "red.png";
								b_prio = true;
				    		}
							else if(run.getResult().toString().equals("SUCCESS") && b_prio == false){
								output = "blue.png";
							}
				    		
						}			    	
					}
				}	
			}
		}
		catch (Exception e){
			output = e.toString();
		}
		
		return output; 							
	}
		
	
	// get all matrix repositories as a function of: matrix_project
	public String[] getMatrixProjectHeadlines(String matrix_project){	
		String str_entries;
		
		MethodsGeneral ex_gen = new MethodsGeneral();
		
		MatrixProject matrixProject = (MatrixProject) Jenkins.getInstance().getItem(matrix_project); 		
		str_entries = matrixProject.getAxes().find("repository").getValueString();
		
		return ex_gen.getSplitString(str_entries, "\\s");		// result: string array with repositories
	}
	
	
	// find repository in matrix and return false,true
	public Boolean findRepositoryInMatrix(String[] matrix_project_headlines, String matrix_repository){
		Boolean output = false;

		Integer loop = 0;
		
		while(loop < matrix_project_headlines.length){
			if(matrix_project_headlines[loop].equals(matrix_repository)){
				output = true;
			}
			
			loop ++;			
		}	
		
		// zwecks FEHLER
		return true;
	}

	
	// get all matrix repositories as a function of: user & matrix_project
	public List<String> getMatrixProjectHeadlinesOfAllUsers(){		
		List <String> output_list = new ArrayList<String>();
		List <String> list_user = new ArrayList<String>();
		
		String matrix_project;
		
		Integer loop_0;
		Integer loop_1;
		
		MethodsGeneral ex_gen = new MethodsGeneral();
		MethodsHudsonCore ex_hud = new MethodsHudsonCore();
		MethodsJob ex_job = new MethodsJob();
		
		
		// collect with user permission: scheint ok zu sein
		if(ex_hud.getCurrentAuthorities().equals("ADMINISTER")){
			list_user = ex_job.getAllUsersOfTopLevel(); 	// all users
		}
		else if (ex_hud.getCurrentAuthorities().equals("READ")){
			list_user.add(ex_hud.getCurrentUser());
		}		
		
		try{
			loop_0 = 0;	
			
			while(loop_0 < list_user.size()){	// <> each users					
				matrix_project = ex_gen.mergeString(list_user.get(loop_0), itf_kw_repository, "__");	// e.g: fmw-sb__prio_build				
				loop_1 = 0;
				
				while(loop_1 < getMatrixProjectHeadlines(matrix_project).length){	// [] each matrix_project				
					output_list.add(list_user.get(loop_0) + " : " + getMatrixProjectHeadlines(matrix_project)[loop_1]); 	// pattern: ":"
				
					loop_1 ++;
				}	
			
				loop_0 ++;
			}
		}
		catch(Exception err){
			output_list.add("getMatrixProjectHeadlinesOfEveryUser error: " + err.toString());
		}	

		return output_list;
	}

	
	// get matrix repository with index: getMatrixProjectHeadlinesOfEveryUser()
	public String getMatrixProjectHeadline(Integer index){	
		MethodsGeneral ex_gen = new MethodsGeneral();		
		return ex_gen.getLastPartOutOfString(getMatrixProjectHeadlinesOfAllUsers().get(index), " : ", 2);
	}
	
	
	// get matrix repository user_id with index: getMatrixProjectHeadlinesOfEveryUser()
	public String getMatrixProjectHeadlineUser(Integer index){	
		MethodsGeneral ex_gen = new MethodsGeneral();		
		return ex_gen.getFirstPartOutOfString(getMatrixProjectHeadlinesOfAllUsers().get(index), " : ");
	}
		
	
	// get matrix project headlines size
	public Integer getMatrixProjectHeadlinesSize(){	
		return getMatrixProjectHeadlinesOfAllUsers().size();
	}	
	
	
	// get summary (worst case icon/link) of matrix repository
	public String getSummaryOutOfDatabase(Integer index_row){
		String state = "blue.png";	
				
		Integer loop_column;
		
		Long lg_check;
		
		Boolean b_state = false;
		
		DatabaseRepositorys memory = DatabaseRepositorys.getInstance();	
				
		loop_column = 1;	// get worst case out of one row: red, grey, blue
		while(loop_column <= itf_job_short_name.length){
				if(memory.getDatabaseRepositorys().get(index_row).get(loop_column).equals("red.png")){
					state = "red.png";	
					b_state = true;
				}
				else if(memory.getDatabaseRepositorys().get(index_row).get(loop_column).equals("grey.png") && b_state != true){
					state = "grey.png";			
				}	
			
			loop_column++;
		}	
		
		return state;
	}
	
	
	// set database with worst case links
	public void setDatabaseRepositorys(){
		Integer loop_row;
		Integer loop_column;
		Integer loop_size;
		
		String worst_case;
		String user_id = "";
		String matrix_project = "";
		String matrix_repository = "";
		
		java.util.Date date_last_success;
		java.util.Date date_last_failure;
		java.util.Date current_last_success;
		java.util.Date current_last_failure;
		java.util.Date last_duration_success;
		java.util.Date last_duration_failure;
		java.util.Date current_duration_success;
		java.util.Date current_duration_failure;
		
		java.util.Date last_duration = new Date(0);
		
		MethodsGeneral ex_gen = new MethodsGeneral();
		
		loop_size = getMatrixProjectHeadlinesOfAllUsers().size();
		
		// set memory
		DatabaseRepositorys memory = DatabaseRepositorys.getInstance();
		memory.initDatabaseRepositorys(loop_size);	// init. storage
		
		loop_row = 0;
		
		while(loop_row < loop_size){	// matrix repositories headlines
			loop_column = 0;

		// Set User
		// Set Repository
			memory.setDatabaseRepositorys(loop_row, loop_column, getMatrixProjectHeadlinesOfAllUsers().get(loop_row));	// index 0, set headline to memory
			
			date_last_success = new Date(0);		// reset
			date_last_failure = new Date(0);
			current_last_success = new Date(0);
			current_last_failure = new Date(0);
			
			last_duration_success = new Date(0);	// reset
			last_duration_failure = new Date(0);		
			current_duration_success = new Date(0);	
			current_duration_failure = new Date(0);
			
			loop_column ++;	// start with index 1 for worst case entries
			while(loop_column <= itf_job_short_name.length){	// run through the hole short names
				worst_case = getRepositoryStatusIcon(loop_row, loop_column - 1);	// get worst case
				
				user_id = getMatrixProjectHeadlineUser(loop_row);
				matrix_repository = ex_gen.delKeyWordOutOfString(getMatrixProjectHeadline(loop_row), " "); 
				matrix_project = user_id + "__" + itf_job_short_name[loop_column - 1];
				
				
			// last success				
				current_last_success = getLastTimestampsPerMatrix(matrix_project, matrix_repository).get(0);
				current_duration_success = getLastTimestampsPerMatrix(matrix_project, matrix_repository).get(2);
				if(date_last_success.equals("")){	// is empty
					date_last_success = current_last_success;
					last_duration_success = current_duration_success;
				}
				else if(date_last_success.compareTo(current_last_success) < 0){		// is not empty
					date_last_success = current_last_success;
					last_duration_success = current_duration_success;
				}
				
				
			// last failure				
				current_last_failure = getLastTimestampsPerMatrix(matrix_project, matrix_repository).get(1);
				current_duration_failure = getLastTimestampsPerMatrix(matrix_project, matrix_repository).get(3);
				if(date_last_failure.equals("")){	// is empty
					date_last_failure = current_last_failure;
					last_duration_failure = current_duration_failure;
				}
				else if(date_last_failure.compareTo(current_last_failure) < 0){	// is not empty
					date_last_failure = current_last_failure;
					last_duration_failure = current_duration_failure;
				}						
								
				memory.setDatabaseRepositorys(loop_row, loop_column, worst_case);		// index 1-6 save worst case icon to memory
				
				loop_column++;
			}			
			
			// last duration
			if(last_duration_failure.compareTo(last_duration_success) < 0){
				last_duration = last_duration_success;
			}
			else{
				last_duration = last_duration_failure;				
			}
			
			memory.setDatabaseRepositorys(loop_row, 7, String.valueOf(date_last_success.getTime()));	// index 7, save last success to memory
			memory.setDatabaseRepositorys(loop_row, 8, String.valueOf(date_last_failure.getTime()));	// index 8, save last failure to memory
			memory.setDatabaseRepositorys(loop_row, 9, String.valueOf(last_duration.getTime()));		// index 9, save last failure to memory;			
			memory.setDatabaseRepositorys(loop_row, 10, getSummaryOutOfDatabase(loop_row));		// index 10, save last failure to memory;	
			
			loop_row++;
		}	
		
	}

	
	// get repository icon from singleton memory database
	public String getDataOutOfDatabase(Integer index_row, Integer index_column, String fct_type){
		String output = "";
		String str_set_data = "";
		String str_get_data = "";

		Long lg_data;
		
		MethodsGeneral ex_gen = new MethodsGeneral();		
		DatabaseRepositorys memory = DatabaseRepositorys.getInstance();
		MethodsHudsonCore ex_hud = new MethodsHudsonCore();
		
		str_set_data = memory.getDatabaseRepositorys().get(index_row).get(index_column + 1);	// get data as string
						
		if(index_row > memory.getSize()){
			output = "";
		}
		else{			
			if(fct_type.equals("else")){	// comments1, commennts2
				output = str_set_data;	
			}	
			else if(fct_type.equals("username")){
				output = ex_gen.getFirstPartOutOfString(str_set_data, " :");
			}
			else if(fct_type.equals("repository")){
				output = ex_gen.getLastPartOutOfString(str_set_data, ":", 2);
			}
			else if(fct_type.equals("icon")){	// index 2-7
				if(str_set_data.equals("") | str_set_data == null){	// disable icon display if no data is available
					str_set_data = "javascript: void(0)";
				}
				else{
					output = ex_hud.getCurrentServerName() + "images/16x16/" + str_set_data;
				}
			}
			else if(fct_type.equals("success")){
			 	lg_data = Long.parseLong(str_set_data);	// cast string to long
			 	
				if(lg_data == 0){	// if 0, than there is no time available -> invisible
					output = "N/A";
				}
				else{
					output = ex_gen.getTimeDifference(ex_gen.getTimeCurrent(), lg_data);
				}
			}
			else if(fct_type.equals("failure")){
				lg_data = Long.parseLong(str_set_data);	// cast string to long
			
				if(lg_data == 0){	// if 0, than there is no time available -> invisible
					output = "N/A";
				}
				else{
					output = ex_gen.getTimeDifference(ex_gen.getTimeCurrent(), lg_data);
				}
			}
			else if(fct_type.equals("duration")){
				lg_data = Long.parseLong(str_set_data);
				
				if(lg_data == 0){	// if 0, than there is no time available -> invisible
					output = "N/A";
				}
				else{
					output = ex_gen.getTimeDifference(lg_data, 0L);
				}			
			}		
		}
		
		return output;
	}
		
	
	// get repository icon from api
	public String getRepositoryStatusIcon(Integer index, Integer job_name_short_index){	
		MethodsJob ex_job = new MethodsJob();
		MethodsGeneral ex_gen = new MethodsGeneral();		
		return ex_job.getJobStatusIcon(getMatrixProjectHeadlineUser(index), ex_gen.delKeyWordOutOfString(getMatrixProjectHeadline(index), " "), itf_job_short_name[job_name_short_index]);
	}
	
	
	// get last times as list[last success, last failure] per matrix
	public List<java.util.Date> getLastTimestampsPerMatrix(String matrix_project, String matrix_repository){
		String output = "getLastTimestampsPerMatrix: no exception thrown";
		Long duration_success = 0L;
		Long duration_failure = 0L;
		
		List <java.util.Date> output_list = new ArrayList<java.util.Date>();
		
		java.util.Date date_success = new Date(0);
		java.util.Date date_failure = new Date(0);		
		
		try{
			if((MatrixProject) Jenkins.getInstance().getItem(matrix_project)!=null){	// if matrixProject is available					
				
				MethodsGeneral ex_gen = new MethodsGeneral();
				
				MatrixProject matrixProject = (MatrixProject) Jenkins.getInstance().getItem(matrix_project); 	// open matrix_project
				MatrixBuild lastMatrixBuild = matrixProject.getLastBuild();				
				
				if (lastMatrixBuild != null) {		// check lastbuild (#) of the matrix job
					for (MatrixRun run : lastMatrixBuild.getRuns()) {	
						if(ex_gen.isKeyWordInString(matrix_repository + ",", run.toString())){		// find the correct matrix_repository; set: (cob_android, fmw-sb__prio_build » amd64,prio_build,cob_command_tools__xyz,hydro,precise #4)
						// latest success							
							if(run.getResult().toString().equals("SUCCESS")){	
								if(date_success.equals("")){	// is empty							
									date_success = run.getTime();
									duration_success = run.getDuration();
								}
								else if(date_success.compareTo(run.getTime()) < 0){	// is not empty
									date_success = run.getTime();
									duration_success = run.getDuration();
								}								
								
								// extend to duration
				    		}
						// latest failure
							else if(run.getResult().toString().equals("FAILURE")){	// 
								if(date_failure.equals("")){	// is empty
									date_failure = run.getTime();
									duration_failure = run.getDuration();
								}
								else if(date_failure.compareTo(run.getTime()) < 0){	// is not empty
									date_failure = run.getTime();
									duration_failure = run.getDuration();
								}
								
								// extend to duration
							}				    		
						}			    	
					}
				}	
			}
		}
		catch (Exception e){
			output = e.toString();
		}
		
		output_list.add(date_success);					// index 0; last success e.g. in fmw-sb__prio_build/cob_commmand
		output_list.add(date_failure);					// index 1;	""
		output_list.add(new Date(duration_success));	// index 2; last duration e.g. in fmw-sb__prio_build/cob_commmand; just could be sent in date format :-)
		output_list.add(new Date(duration_failure));	// index 3; ""

		return output_list; 							
	}



	
	
}







