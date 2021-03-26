##Description
-------------

DevOps-Chef cookbook is desiged and writter in such a way that they can be used to deploy individual service components on any of the nodes in the infrastructure; in short they can be used for single node 'all-in-one' installs (for testing), right up to multi/many node production installs. 

##Cookbooks
-----------

we have Cookbooks for FACTORY,SWARM and Generic Cookbooks for all install process to DragonFly infrastructure

##Roles
--------

There are a number of different chef roles that are included with these cookbooks. Descriptions of each of the roles can be found in the readme in the roles directory.

##Network configuration
-----------------------

Network configuration is stored in Chef environment. Please see environments/example.json and networking documentation.

##Usage
-----------
one the parent repository (note the --recursive flag - this will ensure that each of the repositories that the submodules point to is also cloned):

git clone --recursive git@github.com:DragonflyDataFactory/DevOps-chef.git

upload your cookbooks and roles to your chef server( assuming you have already configured your knife client with the credentials of your chef server: cd chef-cookbooks knife cookbook upload -o cookbooks --all knife role from file roles/*.rb

##Using CookBooks
-----------------

NOTE: You must use chef >= 0.10.12 NOTE: If you are using Red Hat Enterprise Linux you must use RHEL >= 6 and you must enable the repository rhel-x86_64-server-optional-6

Ensure you have registered, one or more nodes with your chef server. Then use knife to assign roles to your nodes:
for an all-in-one deployment of nova, using keystone and glance, on a node called node1: knife node run_list add node1 'role[allinone]'

for a 2 node deployment of nova, installing all nova controller functions, keystone and glance on node1, and setting node2 up as a compute server: knife node run_list add node1 'role[single-controller]' knife node run_list add node2 'role[single-compute]'

for a multi node deployment as above but with 'N' nova compute nodes knife node run_list add node1 'role[single-controller]' knife node run_list add node2 'role[single-compute]' knife node run_list add node3 'role[single-compute]' ... knife node run_list add node[n] 'role[single-compute]'

for a many node deployment with mysql and keystone on one node, nova core functions and horizon on another, glance on another, rabbitmq on another, and 'N' compute nodes:

knife node run_list add node1 'role[keystone]'

knife node run_list add node1 'role[mysql-master]' 

knife node run_list add node2 'role[nova-controller]' 

knife node run_list add node2 'role[horizon-server]' 

knife node run_list add node3 'role[glance-api]'

knife node run_list add node3 'role[glance-registry]'

knife node run_list add node4 'role[rabbitmq-server]'

knife node run_list add node5 'role[single-compute]' 

knife node run_list add node6 'role[single-compute]' 

knife node run_list add nodeN 'role[single-compute]'

## AUTHOR
----------


Author : Narendra reddy Ganesana <narendra.ganesana>
