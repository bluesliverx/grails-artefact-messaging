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
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
    }
    dependencies {
    }
    plugins {
        test(/*TODO Add back in: ':codenarc:0.20', */':code-coverage:1.2.7') {
            export = false
        }
        build(':release:2.2.1', ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}
