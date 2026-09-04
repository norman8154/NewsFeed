# DECISIONS

## 1. UI 框架：Compose

目前的 Compose 生態系已經很完整，並且 Compose Navigation 也已經穩定的情況下，XML View 已經沒有明顯優勢。 原本還有考慮 Fragment Navigation + Compose 內頁，這是我比較熟悉的框架，不採用是因為 Compose Navigation 可以避免掉要處理 Fragment 生命週期的情況。

---

## 2. DI：Hilt

有考慮過直接使用 Koin，不過因為時間上的因素加上我比較熟悉 Hilt 的開發流程，因此最終選擇使用 Hilt。

雖然 Koin 跟 Compose 的相性比較好，不過要等到 Runtime 才知道錯誤，相對地需要 test case 夠完整才適合使用。

---

## 3. 持久化：Room，兩張獨立的表

這邊把離線時也可以瀏覽的文章拆成 2 個 table 而非因為資料結構相同而存在同一張 table 使用 flag 來區分應該抓取哪些資料，主要是在使用時可以明確知道哪個 table 的資料是自己需要的，不需要再使用 flag 篩選，且如果後續各自頁面因為需求改變要增減欄位時，就不需要考慮另一邊的相容性問題。

---

## 4. 模組切分

```
:app                UI、Navigation、ViewModel、UseCase、BO
:base:repository    repository 實作
:core:model         API 回應模型、共用 POJO
:core:resource      Color、TextStyle、drawable
:core:retrofit      網路層
:core:room          Room entity / DAO / database
```

選擇把 feature 的 module 直接放在 app module 裡，主因是目前的功能都是跟文章有關的，如果說有其他的功能模組的話才會再進行細分，例如有 Messaging 功能的話，會拆成這樣 :app、:feature:article、:feature:messaging，另外會多出 :base:common 用來放一些共用的商業邏輯或是 Composable。

---

## 5. 序列化：kotlinx.serialization

原本是因為熟悉度的原因打算使用 Moshi，不過由於 Compose Navigation 已經要使用 kotlinx.serialization，為了避免有兩套序列化的套件，因此讓 Retrofit 也使用 kotlinx.serialization 進行序列化。

---

## 6. 異質內容 feed 不採用 Movie

雖然已經有申請到了免費的 API key，不過考慮到目前的 repo 是 public，repo 改成 private 的來回溝通成本，以及避免要處理 API key 洩漏的情況，因此暫不使用此資料來源。

---

## 7. Saved 頁取消收藏後不會即時從列表移除

Saved 列表在取消收藏後不會把該文章馬上從列表中移除，直到下次重開 App 才會消失。

原本的作法是訂閱 Room 的 Flow，收藏狀態一改變畫面就同步更新。但會造成使用者在 Saved 頁按下取消收藏的瞬間，該篇文章立刻從清單中消失，如果是誤觸就沒有辦法復原，必須回到 Feed 頁重新找到那篇文章。改成只增不減之後，取消收藏只會讓書籤圖示變成未收藏狀態，項目本身仍留在畫面上，使用者可以直接再按一次加回來。

---

## 8. 狀態管理：收藏狀態以資料庫為單一來源

Feed、Saved、Detail 三個畫面都會顯示某篇文章是否已收藏，如果各自維護這個狀態就會需要跨頁面溝通，或是在返回頁面時重新 query DB，很容易出現某一頁忘記更新而狀態不一致的情況。這裡的作法是讓收藏的點擊只負責寫入資料庫，畫面狀態則由訂閱 DB 的 Flow 更新。在任何一頁切換收藏，其他頁面都會自動更新，不需要跨畫面傳遞結果。

需要注意的是每次資料庫變更都會重新計算整份清單的旗標。App 體量持續擴大的話會需要調整這個更新策略。

---

## 9. 並行模型：Coroutines + Flow

Feed 首次載入與下拉刷新時，文章、天氣、服務卡三支 API 以 `async` 並行送出，`awaitAll` 等三支都回來之後才組裝清單並發布一次狀態。原本是各自完成就各自更新，但那樣畫面會連續重排三次——天氣先插進第一列、文章回來把它推開、服務卡又插進第六列，視覺上明顯跳動。改成一次發布之後，代價是首屏由最慢的來源決定，這個目前資料源還不多的情況下勉強可以接受。比較正式的話會讓文章先顯示出來，其他區塊繼續流光。

錯誤處理收在 repository：所有 API 呼叫都以 `runCatching` 包裝，對外回傳 `Result`。這樣 ViewModel 只需要處理成功與失敗兩種結果，不必在每個呼叫點寫 try-catch，也不會有例外意外穿透到 `viewModelScope` 造成崩潰。三個來源的失敗各自獨立，因此單一來源掛掉不會取消其他兩支，天氣抓不到時文章仍然正常顯示。

---

## 10. 底部分頁列改用巢狀 NavHost，並移除 Scaffold

初版把底部分頁列放在 NavHost 外層的 Scaffold，以 `currentBackStackEntryAsState` 判斷目前是否為頂層畫面來決定顯示與否。不過當有個頁面是需要滿版顯示，不需要 BottomNavBar 時會有畫面上過早消失的問題。

原因是 destination 在 `navigate()` 被呼叫的當下就變更，遠早於轉場動畫結束，因此底部列在動畫開始的同一幀就被隱藏；同時 Scaffold 的 `innerPadding` 從「有底部列」變成「沒有底部列」，正在做退場動畫的畫面被重新測量。根因是底部列不屬於任何一個畫面，卻要跟著畫面轉場，而它在 Scaffold 上、動畫在 NavHost 裡，兩者沒有任何同步關係。

改為巢狀 NavHost：外層只有主畫面與詳情頁，主畫面內再放一層 NavHost 管理兩個分頁。底部列結構上只存在於主畫面，因此不需要任何顯示邏輯，進出詳情頁時整個主畫面連同底部列一起轉場。連帶把 Scaffold 移除。

