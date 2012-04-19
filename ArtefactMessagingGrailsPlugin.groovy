import grails.util.Environment
import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class ArtefactMessagingGrailsPlugin {
	static Logger log = Logger.getLogger('grails.app.conf.BootStrap')
	
    // the plugin version
    def version = "0.1-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

	def license = "APACHE"
	def organization = [ name:"Adaptive Computing", url:"http://adaptivecomputing.com" ]
	def issueManagement = [ system:"Jira", url:"https://github.com/adaptivecomputing/grails-artefact-messaging/issues" ]
	def scm = [ url:"https://github.com/adaptivecomputing/grails-artefact-messaging" ]
	
    def author = "Brian Saville"
    def authorEmail = "bsaville@adaptivecomputing.com"
    def title = "Configurable i18n Messaging for Artefacts"
    def description = '''\
This plugin has the ability to add the message function just as in controllers to services or other
arbitrarily defined grails artefacts.  The artefacts may be configured in Config.groovy.

Please note: No warranty is implied or given with this plugin.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/artefact-messaging"
	
	def observe = []
	def loadAfter = []
	def watchedResources = []
	
	public ArtefactMessagingGrailsPlugin() {
		// TODO Update for grails 2 to not use holders
		def conf = loadConfig(AH.application)
		observe = conf.artefacts.inject([]) { list, artefactConfig ->
			if (artefactConfig.plugin)
				list << artefactConfig.plugin
			list
		}
		loadAfter = observe
	}

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
		def artefactMessagingService = ctx.getBean("artefactMessagingService")
		
		def conf = loadConfig(application)
		
		conf.artefacts.each { artefactConfig ->
			log.debug("Adding messaging to ${artefactConfig.name}")
			application."${artefactConfig.name}Classes".each {
				it.metaClass.message = { Map map ->
					artefactMessagingService.getMessage(map)
				}
			}
		}
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
		if (!event.source || !event.ctx)
			return
			
		def conf = loadConfig(event.application)
		conf.artefacts.each { artefactConfig ->
			def getMethod = "get${artefactConfig.name[0].toUpperCase()}${artefactConfig.name[1..-1]}Class"
			if (event.application."${getMethod}"(event.source.name)) {
				log.debug("Re-adding messaging to ${event.source.name}")
				def artefactMessagingService = event.ctx.getBean("artefactMessagingService")
				event.application."${getMethod}"(event.source.name).metaClass.message = { Map map ->
					artefactMessagingService.getMessage(map)
				}
			}
		}
    }

    def onConfigChange = { event ->
		def artefactMessagingService = event.ctx.getBean("artefactMessagingService")
		
		def conf = loadConfig(event.application)
		
		conf.artefacts.each {
			event.application."${artefactConfig.name}Classes".each {
				it.metaClass.message = { Map map ->
					artefactMessagingService.getMessage(map)
				}
			}
		}
    }
	
	// Borrowed from spring security
	ConfigObject loadConfig(application) {
		GroovyClassLoader classLoader = new GroovyClassLoader(this.getClass().getClassLoader());
		ConfigSlurper slurper = new ConfigSlurper(Environment.getCurrent().getName());
		ConfigObject secondary = null;
		try {
			secondary = (ConfigObject)((ConfigObject)slurper.parse(classLoader.loadClass('DefaultArtefactMessagingConfig')).getProperty("artefactmessaging"));
		} catch (Exception e) {
			log.warn "Could not load artefact messaging default config file DefaultArtefactMessagingConfig"
		}
		
		ConfigObject config = new ConfigObject();
		if (secondary == null) {
			config.putAll(application.config.grails.plugins.artefactmessaging);
		} else {
			config.putAll(secondary.merge(application.config.grails.plugins.artefactmessaging));
		}
		return config;
	}
}
