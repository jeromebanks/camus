package com.linkedin.camus.schemaregistry;

import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.repo.SchemaEntry;
import org.apache.avro.repo.SchemaValidationException;
import org.apache.avro.repo.Subject;
import org.apache.avro.repo.client.RESTRepositoryClient;
import org.apache.hadoop.conf.Configuration;

/**
 * An implementation of SchemaRegistry that uses Avro's schema registry to
 * manage Avro schemas.
 */
public class AvroRestSchemaRegistry implements SchemaRegistry<Schema> {
	private RESTRepositoryClient client;
	public static final String ETL_SCHEMA_REGISTRY_URL = "etl.schema.registry.url";
	
	 @Override
	public void init(Properties props) {
	     client = new RESTRepositoryClient(props.getProperty(ETL_SCHEMA_REGISTRY_URL));
	}

	@Override
	public String register(String topic, Schema schema) {
		Subject subject = client.lookup(topic);

		//// XXX
		//// JDB FIXME
		//// What is that method suppossed to do ???
		/**
		if (subject == null) {
			subject = client.register(topic, "org.apache.avro.repo.Validator");
		}
		***/

		try {
			return subject.register(schema.toString()).getId();
		} catch (SchemaValidationException e) {
			e.printStackTrace(System.out);
			e.printStackTrace(System.err);
			throw new SchemaRegistryException(e);
		}
	}

	@Override
	public Schema getSchemaByID(String topic, String id) {
		/**
		 *  JDB XXX Get rid of Avro subject  nonsense
		Subject subject = client.lookup(topic);
		

		if (subject == null) {
			throw new SchemaNotFoundException(" Client look up Schema not found for " + topic);
		}
		***/
		

		///SchemaEntry entry = subject.lookupById(id);

		 SchemaEntry entry = client.lookupByID(id);
		if (entry == null)
			throw new SchemaNotFoundException("Schema not found for " + topic
					+ " " + id);

		return Schema.parse(entry.getSchema());
	}

	@Override
	public SchemaDetails<Schema> getLatestSchemaByTopic(String topicName) {
		Subject subject = client.lookup(topicName);

		if (subject == null) {
			throw new SchemaNotFoundException("Schema not found for "
					+ topicName);
		}

		SchemaEntry entry = subject.latest();

		if (entry == null)
			throw new SchemaNotFoundException("Schema not found for "
					+ topicName);

		return new SchemaDetails<Schema>(topicName, entry.getId(),
				Schema.parse(entry.getSchema()));
	}
}
