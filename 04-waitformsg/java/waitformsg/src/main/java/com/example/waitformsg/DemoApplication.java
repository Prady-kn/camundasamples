package com.example.waitformsg;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.extension.rest.EnableCamundaRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import camundajar.com.google.gson.JsonObject;
import camundajar.com.google.gson.JsonParser;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
@EnableCamundaRestClient
public class DemoApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(final String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Value("${pathtowatch}")
	String monitorPath;

	@Value("${feign.client.config.remoteRuntimeService.url}")
	String camundaRestEndpointUrl;

	@Autowired
	CamundaClient camundaClient;

	void SubscribeExternalTask() {

		ExternalTaskClient client = ExternalTaskClient.create().baseUrl(camundaRestEndpointUrl).build();

		client.subscribe("demoExternalTask").lockDuration(1000)
		.handler(new DemoExternalTask()).open();

	}

	@Override
	public void run(final String... args) throws Exception {

		SubscribeExternalTask();
		// TODO Auto-generated method stub
		JsonParser parser = new JsonParser();

		try {
			final WatchService watcher = FileSystems.getDefault().newWatchService();
			final Path dir = Paths.get(monitorPath);
			dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);

			System.out.println("Watch Service registered for dir: " + dir.getFileName());
			String processedFileName = "";
			while (true) {

				WatchKey key;
				try {
					key = watcher.take();
				} catch (final InterruptedException ex) {
					return;
				}

				for (final WatchEvent<?> event : key.pollEvents()) {
					final WatchEvent.Kind<?> kind = event.kind();

					@SuppressWarnings("unchecked")
					final WatchEvent<Path> ev = (WatchEvent<Path>) event;
					final Path fileName = ev.context();

					System.out.println(kind.name() + ": " + fileName);

					if (kind == ENTRY_MODIFY && fileName.toString().endsWith(".json")
							&& !processedFileName.equals(fileName.toString())) {

						processedFileName = fileName.toString();

						System.out.println("Found file!!!");
						Object json = parser
								.parse(new FileReader(Paths.get(dir.toString(), fileName.toString()).toString()));
						JsonObject jsonobj = (JsonObject) json;

						String businessKey = jsonobj.get("businessKey").getAsString();
						String operationType = jsonobj.get("operation").getAsString();
						String message = jsonobj.get("message").getAsString();

						try {

							if (operationType.equals("resume")) {
								String messageName = jsonobj.get("messagename").getAsString();
								camundaClient.correlate(businessKey, message, messageName);
							}

							if (operationType.equals("create")) {
								String processDefKey = jsonobj.get("procDefKey").getAsString();

								camundaClient.start(processDefKey, businessKey, message);
							}

							LOGGER.info("Message sent to Camunda");
						} catch (final Exception exp) {

							LOGGER.error(exp.toString());
						}

					}
				}

				final boolean valid = key.reset();
				if (!valid) {
					break;
				}

			}

		} catch (final IOException ex) {
			System.err.println(ex);
		}

	}

}
