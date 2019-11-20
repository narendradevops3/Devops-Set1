#
# Cookbook Name:: Datameerdriver
# Recipe:: default
#
# Copyright 2015, DaragonflyDatafactroy
#
# All rights reserved - Do Not Redistribute
#
package "DragonflyDatameerDriver-0.1-1.x86_64-0.0.1.rpm" do
   source "http://52.4.146.113:8081/artifactory/simple/yum-local/com/dragonfly/factory/DragonflyDatameerDriver-0.1-1.x86_64/0.0.1/DragonflyDatameerDriver-0.1-1.x86_64-0.0.1.rpm";
   action :install
   provider Chef::Provider::Package::Rpm
end

# Enabling pentahodriver service and restart the service if subscribed template has changed.
service "datameer-driver" do
  supports :restart => true, :start => true, :stop => true, :reload => true
  action :nothing
 end


#Run Auto startup scrip
template "datameer-driver" do
  path "/etc/init.d/datameer-driver"
  source "init.conf.erb"
  owner "root"
  group "root"
  mode "0755"
  notifies :enable, "service[datameer-driver]"
  notifies :start, "service[datameer-driver]"
end

execute "datameer-driver" do
 command "/etc/init.d/datameer-driver start"
end
