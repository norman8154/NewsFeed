# AI_USAGE

## 使用的工具

Claude Code 與 Codex，兩者交叉驗證。沒有採用 SpecKit、Open Spec 這類正式的 SDLC 流程。

## 分工

Phase 1 的功能大部分由我先寫出框架，把 API call、UseCase、Repository 的使用模式定義清楚之後，Phase 2 才讓 AI 大量接手實作。這個順序是刻意的：先有一致的骨架，AI 產出的程式碼才會落在同一套慣例裡，而不是每個檔案各寫各的。

我負責需求拆解、架構方向與範圍取捨，AI 負責產出草稿與指出風險。所有進入 repo 的程式碼都經過我逐行閱讀，看不懂或無法為其辯護的段落一律重寫。

## 原封接受

### 序列化套件的選擇

我原本因為熟悉度打算用 Moshi，詢問 AI 的意見時，它指出 Compose Navigation 的 type-safe route 本身就要求 `@Serializable`，`kotlin.plugin.serialization` 一定會存在於專案中；此時再引入 Moshi 等於同時維護兩套序列化機制，並為了產生 DTO adapter 多付一個 KSP pass。

---

### API response model 建置

這部分因為 swagger 文件完整，且 required 欄位標示清楚，因此完全交給 AI 進行建置。建立過程中還發現 article api 有 rate limit。

## 拒絕

### 把 UI 依畫面拆成三個 feature module

初期在決定專案 module 切分方式時有詢問 AI 的意見，它給出的意見是 feature 按照頁面切分成 3 個 module，不過我考量到這 3 個 module 實際上是對應同一個功能模組，且如果有需要共用的 pojo 要下放到更底層的 module，因此沒有採用，最終是使用我原本的構想，同一功能的切在同個模組裡面。

## 重寫

### 異質來源 Feed 組合方式

最一開始版本是直接在 UiState 裡面把 3 個資料源組合成一個資料源，不過因為 data class 不適合含有商業邏輯，因此讓 AI 重新思考。

第二個版本是使用在 ViewModel 中 API response 時使用 Mutex lock 進行資料源組合，不過因為這三個資料源都是改動不同的資料，不會有需要 lock 的情況，最終決定我直接給方案讓 AI 進行實作。

最終版本是使用 async 跟 awaitAll 把需要的資料源 API call 綁在一起，且跟後續要加上的新鮮度策略兼融性也比較好。

## 弄錯而被抓到

### 天氣 API 的欄位型別

天氣的 response model 由 Codex 產出，其中 `generationtime_ms` 被宣告成 `Long`，但 Open-Meteo 實際回傳的是小數（例如 `0.1157522201538086`）。因此天氣的請求每次都失敗。

這個問題是交給 Claude 複驗時，它實際打一次 API、把回應和 model 逐欄位對照才發現的——這也是讓兩個工具交叉驗證的價值。

