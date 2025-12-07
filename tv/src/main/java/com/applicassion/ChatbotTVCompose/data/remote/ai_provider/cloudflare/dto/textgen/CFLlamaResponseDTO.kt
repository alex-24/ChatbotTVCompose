package com.applicassion.ChatbotTVCompose.data.remote.ai_provider.cloudflare.dto.textgen

data class CFLlamaResponseDTO(
    val response: String,
    val tool_calls: List<ToolCallResponseDTO> // tool calls requested by the model
)

data class ToolCallResponseDTO(
    val items: ToolCallItemResponseDTO
)

data class ToolCallItemResponseDTO(
    val argument: String,// tool params (returns object..?)
    val name: String // name of the tool
)