#Run random commands on ec2
#fab run_command:"ps -ef|grep java" -H <public ip of ec2>

#Deploy new jar on ec2 from s3 and restart boot app
#fab deploy -H <public ip of ec2>  

#Define roledefs and then call like this
#fab run_command:"ps -ef|grep java" -R service-a

#

from fabric.api import local, settings, abort, run, cd, sudo, task, env
from fabric.contrib.console import confirm
import datetime


env.user = 'ec2-user'
env.key_filename = '/home/kchoudha/work/git/otus.pem'
env.roledefs = {
    'service-a': ['18.219.88.12'],
    'aggr': ['18.188.102.66'],
    'discovery': ['18.220.162.231'],
    'ui' : ['18.188.172.233']
}

def test():
	jarName="service-a-1.0-SNAPSHOT.jar"
	serviceName=jarName[:-17] 
	print 'jarName: '+jarName
	print 'serviceName: '+serviceName
	print "wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/"+serviceName+"/"+jarName
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
        run("wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/"+serviceName+"/"+jarName)
    run("(nohup java -jar /tmp/"+jarName+"  > /tmp/nohup.out < /dev/null &)&", pty=False)

def restart():
    code_dir = '/tmp'
    with cd(code_dir):
        jarName=run("ls *.jar")
        serviceName=jarName[:-17]
        print 'jarName: '+jarName
        print 'serviceName: '+serviceName
        sudo("kill -9 `pgrep java` || true")
    run("(nohup java -jar /tmp/"+jarName+"  > /tmp/nohup.out < /dev/null &)&", pty=False)

def deploy_aggr():
    code_dir = '/tmp'
    with cd(code_dir):
        jarName=run("ls *.jar")
        serviceName=jarName[:-17]
        print 'jarName: '+jarName
        print 'serviceName: '+serviceName
        sudo("kill -9 `pgrep java` || true")
        sudo("mv "+jarName+" "+jarName+"."+datetime.datetime.now().isoformat())
        run("wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/"+serviceName+"/"+jarName)
    run("(nohup java -jar /tmp/"+jarName+" <s3 read access key> <secret key> true > /tmp/nohup.out < /dev/null &)&", pty=False)

def run_command(cmd):
    run(cmd)
