package io.modelcontextprotocol.server;

import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.List;

class McpSyncServerTest extends AbstractServerTest {

	private static final McpServerFeatures.SyncToolSpecification toolSpec = McpServerFeatures.SyncToolSpecification
		.builder()
		.tool(newTool)
		.callHandler(
				(exchange, request) -> McpSchema.CallToolResult.builder().content(List.of()).isError(false).build())
		.build();

	private static final McpServerFeatures.SyncResourceSpecification resourceSpecification = new McpServerFeatures.SyncResourceSpecification(
			resource, (exchange, req) -> new McpSchema.ReadResourceResult(List.of()));

	private static final McpServerFeatures.SyncResourceTemplateSpecification resourceTemplateSpecification = new McpServerFeatures.SyncResourceTemplateSpecification(
			template, (exchange, req) -> new McpSchema.ReadResourceResult(List.of()));

	private static final McpServerFeatures.SyncPromptSpecification promptSpecification = new McpServerFeatures.SyncPromptSpecification(
			prompt, (exchange, req) -> new McpSchema.GetPromptResult("Test prompt description", List.of()));

	private McpSyncServer setUpServer(boolean listToolChanges, boolean listResourceChanges, boolean listPromptChanges) {
		McpAsyncServer asyncServer = McpServer.async(mockMcpServerTransportProvider)
			.capabilities(McpSchema.ServerCapabilities.builder()
				.tools(listToolChanges)
				.prompts(listPromptChanges)
				.resources(false, listResourceChanges)
				.build())
			.build();
		return new McpSyncServer(asyncServer);
	}

	private McpSyncServer setUpServer() {
		return setUpServer(true, true, true);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void toolUpdateNotificationSentBasedOnCapability(boolean listToolChanges) {
		McpSyncServer mcpServer = setUpServer(listToolChanges, true, true);
		mcpServer.addTool(toolSpec);

		int expectedTimes = listToolChanges ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_TOOLS_LIST_CHANGED, null);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void toolUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged) {
		McpSyncServer mcpServer = setUpServer();
		mcpServer.addTool(toolSpec, notifyListChanged);

		int expectedTimes = notifyListChanged ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(Mockito.any(), Mockito.any());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void resourceUpdateNotificationSentBasedOnCapability(boolean listResourceChanges) {
		McpSyncServer mcpServer = setUpServer(true, listResourceChanges, true);
		mcpServer.addResource(resourceSpecification);

		int expectedTimes = listResourceChanges ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_RESOURCES_LIST_CHANGED, null);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void resourceUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged) {
		McpSyncServer mcpServer = setUpServer();
		mcpServer.addResource(resourceSpecification, notifyListChanged);

		int expectedTimes = notifyListChanged ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(Mockito.any(), Mockito.any());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void resourceTemplateUpdateNotificationSentBasedOnCapability(boolean listTemplateChanges) {
		McpSyncServer mcpServer = setUpServer(true, listTemplateChanges, true);
		mcpServer.addResourceTemplate(resourceTemplateSpecification);

		int expectedTimes = listTemplateChanges ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_RESOURCES_LIST_CHANGED, null);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void resourceTemplateUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged) {
		McpSyncServer mcpServer = setUpServer();
		mcpServer.addResourceTemplate(resourceTemplateSpecification, notifyListChanged);

		int expectedTimes = notifyListChanged ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(Mockito.any(), Mockito.any());
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void promptUpdateNotificationSentBasedOnCapability(boolean listPromptChanges) {
		McpSyncServer mcpServer = setUpServer(true, true, listPromptChanges);
		mcpServer.addPrompt(promptSpecification);

		int expectedTimes = listPromptChanges ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_PROMPTS_LIST_CHANGED, null);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	@Override
	void promptUpdateNotificationControlledByNotifyFlag(boolean notifyListChanged) {
		McpSyncServer mcpServer = setUpServer();
		mcpServer.addPrompt(promptSpecification, notifyListChanged);

		int expectedTimes = notifyListChanged ? 1 : 0;
		Mockito.verify(mockMcpServerTransportProvider, Mockito.times(expectedTimes))
			.notifyClients(McpSchema.METHOD_NOTIFICATION_PROMPTS_LIST_CHANGED, null);
	}

}
