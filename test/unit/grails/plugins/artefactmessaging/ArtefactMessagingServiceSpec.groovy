package grails.plugins.artefactmessaging

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.plugins.CodecsGrailsPlugin
import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceResolvable
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ArtefactMessagingService)
class ArtefactMessagingServiceSpec extends Specification {
    @Shared
            dummyMessageSourceResolvable = new DummyMessageSourceResolvable()

    def setup() {
        service.messageSource = Mock(MessageSource)


    }

    @Unroll
    def "test call to message with error or message calls the correct method on messageSource"() {
        when:
        service.getMessage(params)

        then:
        1 * service.messageSource.getMessage(dummyMessageSourceResolvable, Locale.ENGLISH)

        where:
        params << [[error: dummyMessageSourceResolvable], [message: dummyMessageSourceResolvable]]
    }

    def "test call to message with code, arguments and defaultMessag calls the correct method on messageSource"() {
        when:
        service.getMessage(code: "code", args: [], default: "default")

        then:
        1 * service.messageSource.getMessage("code", [], "default", Locale.ENGLISH)
    }

    @Unroll
    def "test call to message with encodeAs (#encoding) gives the correct output"() {
        given:
        service.messageSource.getMessage(_, _, _, _) >> message

        and:
        mockCodecs()

        expect:
        expectedOutput == service.getMessage(code: "code", args: [], default: "default", encodeAs: encoding)

        where:
        message    | encoding     | expectedOutput
        "<x>A</x>" | null         | "<x>A</x>"
        "<x>A</x>" | 'html'       | "&lt;x&gt;A&lt;/x&gt;"
        "<x>A</x>" | 'xml'        | "&lt;x&gt;A&lt;/x&gt;"
        "<x>A</x>" | 'url'        | "%3Cx%3EA%3C%2Fx%3E"
        "<x>A</x>" | 'javascript' | "\\u003cx\\u003eA\\u003c\\u002fx\\u003e"
        "<x>A</x>" | 'base64'     | "PHg+QTwveD4="


    }

    private mockCodecs() {
        new CodecsGrailsPlugin().providedArtefacts.each { Class codecClass ->
            mockCodec(codecClass)
        }
    }
}

class DummyMessageSourceResolvable implements MessageSourceResolvable {

    @Override
    String[] getCodes() {
        return new String[0]
    }

    @Override
    Object[] getArguments() {
        return new Object[0]
    }

    @Override
    String getDefaultMessage() {
        return null
    }
}
