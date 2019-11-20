#
# Cookbook Name:: penthodriver
# Recipe:: default
#
# Copyright 2015, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

package "DragonflyPentahoDriver-0.1-1.x86_64" do
   source "http://52.4.146.113:8081/artifactory/simple/yum-local/com/dragonfly/factory/DragonflyPentahoDriver-0.1-1.x86_64/0.0.1/DragonflyPentahoDriver-0.1-1.x86_64-0.0.1.rpm";
   action :install
   provider Chef::Provider::Package::Rpm
end

# Enabling pentahodriver service and restart the service if subscribed template has changed.
service "pentaho-driver" do
  supports :restart => true, :start => true, :stop => true, :reload => true
  action :nothing
 end


#Run Auto startup scrip
template "Pentaho-driver" do
  path "/etc/init.d/pentaho-driver"
  source "init.conf.erb"
  owner "root"
  group "root"
  mode "0755"
  notifies :enable, "service[pentaho-driver]"
  notifies :start, "service[pentaho-driver]"
end

execute "pentaho-driver" do
 command "/etc/init.d/pentaho-driver start"
end


