package $package$

import java.util.Properties

import java.lang.Long
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.scala.Serdes
import org.apache.kafka.streams.test.{ ConsumerRecordFactory, OutputVerifier }
import org.apache.kafka.streams.{ StreamsConfig, Topology, TopologyTestDriver }
import org.scalatest._
import matchers._
import wordspec._


class WordCountSpec extends AnyWordSpec with should.Matchers with BeforeAndAfterAll {

  private val stringDeserializer = Serdes.String.deserializer()
  private val longDeserializer: Deserializer[java.lang.Long]   = new LongDeserializer()

  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, StreamSettings.appID)
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, StreamSettings.bootstrapServers)

  val recordFactory = new ConsumerRecordFactory[String, String](StreamSettings.inputTopic,
    new StringSerializer,
    new StringSerializer)

  val topology: Topology             = WordCount.topology().build()
  val testDriver: TopologyTestDriver = new TopologyTestDriver(topology, props)

  "WordCount" should {
    "count words correctly" in {
      testDriver.pipeInput(recordFactory.create(StreamSettings.inputTopic, "", "kafka kafka"))
      OutputVerifier.compareKeyValue[String, java.lang.Long](
        testDriver.readOutput(StreamSettings.outputTopic, stringDeserializer, longDeserializer),
        "kafka",
        1L
      )
      OutputVerifier.compareKeyValue[String, java.lang.Long](
        testDriver.readOutput(StreamSettings.outputTopic, stringDeserializer, longDeserializer),
        "kafka",
        2L
      )
    }
  }

  override def afterAll(): Unit = {
    testDriver.close()
  }

}
