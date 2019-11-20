#
# Cookbook Name:: hazelcast
# Recipe:: default
#
# Copyright 2015, DragonflyDataFactroy
#
# All rights reserved - Do Not Redistribute
#
hazelcast_url = "http://52.4.146.113:8081/artifactory/simple/Licensed_Softwares/hazelcast/hazelcast/3.5/hazelcast-3.5.zip"
hazelcast_basedir = node['hazelcast']['base_dir']
hazelcast_currentdir = node['hazelcast']['current_dir']
hazelcast_user = node['hazelcast']['user']

user hazelcast_user do
  system true
  shell "/bin/false"
  home hazelcast_basedir
end

ark 'hazelcast' do
  url hazelcast_url
  version node['hazelcast']['version']
  path hazelcast_basedir
  #home_dir hazelcast_currentdir
  append_env_path true
  owner hazelcast_user
  action :put
end

template "/etc/init.d/hazelcast" do
  source "hazelcast.erb"
  mode "0755"
  owner "root"
  group "root"
end

template "#{hazelcast_currentdir}/bin/hazelcast.xml" do
  source "hazelcast.xml.erb"
  mode "0755"
  owner hazelcast_user
  group hazelcast_user
  notifies :reload, 'service[hazelcast]', :immediately
end

service 'hazelcast' do
  supports :status => true, :restart => true, :reload => true
  action :enable
  subscribes :restart, "template[/etc/init.d/hazelcast", :delayed
end

bash "mancenter" do
  code <<-EOL
  mv /opt/hazelcast/mancenter/*.war* /opt/tomcat/webapps
  chmod 777 -R /opt/tomcat/webapps/
  /etc/init.d/hazelcast start
  /etc/init.d/tomcat7 restart
  EOL
end

