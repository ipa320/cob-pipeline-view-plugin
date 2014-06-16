package jenkins.plugins.CobPipelineViewTable;

import jenkins.model.Jenkins;
import jenkins.model.JenkinsLocationConfiguration;


public class MethodsHudsonCore{

	// get current user_id for example "ipa_fmw"
	public String getCurrentUser(){				
		return hudson.model.User.current().getId().toString();
	}
	
	
	// get current user_authorities 
	public String getCurrentAuthorities(){
		String output = "getCurrentAuthorities: not used";
		
		if(Jenkins.getInstance().hasPermission(Jenkins.ADMINISTER)){
			output = "ADMINISTER";
		}
		else if(Jenkins.getInstance().hasPermission(Jenkins.READ)){
			output = "READ";
		}
		else{
			output = "NO PERMISSION";
		}
		
		return output;
	}
	
	
	// get current servername
	public String getCurrentServerName(){
		JenkinsLocationConfiguration globalConfig = new JenkinsLocationConfiguration();
		return globalConfig.getUrl();
	}
	
}
