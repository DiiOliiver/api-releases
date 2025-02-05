package br.com.bank.api_releases.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

	@KafkaListener(topics = "account-events", groupId = "bank-group")
	public void listen(String message) {
		System.out.println("Mensagem recebida do Kafka: " + message);
	}
}
