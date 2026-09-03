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

