class DemoService {

    boolean transactional = true

    static expose = ['jabber']

    def onJabberMessage = {msg ->

        println "Eeek a message!!! From ${msg.from()} with body ${msg.body}"
        log.error "Eeek a message!!! From ${msg.from()} with body ${msg.body}"

    }
}
