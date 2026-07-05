# AI Chatbot — Full Stack Java + React

A full-stack AI chatbot application built with Spring Boot and React, 
integrating LLM APIs with advanced retrieval capabilities.

## Projects

### Project 1 — Conversational AI Chatbot ✅
A chatbot with persistent conversation history, system prompt configuration, 
and real-time streaming responses via SSE.

**Tech:** Spring Boot · React · Groq API (Llama 3) · SSE Streaming

**Key concepts:** LLM API integration · Prompt engineering · 
Stateful conversation design · Server-Sent Events

---

### Project 2 — Document Q&A with RAG 🚧 (in progress)
Upload a document and ask questions about it using 
Retrieval-Augmented Generation.

**Tech:** Spring Boot · React · LangChain4j · pgvector · OpenAI Embeddings

---

## Running Locally

### Backend
```bash
cd backend
# Add your Groq API key to .env
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```
