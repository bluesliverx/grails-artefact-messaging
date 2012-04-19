package grails.plugins.artefactmessaging

import grails.util.GrailsWebUtil
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.support.WebApplicationContextUtils
import org.springframework.web.servlet.support.RequestContextUtils

/**
 * Most of the code is from the localizations plugin's
 * Localization domain class, but has been modified to support
 * message(error:...).
 * @author Paul Fernley
 * @author bsaville
 */
class ArtefactMessagingService {
	static transactional = false
	
	def messageSource

	String getMessage(Map parameters) {
		def requestAttributes = RequestContextHolder.getRequestAttributes()
		def applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletContextHolder.getServletContext())
		boolean unbindRequest = false

		// Outside of an executing request, establish a mock version
		if (!requestAttributes) {
			requestAttributes = GrailsWebUtil.bindMockWebRequest(applicationContext)
			unbindRequest = true
		}

		def locale = RequestContextUtils.getLocale(requestAttributes.request)

		// What the heck is going on here with RequestContextUtils.getLocale() returning a String?
		// Don't know why - just fix it!
		if (locale instanceof String) {
			if (locale.length() >= 5) {
				locale = new Locale(locale[0..1], locale[3..4])
			} else {
				locale = new Locale(locale)
			}
		}

		def msg
		if (parameters.error)
			msg = messageSource.getMessage(parameters.error, locale)
		else
			msg = messageSource.getMessage(parameters.code, parameters.args as Object[], parameters.default, locale)

		if (unbindRequest) RequestContextHolder.setRequestAttributes(null)
		if (parameters.encodeAs) {
			switch (parameters.encodeAs.toLowerCase()) {
				case 'html':
					msg = msg.encodeAsHTML()
					break

				case 'xml':
					msg = msg.encodeAsXML()
					break

				case 'url':
					msg = msg.encodeAsURL()
					break

				case 'javascript':
					msg = msg.encodeAsJavaScript()
					break

				case 'base64':
					msg = msg.encodeAsBase64()
					break
			}
		}

		return msg
	}
}
