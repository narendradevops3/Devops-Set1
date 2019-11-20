Datameer Cookbook
=================
To install Datameer versio 5.1.2 with Cloudera Hadoop Mrv1 5.1 

Requirements
------------
Java cookbook needed

Attributes
----------
default['Datameer']['user'] = 'root'
default['Datameer']['group'] = 'root' 
## installation
default['Datameer']['version'] = '5.1.2'
default['hadoop']['version'] = 'cdh-5.1.0-mr1-5.1.2'
default['Datameer']['set_etc_environment'] = false

Usage
-----
#### Datameer::default

/etc/init.d/datameer start/stop/restart

Just include `Datameer` in your node's `run_list`:

```json
{
  "name":"my_node",
  "run_list": [
    "recipe[Datameer]"
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
Authors: Narendra reddy ganesana <narendra.ganesana@dragonflydf.com>
Organisation: DragonflyDataFactory
