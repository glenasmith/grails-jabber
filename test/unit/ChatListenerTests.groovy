import org.codehaus.groovy.grails.jabber.ChatListener

import org.apache.log4j.Logger

/**
 * Some sample tests that confirm things are working with a local OpenFire server.
 * Really just smoke tests to see messages are passing around ok.
 */
class ChatListenerTests extends GroovyTestCase {

    private static final Logger log = Logger.getLogger(ChatListenerTests.class)

    void OFFtestSend() {


        ChatListener cl = new ChatListener(host: 'localhost', // serviceName: 'decaf.local',
                                userName: 'glen', password: 'password')

        cl.connect()

        cl.sendJabberMessage("peter@decaf.local", "Just a test case... at ${ new Date() }")

        cl.disconnect()

    }


    void OFFtestListen() {

        ChatListener cl = new ChatListener(host: 'localhost', // serviceName: 'decaf.local',
                                userName: 'glen', password: 'password'
                           )

        // Simulate a Grails service with "onMessage" routine
        Expando mockService = new Expando()
        mockService.onMessage = { msg ->
            println "From ${msg.from} with content ${msg.body}"
        }

        cl.targetService = mockService
        cl.listenerMethod = "onMessage"

        cl.connect()
        cl.listen()

        
        cl.sendJabberMessage("peter@decaf.local", "Hi Peter... Just starting a chat...")
        println "Waiting..."
        Thread.currentThread().sleep(10000)
        cl.disconnect()
        

    }



}