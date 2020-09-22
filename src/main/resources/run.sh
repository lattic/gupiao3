docker stop gupiao
docker rm gupiao
docker run --name gupiao \
	-v  /home/admin/docker/jenkins/jenkins_data/workspace/gupiao/target:/usr/src/ \
	-w /usr/src \
	java:8 \
	java -jar gupiao-0.0.1-SNAPSHOT.jar
