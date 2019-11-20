#
# Cookbook Name:: Datameer
# Recipe:: default
#
# Copyright 2015, DragonflyDataFactory
#
# All rights reserved - Do Not Redistribute

datameer_version = node['Datameer']['version']
hadoop_distribution = node['hadoop']['version']
download_url = "http://52.4.146.113:8081/artifactory/simple/Licensed_Softwares/com/dragonfly/factory/softwares/Datameer-5.1.2-cdh-5.1.0-mr1/5.1.2/Datameer-#{datameer_version}-#{hadoop_distribution}.zip"

#Installation
ark 'Datameer-5.1.2-cdh-5.1.0-mr1' do
  url download_url
  owner node['Datameer']['user']
  group node['Datameer']['group']
end

#Changemode to Datameer recursively
execute "change-mode" do
 command "chmod 777 -R /usr/local/dragonfly/factory/workers/Datameer_5_1_2-cdh-5-1-0/lib/Datameer-5.1.2-cdh-5.1.0-mr1"
end


#replace configuration file 

template "configurations" do
  path "/usr/local/dragonfly/factory/workers/Datameer_5_1_2-cdh-5-1-0/lib/Datameer-5.1.2-cdh-5.1.0-mr1/conf/live.properties"
  source "live.properties.erb"
  owner "root"
  group "root"
  mode "0755"
 end

# Enabling datameer service and restart the service if subscribed template has changed.
service "datameer" do
  supports :restart => true, :start => true, :stop => true, :reload => true
  action :nothing
 end

#Run Auto startup scrip
template "datameer" do
  path "/etc/init.d/datameer"
  source "init.conf.erb"
  owner "root"
  group "root"
  mode "0755"
  notifies :enable, "service[datameer]"
  notifies :start, "service[datameer]"
end

execute "datameer" do
 command "/etc/init.d/datameer start"
end

