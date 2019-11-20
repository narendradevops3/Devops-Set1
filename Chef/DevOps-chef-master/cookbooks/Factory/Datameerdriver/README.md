Datameerdriver Cookbook
=======================
Install Datameer driver 

Requirements
------------
Requried Datameer Worker recipe and ark 0.9.3 recipe

Attributes
----------

##Datameerdriver::default
<table>
  <tr>
    <th>Key</th>
    <th>Type</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td><tt>['Datameerdriver']['bacon']</tt></td>
    <td>Boolean</td>
    <td>whether to include bacon</td>
    <td><tt>true</tt></td>
  </tr>
</table>

Usage
-----
#### Datameerdriver::default

`json
{
  "name":"my_node",
  "run_list": [
    "recipe[Datameerdriver]"
  ]
}
```

Contributing
------------
https://github.com/DragonflyDataFactory/DevOps-chef/tree/master/cookbooks/Factory

e.g.
1. Fork the repository on Github
2. Create a named feature branch (like `add_component_x`)
3. Write your change
4. Write tests for your change (if applicable)
5. Run the tests, ensuring they all pass
6. Submit a Pull Request using Github

License and Authors
-------------------
Authors: Narendra reddy Ganesana <narendra.ganesana@dragonflydf.com>

Organisation: DragonflyDatafactory <http://www.dragonflydatafactory.com/>
