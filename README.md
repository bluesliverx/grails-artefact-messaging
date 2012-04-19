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

### Configuring Additional Artefacts

Configuring additional artefacts is simple, just add another entry to the `grails.plugins.artefactmessaging.artefacts`
list in `Config.groovy`.

```groovy
grails.plugins.artefactmessaging.artefacts << [
	name:'task',
	plugin:'quartz'
]
```

The `name` property is used to retrieve the classes from the Grails application.  For example, the entry above
would attempt to retrieve all the quartz task artefacts by using `grailsApplication.taskClasses`.  The artefact 
messaging plugin automatically will observe all changes to files watched by the plugin configured by the
`plugin` property.  This property may also be null is reloading is not desired.

### Default Configuration

The default configuration is as follows:

```groovy
grails.plugins.artefactmessaging {
	artefacts = [
		[
			name:"service",
			plugin:"services",
		],
	]
}
```

This means that all grails services are automatically injected with the `message` function.

### Release Notes

Special thank you to Paul Fernley for his implementation of grabbing a localized message by the current
request bound to the thread (see ArtefactMessagingService).

#### 0.2

* Initial release