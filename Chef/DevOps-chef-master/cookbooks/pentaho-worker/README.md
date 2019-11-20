pentaho-worker Cookbook
=======================
Install pentaho Worker Libraries

Requirements
------------
Pentaho Worker depends on Pentaho-driver Recipe.
so its installed under /usr/local/dragonfly/factory/workers/pentaho_5_2/lib/<data-integration>


Attributes
----------
default['pentaho-worker']['user'] = 'root'
default['pentaho-worker']['group'] = 'root' 
## installation
default['pentaho-worker']['version'] = '5.2.0.0-209'
default['tomcat-all']['set_etc_environment'] = false

Usage
-----
#### pentaho-worker::default
Pentaho-worker conains only pentaho libraries which we have to use from Pentaho Driver

```json
{
  "name":"my_node",
  "run_list": [
    "recipe[pentaho-worker]"
  ]
}
```

Contributing
------------
https://github.com/DragonflyDataFactory/DevOps-chef

e.g.
1. Fork the repository on Github
2. Create a named feature branch (like `add_component_x`)
3. Write your change
4. Write tests for your change (if applicable)
5. Run the tests, ensuring they all pass
6. Submit a Pull Request using Github

License and Authors
-------------------
Authors: narendra reddy ganesana <narendra.ganesana@dragonflydf.com>

Organisation: DragonflyDatafactory
