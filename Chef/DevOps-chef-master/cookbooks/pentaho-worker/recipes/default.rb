#
# Cookbook Name:: pentaho-worker
# Recipe:: default
#
# Copyright 2015, DragonflyDataFactory
#
# All rights reserved - Do Not Redistribute
#
pentaho_version = node['pentaho-worker']['version']
download_url = "http://liquidtelecom.dl.sourceforge.net/project/pentaho/Data%20Integration/5.2/pdi-ce-#{pentaho_version}.zip"

#Installation
ark 'data-integration' do
  url download_url
  home_dir node['tomcat-all']['tomcat_home']
  owner node['pentaho-worker']['user']
  group node['pentaho-worker']['group']
end
