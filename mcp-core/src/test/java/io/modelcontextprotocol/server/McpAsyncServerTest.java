package io.modelcontextprotocol.server;

import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class McpAsyncServerTest extends AbstractServerTest {

	private static final McpServerFeatures.AsyncToolSpecification toolSpec = McpServerFeatures.AsyncToolSpecification
		.builder()
		.tool(newTool)
		.callHandler((exchange, request) -> Mono
			.just(McpSchema.CallToolResult.builder().content(List.of()).isError(false).build()))
		.build();

	private static final McpServerFeatures.AsyncResourceSpecification resourceSpecification = new McpServerFeatures.AsyncResourceSpecification(
			resource, (exchange, req) -> Mono.just(new McpSchema.ReadResourceResult(List.of())));

	private static final McpServerFeatures.AsyncResourceTemplateSpecification resourceTemplateSpecification = new McpServerFeatures.AsyncResourceTemplateSpecification(
			template, (exchange, req) -> Mono.just(new McpSchema.ReadResourceResult(List.of())));

	private static final McpServerFeatures.AsyncPromptSpecification promptSpecification = new McpServerFeatures.AsyncPromptSpecification(
			prompt, (exchange, req) -> Mono.just(new McpSchema.GetPromptResult("Test prompt description", List.of())));

	private McpAsyncServer setUpServer(boolean listToolChanges, boolean listResourceChanges,
			boolean listPromptChanges) {
		return McpServer.async(mockMcpServerTransportProvider)
			.capabilities(McpSchema.ServerCapabilities.builder()
				.tools(listToolChanges)
				.prompts(listPromptChanges)
				.resources(false, listResourceChanges)
				.build())
			.build();
	}

	private McpAsyncServer setUpServer() {
		return setUpServer(true, true, true);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void toolUpdateNotificationSentBasedOnCapability(boolean listToolChanges) {
		McpAsyncServer mcpServer = setUpServer(listToolChanges, true, true);
		StepVerifier.create(mcpServer.addTool(toolSpec)).verifyComplete();

		int expectedTimes = listToolChanges ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_TOOLS_LIST_CHANGED, null);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void toolUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged) {
		McpAsyncServer mcpServer = setUpServer();
		StepVerifier.create(mcpServer.addTool(toolSpec, notifyListChanged)).verifyComplete();

		int expectedTimes = notifyListChanged ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(Mockito.any(), Mockito.any());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void resourceUpdateNotificationSentBasedOnCapability(boolean listResourceChanges) {
		McpAsyncServer mcpServer = setUpServer(true, listResourceChanges, true);
		StepVerifier.create(mcpServer.addResource(resourceSpecification)).verifyComplete();

		int expectedTimes = listResourceChanges ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_RESOURCES_LIST_CHANGED, null);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void resourceUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged) {
		McpAsyncServer mcpServer = setUpServer();
		StepVerifier.create(mcpServer.addResource(resourceSpecification, notifyListChanged)).verifyComplete();

		int expectedTimes = notifyListChanged ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(Mockito.any(), Mockito.any());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void resourceTemplateUpdateNotificationSentBasedOnCapability(boolean listTemplateChanges) {
		McpAsyncServer mcpServer = setUpServer(true, listTemplateChanges, true);
		StepVerifier.create(mcpServer.addResourceTemplate(resourceTemplateSpecification)).verifyComplete();

		int expectedTimes = listTemplateChanges ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_RESOURCES_LIST_CHANGED, null);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void resourceTemplateUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged) {
		McpAsyncServer mcpServer = setUpServer();
		StepVerifier.create(mcpServer.addResourceTemplate(resourceTemplateSpecification, notifyListChanged))
			.verifyComplete();

		int expectedTimes = notifyListChanged ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(Mockito.any(), Mockito.any());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void promptUpdateNotificationSentBasedOnCapability(boolean listPromptChanges) {
		McpAsyncServer mcpServer = setUpServer(true, true, listPromptChanges);
		StepVerifier.create(mcpServer.addPrompt(promptSpecification)).verifyComplete();

		int expectedTimes = listPromptChanges ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_PROMPTS_LIST_CHANGED, null);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void promptUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged) {
		McpAsyncServer mcpServer = setUpServer();
		StepVerifier.create(mcpServer.addPrompt(promptSpecification, notifyListChanged)).verifyComplete();

		int expectedTimes = notifyListChanged ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_PROMPTS_LIST_CHANGED, null);
	}

}
