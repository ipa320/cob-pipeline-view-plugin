package jenkins.plugins.CobPipelineViewDiagramms;

import hudson.Extension;
import hudson.model.RootAction;

@Extension
public class CobPipelineViewDiagramms implements RootAction{
	 
	public String getIconFileName() {
		return "/plugin/data-processing-plugin/img/x-office-presentation.png";
	}
	 
	public String getDisplayName() {
		return "Cob Pipeline View Diagramms";
	}
	
	public String getUrlName() {
		return "CobPipelineViewDiagramms";
	}
	
	
}
