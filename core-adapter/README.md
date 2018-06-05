Core Adapter
============

This module represents work with DynamicAdapter.
```DynamicAdapter``` is adapter for [ListItem]s based on 
[Hannes Dorfmann AdapterDelegates](https://github.com/sockeqwe/AdapterDelegates)
but without necessity to create all AdapterDelegates with adapter creation.
Delegates creates by necessity via factory depends on set of [ListItem] added to adapter.
Also ```DynamicAdapter``` encapsulates logic of working with ```DiffUtil```.

For full implementation guide, view [sample project.](../app/src/main/java/com/nullgr/androidcore/adapter)