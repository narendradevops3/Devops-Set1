name             'tomcat-all'
maintainer       'Narendra reddy G'
maintainer_email 'narendra.ganesana@dragonflydf.com'
license          'All rights reserved'
description      'Installs/Configures tomcat'
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          '0.3.1'

depends 'ark', '~> 0.9.1'
depends 'java', '~> 1.31.0'
depends 'apt', '~> 2.7.0'

supports 'ubuntu', '= 12.04'
supports 'centos', '>= 6.4'
supports 'redhat', '>= 6.4'
