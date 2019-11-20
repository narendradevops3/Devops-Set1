name	         "hazelcast"
maintainer       "narendra reddy g"
maintainer_email "narendra.ganesana@dragonflydf.com"
license          "All rights reserved"
description      "Installs/Configures hazelcast"
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          "0.1.0"

recipe "hazelcast","Downloads, installs and configures standalone node"

depends 'ark', '~> 0.9.1'
depends 'java', '~> 1.32.0'

