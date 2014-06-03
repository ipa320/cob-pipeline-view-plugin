mvn clean install
scp -r /media/jenkins/Win8/jenkins-plugins/cob-pipeline-view-plugin/target/cob-pipeline-view-plugin.hpi jenkins@jenkins-fmw-sb:/var/lib/jenkins/plugins/
ssh jenkins@jenkins-fmw-sb "sudo service jenkins restart"
