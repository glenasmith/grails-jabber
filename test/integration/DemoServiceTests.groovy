class DemoServiceTests extends GroovyTestCase {


    DemoService demoService

    void testWaitForIncomingMessage() {

        demoService.sendJabberMessage("peter@decaf.local", "Just a tester....")
        Thread.currentThread().sleep 20000
        

    }
}
