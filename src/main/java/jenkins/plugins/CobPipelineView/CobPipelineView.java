/**
 * Extension Point: ListView
 * View that contains customized information about the cob-pipeline-plugin
 * 
 * @author Stefan Bauer
 *
 */

package jenkins.plugins.CobPipelineView;

import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import hudson.model.ListView;
import hudson.model.ViewDescriptor;

public class CobPipelineView extends ListView{

	@DataBoundConstructor
    public CobPipelineView(String name) {
		super(name);
	}   
    
	@Extension
    public static final class DescriptorImpl extends ViewDescriptor {
		public DescriptorImpl()
        {
            super();
        }
		
        @Override
        public String getDisplayName() {
            return "Cob Pipeline View";
        }        
    }	
}
