package es.test.bank.kafka;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.test.bank.model.AccountModel;
import es.test.bank.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
@Data
public class AccountConsumerListener {

	private ObjectMapper objectMapper;
	private final AccountService accountService;

	@KafkaListener(topics = "${app.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.groupid}", containerFactory = "kafkaListenerContainerFactory")
	public void updateAmount(final ConsumerRecord<String, String> consumerRecord) throws IOException {
		String payload = consumerRecord.value();
		LOGGER.info("Receive petition to update the salary of an account", payload);
		AccountModel accountModel = objectMapper.readValue(payload, AccountModel.class);
		accountService.updateAccount(accountModel.getIban(), accountModel.getAmount());
	}

}
