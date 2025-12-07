# NovaTV 🎙️

A modern voice-powered AI assistant for Android TV, built with Jetpack Compose for TV.


### - Screenshot:
<img width="540" alt="Screenshot_20251207_205054" src="https://github.com/user-attachments/assets/bf022153-8aeb-4efa-86cd-851131fe7404" />


### - Recording:
https://github.com/user-attachments/assets/c9809be6-7cf5-4391-bf2d-403b805fd37a



> **Note:** If the video doesn't play above, you can find it at [`readme_resources/Screen_recording_20251207_204854.mp4`](readme_resources/Screen_recording_20251207_204854.mp4)

## ✨ Features

- 🎤 **Voice Input** — Press the mic button to speak your questions
- 🤖 **AI-Powered Responses** — Powered by Cloudflare Workers AI (Llama 3.1 8B)
- 🔊 **Text-to-Speech** — Bot responses are read aloud automatically
- 📺 **TV-First Design** — Built specifically for Android TV with D-pad navigation
- 🎨 **Modern Dark UI** — Sleek indigo/purple accent theme

## 🏗️ Architecture

### Voice Flow

```
🎤 Record Audio ──► 📝 Speech-to-Text ──► 🤖 LLM ──► 🔊 Text-to-Speech
     (Mic)              (Whisper)        (Llama)      (Android TTS)
```

### Layer Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                        UI Layer                              │
│  ┌─────────────┐  ┌───────────────┐  ┌────────────────────┐  │
│  │ HomeScreen  │  │ HeaderWidget  │  │ VocalAssistantVM   │  │
│  │ (Compose)   │  │ (Compose)     │  │ (STT + LLM + TTS)  │  │
│  └─────────────┘  └───────────────┘  └────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
                              ▲
                              │
                              ▼
┌──────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌─────────────────────┐  ┌────────────────────────────────┐ │
│  │ SpeechToTextUseCase │  │ TextGenerationUseCase          │ │
│  └─────────────────────┘  └────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
                              ▲
                              │
                              ▼
┌──────────────────────────────────────────────────────────────┐
│                       Data Layer                             │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ CloudflareRepositoryImpl                                │ │
│  │  • Speech-to-Text (Whisper)                             │ │
│  │  • Text Generation (Llama 3.1 8B Instruct)              │ │
│  └─────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Android TV device or emulator (API 21+)
- Cloudflare account with Workers AI access

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ChatbotTVCompose.git
   cd ChatbotTVCompose
   ```

2. **Configure API credentials**
   
   Copy the example environment file:
   ```bash
   cp .env.example .env
   ```
   
   Edit `.env` and add your Cloudflare credentials:
   ```properties
   CLOUDFLARE_ACCOUNT_ID=your_account_id
   CLOUDFLARE_API_TOKEN=your_api_token
   ```

3. **Build and run**
   ```bash
   ./gradlew :tv:installDebug
   ```

## 🎮 Usage

| Action | Control |
|--------|---------|
| Navigate | D-pad arrows |
| Select/Confirm | Enter / Center button |
| Start recording | Select mic button, speak |
| Stop recording | Select mic button again |

### Header Buttons

| Button | Function |
|--------|----------|
| 🔔 Notifications | Open app notification settings |
| 📱 Apps | Open system apps manager |
| ⚙️ Settings | Open Android settings |
| 🎤 Mic | Start/stop voice recording |

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **UI** | Jetpack Compose for TV, Material 3 |
| **Architecture** | MVVM, Clean Architecture |
| **DI** | Hilt |
| **Networking** | Retrofit, OkHttp |
| **Serialization** | Kotlinx Serialization |
| **AI Provider** | Cloudflare Workers AI |
| **STT Model** | OpenAI Whisper |
| **LLM Model** | Meta Llama 3.1 8B Instruct (Fast) |
| **TTS** | Android TextToSpeech |

## 📁 Project Structure

```
tv/src/main/java/com/applicassion/ChatbotTVCompose/
├── data/
│   ├── remote/
│   │   └── ai_provider/
│   │       ├── BaseAIService.kt          # Generic AI service interface
│   │       └── cloudflare/
│   │           ├── CloudflareWorkersBaseAIService.kt
│   │           └── dto/                   # Data Transfer Objects
│   └── repository_impl/
│       └── CloudflareRepositoryImpl.kt
├── di/
│   └── CloudflareModule.kt               # Hilt dependency injection
├── domain/
│   ├── model/                            # Domain models
│   ├── repository/
│   │   └── AIRepository.kt
│   └── usecase/
│       ├── SpeechToTextUseCase.kt
│       └── TextGenerationUseCase.kt
├── ui/
│   ├── screens/
│   │   └── home/
│   │       └── HomeScreen.kt
│   ├── widgets/
│   │   ├── HeaderWidget.kt
│   │   ├── ChatBubble.kt
│   │   └── TvCircularProgressIndicator.kt
│   ├── theme/
│   │   └── Color.kt
│   └── VocalAssistantViewModel.kt
└── utils/
    └── Constants.kt
```

## 🔐 Environment Variables

| Variable | Description |
|----------|-------------|
| `CLOUDFLARE_ACCOUNT_ID` | Your Cloudflare account ID |
| `CLOUDFLARE_API_TOKEN` | API token with Workers AI permissions |

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Cloudflare Workers AI](https://developers.cloudflare.com/workers-ai/) for the AI models
- [Jetpack Compose for TV](https://developer.android.com/training/tv/playback/compose) for the UI framework
- [Material Design 3](https://m3.material.io/) for design guidelines

---

<p align="center">
  Built with ❤️ for Android TV
</p>

