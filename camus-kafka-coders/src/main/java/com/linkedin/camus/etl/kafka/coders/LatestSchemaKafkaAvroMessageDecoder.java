package com.linkedin.camus.etl.kafka.coders;

import kafka.message.Message;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import com.linkedin.camus.coders.CamusWrapper;

public class LatestSchemaKafkaAvroMessageDecoder extends KafkaAvroMessageDecoder
{
	private static final Logger log = Logger.getLogger( LatestSchemaKafkaAvroMessageDecoder.class);

	@Override
	public CamusWrapper<Record> decode(byte[] payload)
	{
		try
		{
			GenericDatumReader<Record> reader = new GenericDatumReader<Record>();
			
			Schema schema = super.registry.getLatestSchemaByTopic(super.topicName).getSchema();
			
			reader.setSchema(schema);
			
             String  payloadStr =     new String(
                                    payload, 
                                    //Message.payloadOffset(message.magic()),
                                    Message.MagicOffset(),
                                    payload.length - Message.MagicOffset()
                            );
			log.info(" Magic offset is  "  + Message.MagicOffset() );
			log.info(" Payload is "  + payloadStr );
			log.info(" Entire string is " + new String(payload));
			
			return new CamusWrapper<Record>(reader.read(
                    null, 
                    decoderFactory.jsonDecoder(
                            schema, 
                            payloadStr
                    )
            ));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}