package io.modelcontextprotocol.server;

import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class AbstractServerTest {

	protected static final McpSchema.Tool newTool = McpSchema.Tool.builder().name("new-tool").build();

	protected static final McpSchema.Prompt prompt = new McpSchema.Prompt("test_prompt_name", "Test Prompt",
			"Test Prompt", List.of());

	protected static final McpServerTransportProvider mockMcpServerTransportProvider = Mockito
		.mock(McpServerTransportProvider.class);

	protected static final McpSchema.Resource resource = McpSchema.Resource.builder()
		.uri("test://resource")
		.name("Test Resource")
		.build();

	protected static final McpSchema.ResourceTemplate template = McpSchema.ResourceTemplate.builder()
		.uriTemplate("test://template/{id}")
		.name("test-template")
		.build();

	@BeforeEach
	void setUp() {
		Mockito.when(mockMcpServerTransportProvider.notifyClients(Mockito.isA(String.class), Mockito.eq(null)))
			.thenReturn(Mono.empty());
	}

	@AfterEach
	void tearDown() {
		Mockito.reset(mockMcpServerTransportProvider);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	abstract void toolUpdateNotificationSentBasedOnCapability(boolean listToolChanges);

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	abstract void toolUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged);

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	abstract void resourceUpdateNotificationSentBasedOnCapability(boolean listResourceChanges);

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	abstract void resourceUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged);

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	abstract void resourceTemplateUpdateNotificationSentBasedOnCapability(boolean listTemplateChanges);

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	abstract void resourceTemplateUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged);

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	abstract void promptUpdateNotificationSentBasedOnCapability(boolean listPromptChanges);

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	abstract void promptUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged);

}
