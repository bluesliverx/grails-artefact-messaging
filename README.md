This plugin has the ability to add the `message` function just as in controllers to services or other
arbitrarily defined grails artefacts.  The artefacts may be configured in Config.groovy.

By default, the `message` function is injected into services, but may be configured for additional artefacts as well.

### The `message` Method

The `message` method may be used exactly as the controller or tag lib `message` method as described in 
http://grails.org/doc/latest/ref/Tags/message.html.

```groovy
def message = message(code:"default.not.found.message", default:"Not found", args:[
		"My Object",
		"1"
	])
```

### Modifying Affected Artefacts

In order to modify the artefacts that are affected by the plugin, simple add the following lines to your `Config.groovy`
file and add/remove entries as needed:

```groovy
grails.plugins.artefactmessaging.artefacts = [
	[name:'service', plugin:'services'],
	[name:'task', plugin:'quartz'],
]
```

The `name` property is used to retrieve the classes from the Grails application.  For example, the second entry 
above would attempt to retrieve all the quartz task artefacts by using `grailsApplication.taskClasses`.  The 
artefact  messaging plugin automatically will observe all changes to files watched by the plugin configured 
by the `plugin` property.  This property may also be null if reloading is not desired.

### Default Configuration

The default configuration is as follows:

```groovy
grails.plugins.artefactmessaging.artefacts = [
	[name:'service', plugin:'services'],
]
```

This means that all grails services are automatically injected with the `message` function.

### Release Notes

Special thank you to Paul Fernley for his implementation of grabbing a localized message by the current
request bound to the thread (see ArtefactMessagingService).

#### 1.0
* Fix #2 Released as 1.0
* Fix #3 Now supports message: argument like the regular message code does in Grails

#### 0.3

* Fix #1 - default configuration was not working properly

#### 0.2

* Initial release
