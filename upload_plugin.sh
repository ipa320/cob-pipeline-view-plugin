mvn clean install
scp -r /media/jenkins/Win8/jenkins-plugins/cob-pipeline-view-plugin/target/cob-pipeline-view-plugin.hpi jenkins@build2.care-o-bot.org:/var/lib/jenkins/plugins/
ssh jenkins@build2.care-o-bot.org "sudo service jenkins restart"
