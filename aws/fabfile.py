from fabric.api import local, settings, abort, run, cd, sudo
from fabric.contrib.console import confirm
import datetime

def test():
	jarName="service-a-1.0-SNAPSHOT.jar"
	serviceName=jarName[:-17]
	print 'jarName: '+jarName
	print 'serviceName: '+serviceName
	print "wget https://s3.us-east-2.amazonaws.com/otus-201708/lib/"+serviceName+"/"+jarName
	print "mv "+jarName+" "+jarName+"."+datetime.datetime.now().isoformat()
	print "nohup java -jar "+jarName+" &"

def deploy():
    code_dir = '/tmp'
    with cd(code_dir):
        jarName=run("ls *.jar")
        serviceName=jarName[:-17]
        print 'jarName: '+jarName
        print 'serviceName: '+serviceName
        sudo("kill -9 `pgrep java` || true")
        sudo("mv "+jarName+" "+jarName+"."+datetime.datetime.now().isoformat())
        run("wget https://s3.us-east-2.amazonaws.com/otus-201708/lib/"+serviceName+"/"+jarName)
    run("(nohup java -jar /tmp/"+jarName+"  > /tmp/nohup.out < /dev/null &)&", pty=False)




        
