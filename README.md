# NewsFeed

## Build & Run

```
./gradlew installDebug
```

- minSdk 24 / targetSdk 36 / Kotlin / Jetpack Compose
- 單元測試：`./gradlew test`

## 資料來源

| 來源 | 用途 |
|---|---|
| [Spaceflight News API](https://api.spaceflightnewsapi.net/v4/articles/) | 文章清單與詳情（含分頁） |
| [Open-Meteo](https://api.open-meteo.com/v1/forecast) | 天氣卡 |
| [DummyJSON](https://dummyjson.com/products) | 服務卡 |

## 架構

```
:app                UI、Navigation、ViewModel、UseCase、BO
:base:repository    repository 實作（文章、天氣、服務卡、網路狀態、新鮮度）
:core:model         API 回應模型、共用 POJO
:core:resource      Color、TextStyle、drawable
:core:retrofit      網路層
:core:room          Room entity / DAO / database
```

畫面採 MVI：State、Intent、Event 三種型別搭配共用的 BaseViewModel。使用者操作以 Intent 進入 ViewModel，ViewModel 更新 State，一次性的訊息（導航、提示）走 Event。

資料流為 API 回應 → UseCase 轉成 BO → ViewModel 組成畫面狀態。Room 存兩張表：`saved_article`（收藏，存在即代表已收藏）與 `cached_article`（feed 第一頁的快取）。

各項技術選型的理由見 [doc/DECISIONS.md](doc/DECISIONS.md)。

## 「新鮮度」策略


| 來源 | TTL | 理由                                        |
|---|---|-------------------------------------------|
| 文章 | 30 分鐘 | App 的主內容，且需要有一定的即時性，又不能太頻繁讓使用者感到煩躁，取一個中間值 |
| 天氣 | 5 分鐘 | 需要即時性，因此會頻繁更新                             |
| 服務卡 | 6 小時 | 內容幾乎不太會改變，因此把時間拉長                         |

### 觸發規則

使用者從背景或其他頁面返回到 Feed 的時候檢查各資料源的 TTL 後觸發，文章刷新會觸發整頁流光狀態是為了讓使用者明確感受到頁面的資料被替換，天氣不這樣做的原因是這個資訊是次級資訊且更新頻繁，因此使用上次更新時間來告知使用者這個天氣資訊是什麼時候的。

### 目前的限制

天氣與服務卡沒有本地快取，它們的 TTL 只在 App 執行期間生效，取得的資料也不會存進 DB，原因是天氣是一個需要即時性的資料，如果離線時顯示反而會是不必要的資訊；服務卡片則是因為開發時間的限制。

## 異質內容的融合策略

用 SealedClass 把不同來源的資料融進 Feed 列表裡，未來要擴充的時候也只需要擴充該 SealedClass 即可。

主內容只有文章，因此其餘兩個資料來源失敗的時候不會顯示 toast 給使用者知道，只有文章顯示發生錯誤時才通知使用者。這個判斷的理由跟新鮮度策略的判斷方式是一樣的。

## 已知限制

- 天氣使用固定座標（台北），未接入 GPS 定位。
- 只快取 feed 第一頁，離線時可瀏覽 20 篇文章且無法再往下分頁。
- 詳情頁離線時只能開啟已收藏或已在快取中的文章。
- 錯誤提示沒有區分原因（連線失敗、rate limit、伺服器錯誤共用同一則訊息）。
- 服務卡片只能看到 5 則
- 服務卡片 UI 資訊量不足

## 若有更多時間會做什麼

1. 加入 CI（push 觸發 build 與 test）與 Baseline Profile。
2. 針對文章 API rate limit 做更完整的錯誤處理。
3. 服務卡片分頁，而非只會出現在列表的第六列
4. 服務卡片存 DB 且只有在 TTL 過期時打 API，重開 App 也不主動重抓，優先使用 Cached 的資料。
5. UI TestCase，原本有打算在寫 UI 的時候，先在 Modifier 加上 testTag 標記，不過考慮到時間會來不及做 TestCase 就先沒加上去。
6. 開啟 R8 並補齊 proguard-rules。

## AI 使用

開發全程以 Claude Code 跟 Codex 作為主要協作工具並且交叉驗證，沒有採用 SpecKit、Open Spec 這類正式的 SDLC 流程。分工是我決定需求拆解、架構方向與範圍取捨，AI 負責產出草稿、指出風險。Phase 1 的功能大部分由我先寫出框架，定義好專案的整體架構如: API call、UseCase、Repository 等等使用模式後再讓 AI 大量的接手 Phase 2 的功能實作。

## Plan & Sequencing

### 執行順序

實作順序按照先達成 MVP 的順序執行，新聞的列表跟收藏是這個 App 的主要功能，因此優先進行。當新聞瀏覽都完成後再執行內容豐富及 UX 優化。

**Phase 1 — MVP：核心瀏覽與收藏**

1. module、api model 建立
2. News Feed 列表 + 分頁
3. 收藏 News 列表
4. News detail

**Phase 2 — 內容豐富與 UX 優化**

5. News Feed 列表加入 other sources
6. Shimmer、頁面狀態 UI 實作
7. 新鮮度策略

**Phase 3 — 加分項**

8. 搜尋
9. Test Case
10. CI
11. 多語系
12. Dark Theme
