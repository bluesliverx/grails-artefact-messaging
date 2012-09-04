grails.work.dir = "target/workdir"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.docs.output.dir = 'target/docs' // for backwards-compatibility, the docs are checked into gh-pages branch

grails.release.scm.enabled = false

codenarc.reports = {
	XmlReport('xml') {
		outputFile = 'target/test-reports/CodeNarcReport.xml'
		title = 'Plugin Report'
	}
	HtmlReport('html') {
		outputFile = 'target/test-reports/CodeNarcReport.html'
		title = 'Plugin Report'
	}
}

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
    }
	plugins {
		test(':codenarc:0.16.1') {
			export = false
		}
		test(':code-coverage:1.2.5') {
			export = false
		}
		build(':release:2.0.0') {
			export = false
		}
	}
}
